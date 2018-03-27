package com.southgt.smosplat.project.service.autoCollect.mcu.Impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.xsocket.connection.INonBlockingConnection;

import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.FileUtil;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.LogUtil;
import com.southgt.smosplat.data.entity.CX_Data;
import com.southgt.smosplat.data.entity.MT_Data;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.data.entity.ZC_Data;
import com.southgt.smosplat.data.service.ICXService;
import com.southgt.smosplat.data.service.IMTService;
import com.southgt.smosplat.data.service.ISWService;
import com.southgt.smosplat.data.service.IZCService;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.data.vo.CabelMeterMeasureData;
import com.southgt.smosplat.data.vo.CxMessureData;
import com.southgt.smosplat.data.vo.StressMeasureData;
import com.southgt.smosplat.data.vo.WaterLevelMeasureData;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.project.entity.CableMeter;
import com.southgt.smosplat.project.entity.Clinometer;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Stress;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;
import com.southgt.smosplat.project.service.ICableMeterService;
import com.southgt.smosplat.project.service.IStressService;
import com.southgt.smosplat.project.service.ISurveyPoint_SWService;
import com.southgt.smosplat.project.service.IWarnningDataService;
import com.southgt.smosplat.project.service.autoCollect.ConnectCallback;
import com.southgt.smosplat.project.service.autoCollect.DeviceMapping;
import com.southgt.smosplat.project.service.autoCollect.MCU_OrderCode;
import com.southgt.smosplat.project.service.autoCollect.mcu.IMcuCollectLogic;

/**
 * 斯比特mcu采集逻辑实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月17日     mohaolin       v1.0.0        create</p>
 *
 */
public class Speed_McuCollectLogic extends Abstract_McuCollectLogic implements IMcuCollectLogic {

	private void modifyRptAndSmpPeriod(Calendar firstCollectTime,int interval) {
		try {
			ApplicationUtil.doConnectAction(mcus.get(0).getNetwork(), new ConnectCallback(mcus.get(0).getNetwork()) {
				@Override
				public void onConnected(INonBlockingConnection connection,String flag,boolean b) {
					//保存connection，用于下一次发送指令
					setConnection(connection);
					System.out.println("进入modify,"+"flag:"+flag+"\r\n");
					int days = interval / 1440;
					int hour = (interval - days * 1440) / 60;
					int min = interval - days * 1440 - 60 * hour;
					String writeString = "";
					String msg = "";
					String year ="";
					String month=""; 
					String day=""; 
					String dtHour=""; 
					String dtMin=""; 
					String dtSecond="";
					String ftHour="";
					String ftMin="";
					String DD=""; 
					String HH=""; 
					String MM="";
					try{
						//获取系统时间,用于设置mcu的系统时间
						Calendar c=Calendar.getInstance();
						c.setTime(new Date());
						year = String.format("%02d", c.get(Calendar.YEAR)).substring(2);
						//calendar取month比系统时间少1，所以要加上1
						month = String.format("%02d", c.get(Calendar.MONTH)+1);
						day = String.format("%02d",c.get(Calendar.DAY_OF_MONTH));
						dtHour=String.valueOf(c.get(Calendar.HOUR_OF_DAY));
						int hourF=Integer.parseInt(dtHour);
						if(hourF<10){
							dtHour=String.format("%02d",hourF);
						}
						dtMin = String.format("%02d", c.get(Calendar.MINUTE));
						dtSecond = String.format("%02d",c.get(Calendar.SECOND));
						//获取采样起点时间
						ftHour=String.valueOf(firstCollectTime.get(Calendar.HOUR_OF_DAY));
						int hourF1=Integer.parseInt(ftHour);
						if(hourF1<10){
							ftHour=String.format("%02d",hourF1);
						}
						ftMin = String.format("%02d", firstCollectTime.get(Calendar.MINUTE));
						//自报和采样的间隔，现在这两个设成一样的，只要采到数据，就自报
						DD = String.format("%02d", days);
						HH = String.format("%02d", hour);
						MM = String.format("%02d", min);
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("时间转换出错");
					}
					switch(flag){
					case MCU_OrderCode.CONNECT_END:
						this.setLastActionFlag(flag);
						this.setActionFlag(MCU_OrderCode.SET_AUTO_REPORT);
						//开启自报功能
						setAutoReport(true);
						break;
					case MCU_OrderCode.SET_AUTO_REPORT:
						if(validateCommond(connection,b)){
						    msg = "mcu" + mcus.get(0).getSn() + "开启自报成功" + new SimpleDateFormat("hh:mm:ss").format(new Date());
							System.out.println(msg);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.SET_TIME);
							writeString = String.format("MCU32 %1$s SetTime %2$s/%3$s/%4$s %5$s:%6$s:%7$s\r\n",
									mcus.get(0).getSn(), year, month, day, dtHour, dtMin, dtSecond);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), writeString);
							sendCommond(writeString);
						}else{//如果不是由于网络原因发送失败（可能是设备原因），重发一次
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.SET_AUTO_REPORT);
							//发送一个测试指令（用开启自报指令当作测试指令），使得回调重新进来，重发上一条
							setAutoReport(true);
						}
						break;
					case MCU_OrderCode.SET_TIME:
						msg = "mcu" + mcus.get(0).getSn() + "时间对准成功" + "\r\n" + new SimpleDateFormat("hh:mm:ss").format(new Date());
						System.out.println(msg);
						ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
						if(validateCommond(connection,b)){
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.SET_COLLECT_START_TIME);
							writeString = String.format("MCU32 %1$s WrSmpStd %2$s%3$s\r\n", mcus.get(0).getSn(),ftHour, ftMin);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), writeString);
							sendCommond(writeString);
						}else{//如果不是由于网络原因发送失败（可能是设备原因），重发一次
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.SET_COLLECT_START_TIME);
							//发送一个测试指令（用开启自报指令当作测试指令），使得回调重新进来，重发上一条
							setAutoReport(true);
						}
						break;
					case MCU_OrderCode.SET_COLLECT_START_TIME:
						if(validateCommond(connection,b)){
							msg = "mcu" + mcus.get(0).getSn() + "修改采样起点成功" + "\r\n" + new SimpleDateFormat("hh:mm:ss").format(new Date());
							System.out.println(msg);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.SET_REPORT_PERIOD);
							writeString = String.format("MCU32 %1$s WrRpt %2$s%3$s\r\n", mcus.get(0).getSn(), HH, MM);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), writeString);
							sendCommond(writeString);
						}else{//如果不是由于网络原因发送失败（可能是设备原因），重发一次
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.SET_REPORT_PERIOD);
							//发送一个测试指令（用开启自报指令当作测试指令），使得回调重新进来，重发上一条
							setAutoReport(true);
						}
						break;
					case MCU_OrderCode.SET_REPORT_PERIOD:
						if(validateCommond(connection,b)){
							msg = "mcu"+mcus.get(0).getSn()+"设置自报周期成功"+"\r\n" + new SimpleDateFormat("hh:mm:ss").format(new Date());
							System.out.println(msg);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.SET_COLLECT_PERIOD);
							writeString = String.format("MCU32 %1$s WrSmpInt %2$s%3$s%4$s\r\n",mcus.get(0).getSn(), DD, HH, MM);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), writeString);
							sendCommond(writeString);
						}else{//如果不是由于网络原因发送失败（可能是设备原因），重发一次
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.SET_COLLECT_PERIOD);
							//发送一个测试指令（用开启自报指令当作测试指令），使得回调重新进来，重发上一条
							setAutoReport(true);
						}
						break;
					case MCU_OrderCode.SET_COLLECT_PERIOD:
						if(validateCommond(connection,b)){
							msg = "mcu"+mcus.get(0).getSn()+"设置采样周期成功"+"\r\n"+"mcu"+mcus.get(0).getSn()+"设置完成"+"\r\n" + new SimpleDateFormat("hh:mm:ss").format(new Date());
							System.out.println(msg);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
							isSetOver=true;
							mcu_firstCollectTime = firstCollectTime;
							ApplicationUtil.endConnectAction(mcus.get(0).getNetwork());
						}
						break;
					}
				}
			});
		}catch(Exception ex){
			LogUtil.error(ex.toString());
		}
	}
	
	@Override
	public void setAutoReport(boolean a) {
		String commond="";
		if(!a){
			commond = String.format("MCU32 %1$s WrReport 0 0 0 0 0\r\n", mcus.get(0).getSn());
		}else if(a){
			commond = String.format("MCU32 %1$s WrReport 1 1 1 1 1\r\n", mcus.get(0).getSn());
		}
		//推送到前台
		ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), commond);
		sendCommond(commond);
	}

	@Override
	public void sendCommond(String commond) {
		try {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			connection.write(commond);
		} catch (BufferOverflowException e) {
			LogUtil.error(e.toString());
		} catch (IOException e) {
			LogUtil.error(e.toString());
		}
		System.out.println(commond+new Date()+"\r\n");
	}

	@Override
	public boolean validateCommond(INonBlockingConnection connection, boolean b) {
		//b是重发上一条指令的标识
		if(b){
			return false;
		}
		System.out.println("收到数据"+"\r\n");
		String data="";
		try {
			int d=connection.available();
			System.out.println("数据长度："+d);
			if(d>0){
				ByteBuffer[] buffer=connection.readByteBufferByLength(d);
				for(int i=0;i<buffer.length;i++){
					data+=byteBufferToString(buffer[i]);
				}
				System.out.println("received:"+new Date().toString()+data+"\r\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(data.contains("OK")){
			if(data.lastIndexOf("\r\n")==data.length()-2){
				data="";
			}
			return true;
		}
		return false;
		
	}
	
	public static String byteBufferToString(ByteBuffer buffer) {
		CharBuffer charBuffer = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer.asReadOnlyBuffer());  
			buffer.flip();
			return charBuffer.toString();
		} catch (Exception ex) {
			//若一个字符集解析字符出错，就换一个字符集
			try{
				Charset charset = Charset.forName("ISO-8859-1");
				CharsetDecoder decoder = charset.newDecoder();
				charBuffer = decoder.decode(buffer.asReadOnlyBuffer());  
				buffer.flip();
				return charBuffer.toString();
			}catch(Exception ex1){
				return null;
			}
		}
	}

	@Override
	public void getAutoReportData() {
		try {
			ApplicationUtil.doConnectAction(mcus.get(0).getNetwork(), new ConnectCallback(mcus.get(0).getNetwork()) {
				
				@Override
				public void onConnected(INonBlockingConnection connection,String flag,boolean b) {
					this.setAllowSendTest(false);//自报不需要发送上一条数据
					setConnection(connection);
					System.out.println("flag:"+flag+"\r\n");
					//端口打开后会多次进入此回调(数据会分多次返回)
					switch(flag){
					case MCU_OrderCode.CONNECT_END:
						//连接成功后进来，这是还没拿到数据
						this.setLastActionFlag(flag);
						this.setActionFlag(MCU_OrderCode.READ_DATA);
						break;
					case MCU_OrderCode.READ_DATA:
						//开始获取数据(所有数据的其中一次返回的部分)
						String segmentData = "";
						//获取一次数据
						segmentData = validateReturnString(connection);//获取返回的数据
						System.out.println("分析的data:"+segmentData+"\r\n");
						LogUtil.info(mcus.get(0).getSn()+"分析的data:"+segmentData);
						if(segmentData.equals("")){
							break;
						}
						// 分析报文,如果数据没有接收完，分析返回2，接收完返回0
						int result = analyseAutoCollectData(segmentData);
						switch (result) {
						case 1:
						case 2://数据没接收完，继续接收
							this.setActionFlag(MCU_OrderCode.READ_DATA);
							break;
						case 0:
							String msg1 = segmentData;
							msg1 += new SimpleDateFormat("hh:mm:ss").format(new Date());
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg1);
							//原始报文存到服务器
							SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
							String dateString = formatter.format(new Date());
							FileUtil.saveMCUFile(mcus.get(0).getProject().getOrgan().getOrganUuid(), mcus.get(0).getProject().getProjectUuid(), uploadFileSrc, dateString, mcus.get(0).getSn(), msg1);
							LogUtil.info("msg1:"+msg1);
							break;
						}
						System.out.println("自报result:" + result);
						LogUtil.info(mcus.get(0).getSn()+"自报result:" + result);
						//数据接收完毕，入库
						if (result == 0) {
							// 数据全部拿到后，断开连接
							this.setActionFlag(MCU_OrderCode.CONNECT_END);
							isGetDataOver=true;
							ApplicationUtil.endConnectAction(mcus.get(0).getNetwork());
						}
						break;
					}
				}
			});
		} catch (Exception e) {
			LogUtil.error(e.toString());
		}
	}
	/**
	 * 解析每一段数据，将其整到一起 
	 */
	public String validateReturnString(INonBlockingConnection connection) {
		String result = "";
		try {
			int d=connection.available();
			System.out.println("数据长度："+d+"\r\n");
			if(d>0){
				ByteBuffer[] buffer=connection.readByteBufferByLength(d);
				for(int i=0;i<buffer.length;i++){
					String temp=byteBufferToString(buffer[i]);
					System.out.println(buffer[i].toString()+"\r\n");
					data+=temp;
				}
				System.out.println("result:"+result+"/////"+new Date()+"\r\n");
				if(data.lastIndexOf("\r\n")==data.length()-2){
					result=data;
					data="";
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int analyseAutoCollectData(String data) {
		String subStringedStr = "";
		List<String> sMcuNo = new ArrayList<>();
		List<Integer> snIndex = new ArrayList<>();
		if(data.contains("Sn=") && data.contains("ZXN")){
			String[] dataArray = data.split("\r|\n", -1);
			for (int k = 0; k < dataArray.length; k++) {
				if (dataArray[k].contains("Sn=")) {
					if (dataArray[k].length() > 8) {
						sMcuNo.add(dataArray[k].substring(
								dataArray[k].indexOf("Sn=") + 3,
								dataArray[k].indexOf("Sn=") + 3 + 5));
						snIndex.add(data.indexOf(dataArray[k]));
					}
				}
			}
			//截去不是当前mcu的报文
			Boolean isFined = false;
			for(int m = 0; m < sMcuNo.size(); m++){
				if(sMcuNo.get(m).equals(mcus.get(0).getSn())){
					if(m == sMcuNo.size()-1){
						subStringedStr = data.substring(snIndex.get(m));
						dataArray = subStringedStr.split("\r|\n", -1);
					}else{
						subStringedStr = data.substring(snIndex.get(m),snIndex.get(m + 1));
						dataArray = subStringedStr.split("\r|\n", -1);
					}
					isFined = true;
					LogUtil.info("处理后的报文：------"+subStringedStr);
					break;
				}
			}
			//报文中若找到了当前dtu的报文才进行解析，否则不处理
			if(isFined){
				for (String str : dataArray) {
					try {
						try {
							if (str.length() < 4)
								continue;
							if (str.substring(0, 2) == "Sn")
								continue;
							if (str.substring(0, 2) == "No")
								continue;
							if (str.substring(0, 4) == "Wait")
								continue;
							String[] rd = str.split(" ", -1);
							//去除空项
							rd = removeNull(rd);
							//找出M（）ZXN开头的模块号标志
							for (int j = 0; j < 4; j++) {
								String zxCheck = String.format("M%1$sZXN", j + 1);
								double moduledata = 0.0;
								double tempdata = 0.0;

								if (rd.length >= 2)
									if (rd[2].equals(zxCheck))// 确认是哪个模块的报文并截取出数据
									{
										for (int i = 0; i < 8; i++) {
											int indexofmodule = (i + 1) * 2 + 1;
											if (rd.length > indexofmodule) {
												try{
												//截取出传感器的模数
												moduledata = Double.parseDouble(rd[indexofmodule].substring(3));
												}catch(NumberFormatException ex){
													System.out.println(ex.toString());
													//模数转换失败，估计报文不全，往后的丢掉
													break;
												}
												int indexoftemp = (i + 1) * 2 + 2;
												if (rd.length > indexoftemp) {
													//"---"表示没有温度值
													if (!rd[indexoftemp].substring(3).contains("-")) {
														//截取出模块的温度
														tempdata = Double.parseDouble(rd[indexoftemp].substring(rd[indexoftemp].length() - 3));
													}
													//addData(j + 1, i + 1, moduledata,tempdata);
													
													if (moduledata == 0 || moduledata == 0.1) {
														// 未取得数据
														continue;
													}
													
													int moduleNo = j + 1;
													int pointNo = i + 1;
													//找出对应的模块号、通道号连接的设备信息
													//找到设备型号
													String deviceType="";
													Device device=null;
													for (Device d : deviceList) {
														if((d.getModuleNum() == moduleNo) && (d.getPointNum() == pointNo)){
															deviceType = d.getDevType().getDevTypeName();
															device = d;
															break;
														}
													}
													//根据设备型号做不同的处理
													//根据模块号和通道号存数据
													if(deviceType.contains("混凝土应变计")||deviceType.contains("钢支撑应变计")||deviceType.contains("混凝土应力计")){
														//找到对应的轴力设备
														IStressService stressService=(IStressService) ApplicationUtil.getWebApplicationContext().getBean("stressService");
														Stress stress = stressService.getStressByDevice(device.getDeviceUuid());
														StressMeasureData sData=new StressMeasureData();
														sData.setModValue(moduledata);
														sData.setTemperatureValue(tempdata);
														double CollectModule = Math.sqrt(moduledata * 1000); //采集频率
														double OriginalValue = Math.sqrt(stress.getStartModValue() * 1000); //初始频率
														double calculateValue=(CollectModule * CollectModule - OriginalValue * OriginalValue) * stress.getCalculatedValue();
														sData.setCalculateValue(calculateValue);
														sData.setStressUuid(stress.getStressUuid());
														stressDataList.add(sData);
													}else if(deviceType.contains("锚索计")){
														//找到对应的轴力设备
														ICableMeterService cabelMeterService=(ICableMeterService) ApplicationUtil.getWebApplicationContext().getBean("cableMeterService");
														CableMeter cableMeter = cabelMeterService.getCableMeterByDevice(device.getDeviceUuid());
														CabelMeterMeasureData cData = new CabelMeterMeasureData();
														cData.setCableMeter(cableMeter);
														cData.setTemperatureValue(tempdata);
														cData.setModuleData(moduledata);
														cableDataList.add(cData);
													}else if(deviceType.contains("轴力计")){
														//找到对应的轴力设备
														IStressService stressService=(IStressService) ApplicationUtil.getWebApplicationContext().getBean("stressService");
														Stress stress = stressService.getStressByDevice(device.getDeviceUuid());
														StressMeasureData sData=new StressMeasureData();
														sData.setStressUuid(stress.getStressUuid());
														sData.setTemperatureValue(tempdata);
														double CollectModule = Math.sqrt(moduledata * 1000); //采集频率
														sData.setModValue(moduledata);
														sData.setStressUuid(stress.getStressUuid());
														stressDataList.add(sData);
													}else if(deviceType.contains("水位计")){
														//找到对应的水位设备
														ISurveyPoint_SWService sp_SWService=(ISurveyPoint_SWService) ApplicationUtil.getWebApplicationContext().getBean("sp_SWService");
														SurveyPoint_SW spSW = sp_SWService.getSPSWByDevice(device.getDeviceUuid());
														WaterLevelMeasureData wData = new WaterLevelMeasureData();
														wData.setModuleData(moduledata);
														wData.setTemperatureValue(tempdata);
//														double collectModule = Math.sqrt(moduledata * 1000); //采集频率
//														double originalValue = Math.sqrt(spSW.getOriginalModule() * 1000); //初始频率
														double g = 9.8;//重力加速度
//														double calculateValue = -1 * (spSW.getConstant() * (collectModule - originalValue) * 1000 / g + spSW.getDeep() * 1000);//20160229 by yao&su rui ming
//														calculateValue /=1000;
														//水位公式用模数，不用频率
														double calculateValue = (spSW.getConstant() * (spSW.getOriginalModule() - moduledata) / g - spSW.getDeep()) * 1000;//by 邱北刘 & yao 20170615
														//四舍五入，保留两位小数
														BigDecimal b = new BigDecimal(calculateValue); 
														calculateValue = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
														wData.setCalculateValue(calculateValue);
														wData.setWlSurveyPointUuid(spSW.getSurveyPointUuid());
														waterLevelDataList.add(wData);
													}
												}
											}
										}
									}
							}
						} catch (Exception ex) {
							System.out.print("传感器采集出错，" + ex.toString());
							LogUtil.error("传感器采集出错，"+ ex.toString());
							throw ex;
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("分析字符串出错，return2"+"\r\n");
						LogUtil.error("分析字符串出错，return2");
						return 2;
					}
				}
				// 设置mcu电压
				mcus.get(0).setVoltage(Double.parseDouble(data.substring(data.indexOf("Supply=") + 7,data.indexOf("Supply=") + 7 + 4))); // mcu电压
				System.out.print("采集Pass");
				return 0;
			}
		}
		return 1;//箱号对不上
	}
	private String[]  removeNull(String[] aa){
        List<String> tmp = new ArrayList<String>();
        for(String str:aa){
            if(str!=null && str.length()!=0){
                tmp.add(str);
            }
        }
        aa = tmp.toArray(new String[0]);
        return aa;
	}
	
	@Override
	public void beginCollect() {
		try {
			ApplicationUtil.doConnectAction(mcus.get(0).getNetwork(), new ConnectCallback(mcus.get(0).getNetwork()) {
				int count = 0;
				@Override
				public void onConnected(INonBlockingConnection connection,String flag,boolean b) {
					String writeString = "";
					String msg = "";
					this.setAllowSendTest(false);
					setConnection(connection);
					System.out.println("flag:"+flag+"\r\n");
					//端口打开后会多次进入此回调(数据会分多次返回)
					switch(flag){
					case MCU_OrderCode.CONNECT_END:
						//连接成功后进来，这是还没拿到数据 
						this.setLastActionFlag(flag);
						this.setActionFlag(MCU_OrderCode.MCU_Begin);
						//关闭自报
						setAutoReport(false);
						msg = "mcu" + mcus.get(0).getSn() + "关闭自报" + new SimpleDateFormat("hh:mm:ss").format(new Date());
						System.out.println(msg);
						ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
						break;
					case MCU_OrderCode.MCU_Begin:
						if(validateCommond(connection,b)){
							msg = "mcu" + mcus.get(0).getSn() + "开始召测" + new SimpleDateFormat("hh:mm:ss").format(new Date());
							System.out.println(msg);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.MCU_Read);
							writeString = String.format("MCU32" + " " + mcus.get(0).getSn() + " " + "Begin"+"\r\n");
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), writeString);
							sendCommond(writeString);
							
						}
						break;
					case MCU_OrderCode.MCU_Read:
						if(validateCommond(connection,b)){
							ApplicationUtil.endConnectAction(mcus.get(0).getNetwork());
						}
						break;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void getCallCollectData() {
		try {
			ApplicationUtil.doConnectAction(mcus.get(0).getNetwork(), new ConnectCallback(mcus.get(0).getNetwork()) {
				int count = 0;
				@Override
				public void onConnected(INonBlockingConnection connection,String flag,boolean b) {
					this.setAllowSendTest(false);
					setConnection(connection);
					String writeString = "";
					String msg = "";
					System.out.println("flag:"+flag+"\r\n");
					//端口打开后会多次进入此回调(数据会分多次返回)
					switch(flag){
					case MCU_OrderCode.CONNECT_END:
						//连接成功后进来，这是还没拿到数据 
						this.setLastActionFlag(flag);
						this.setActionFlag(MCU_OrderCode.MCU_Read);
						//关闭自报
						setAutoReport(false);
						msg = "mcu" + mcus.get(0).getSn() + "关闭自报" + new SimpleDateFormat("hh:mm:ss").format(new Date());
						System.out.println(msg);
						ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
						break;
					case MCU_OrderCode.MCU_Read:
						if(validateCommond(connection,b)){
							//连接成功后进来，这是还没拿到数据
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.MCU_Begin);
							msg = "mcu" + mcus.get(0).getSn() + "开始发送读取数据指令" + new SimpleDateFormat("hh:mm:ss").format(new Date());
							System.out.println(msg);
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), msg);
							this.setLastActionFlag(flag);
							this.setActionFlag(MCU_OrderCode.ANALYSE_DATA);
							writeString = String.format("MCU32" + " " + mcus.get(0).getSn() + " " + "Read"+"\r\n");
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), writeString);
							sendCommond(writeString);
						}
						break;
					case MCU_OrderCode.ANALYSE_DATA:
						//开始获取数据(所有数据的其中一次返回的部分)
						String segmentData="";
						//获取一次数据
						segmentData = validateReturnString(connection);//获取返回的数据
						if(segmentData.equals("")){
							break;
						}
						if(segmentData.contains("Wait")){
							setAutoReport(false);
							this.setActionFlag(MCU_OrderCode.MCU_Read);
						}else if(segmentData.contains("No Data Ready")){
							if(count < 1){
								//最多重发1次
								setAutoReport(false);
								count++;
								this.setActionFlag(MCU_OrderCode.MCU_Begin);
							}
						}
						System.out.println("分析的data:"+segmentData+"\r\n");
						if(segmentData.equals("")){
							break;
						}
						// 分析报文,如果数据没有接收完，分析返回2，接收完返回0
						int result = analyseAutoCollectData(segmentData);
						ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), "mcu【"+mcus.get(0).getSn()+"】从设备读取到数据:"+segmentData+new SimpleDateFormat("hh:mm:ss").format(new Date()));
						switch (result) {
						case 1:
						case 2://数据没接收完，继续接收
							this.setActionFlag(MCU_OrderCode.READ_DATA);
							setAutoReport(false);
							break;
						case 0:
							break;
						}
						System.out.println("自报result:" + result);
						//数据接收完毕，入库
						if (result == 0) {
							// 数据全部拿到后，断开连接
							this.setActionFlag(MCU_OrderCode.CONNECT_END);
							isGetDataOver=true;
							ApplicationUtil.endConnectAction(mcus.get(0).getNetwork());
						}
						break;
					}
				}
			});
		}catch(Exception ex){
			
		}
	}

	@Override
	public void testMcu() {
		try {
			ApplicationUtil.doConnectAction(mcus.get(0).getNetwork(), new ConnectCallback(mcus.get(0).getNetwork()) {
				@Override
				public void onConnected(INonBlockingConnection connection,String flag,boolean b) {
					String msg = "";
					this.setAllowSendTest(false);
					setConnection(connection);
					System.out.println("flag:"+flag+"\r\n");
					//端口打开后会多次进入此回调(数据会分多次返回)
					switch(flag){
					case MCU_OrderCode.CONNECT_END:
						//连接成功后进来，这是还没拿到数据 
						this.setLastActionFlag(flag);
						this.setActionFlag(MCU_OrderCode.MCU_Test);
						//关闭自报
						setAutoReport(false);
						
						break;
					case MCU_OrderCode.MCU_Test:
						if(validateCommond(connection,b)){
							isTestOver = true;
							ApplicationUtil.endConnectAction(mcus.get(0).getNetwork());
						}
						break;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public double calZC(List<Stress> stressList, List<StressMeasureData> sDataList) {
		double dataAverage = 0.0;
		double dataSum = 0.0;
		if(stressList.get(0).getDevice().getDevType().getDevTypeName().contains("轴力计")){
			double sumCollectMod = 0.0;
			double sumOriginalMod = 0.0;
			for(int i = 0;i < sDataList.size();i++){
				String stressUuid = sDataList.get(i).getStressUuid();
				List<Stress> stress1 = stressList.stream().filter(p -> p.getStressUuid().equals(stressUuid)).collect(Collectors.toList());
				double OriginalValue = Math.sqrt(stress1.get(0).getStartModValue() * 1000); //初始频率
				sumCollectMod += sDataList.get(i).getModValue();
				sumOriginalMod += OriginalValue;
			}
			//calibrateMod就是K值
			dataSum = (sumCollectMod + sumOriginalMod)*(sumCollectMod - sumOriginalMod)*stressList.get(0).getCalibratedMod();
			dataAverage = dataSum/(sDataList.size()*sDataList.size());
		}else{
			for (StressMeasureData sData : sDataList) {
				dataSum += sData.getCalculateValue();
			}
			dataAverage = dataSum/sDataList.size();
		}
		return dataAverage;
	}
	
	@Override
	public double calMT(List<CabelMeterMeasureData> mtDataList) {
		double dataAverage = 0.0;
		double dataSum = 0.0;
		//这个点下的锚索
		CableMeter cableMeter = mtDataList.get(0).getCableMeter();
		if(cableMeter.getDevice().getDevType().getDevTypeName().contains("锚索计")){
			double sumCollectMod = 0.0;
			double sumOriginalMod = 0.0;
			for(int i = 0;i < mtDataList.size();i++){
				CableMeter cableMeter1 = mtDataList.get(i).getCableMeter();
				double OriginalValue = cableMeter1.getSp_MT().getFrequency();//初始频率
				double collectModule = Math.sqrt(mtDataList.get(i).getModuleData() * 1000); //采集模数转为采集频率
				sumCollectMod += collectModule;
				sumOriginalMod += OriginalValue;
			}
			dataSum = (sumCollectMod + sumOriginalMod)*(sumCollectMod - sumOriginalMod)*cableMeter.getSp_MT().getCalibratedMod();
			dataAverage = dataSum/(mtDataList.size()*mtDataList.size());
		}
		
		return dataAverage;
	}

	@Override
	public List<CxMessureData> getCxDataList() {
		return new ArrayList<>();
	}

	@Override
	public List<CxMessureData> calCX(Clinometer clinometer, List<CxMessureData> cDataList) {
		return new ArrayList<>();
	}

	@Override
	public void setInterval(int interval) {
		
	}

	@Override
	public void init(Calendar firstCollectTime, int interval) {
		modifyRptAndSmpPeriod(firstCollectTime, interval);
	}

	@Override
	public void projectStartCollectByNetwork(Network network, List<Mcu> mcus, Calendar firstGetDataTime, int interval, int connectMcuTime, String uploadStr) throws Exception {
		//读取数据计数，用来算获取数据的时间
				int[] index = new int[]{0};
				//启动定时器，根据间隔打开端口获取数据
				//所有设备采集间隔一样，所以所有设备采集时间也统一设成一样的
				//设置mcu跟获取数据分开处理，是为了统一管理采集时间，因为设置需要耗费时间，不能保证时间一致
				//每个网络连接启动一个定时器,每隔采集间隔时间就打开端口依次接收mcu发回来的数据并存到数据库
				Timer timer = ApplicationUtil.addTimer(network);
				timer.scheduleAtFixedRate(new TimerTask() { //使用scheduleAtFixedRate以保证定时器运行的频率
				IDeviceService deviceService = (IDeviceService) ApplicationUtil.getWebApplicationContext().getBean("deviceService");	
					@Override
					public void run() {
							//异步去读取数据
							//循环所有mcu，每隔一定时间（mcu的通讯时间）读取一个mcu的数据,直到所有mcu循环读取完毕后手动关闭连接
							//读取数据时的时间就是所有mcu的采集时间
							Calendar collectTime=Calendar.getInstance();
							collectTime.setTime(firstGetDataTime.getTime());
							//除了第一次，每取一次数据，采集时间都加上间隔
							collectTime.add(Calendar.SECOND, index[0]*interval*6*10);
							//次数+1
							index[0]++;
							for (int j = 0; j < mcus.size(); j++) {
								ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "mcu【"+mcus.get(j).getSn()+"】开始读取数据！,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
								Device tempD = deviceService.getEntity(mcus.get(j).getDeviceUuid());
								String className = DeviceMapping.operatorMapping.get(tempD.getDeviceModel().getDevModelName());
								IMcuCollectLogic mcuCollectLogic = null;
								try {
									mcuCollectLogic = (IMcuCollectLogic) Class.forName(className).newInstance();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								List<Mcu> sMcus = new ArrayList<>();
								sMcus.add(mcus.get(j));
								mcuCollectLogic.setMcus(sMcus);
								mcuCollectLogic.setUploadFileSrc(uploadFileSrc);
								//得到mcu连着的所有设备
								IDeviceService deviceService = (IDeviceService) ApplicationUtil.getWebApplicationContext().getBean("deviceService");
								List<Device> deviceList=deviceService.getDevicesByMcu(mcus.get(j).getMcuUuid());
								mcuCollectLogic.setDeviceList(deviceList);
								mcuCollectLogic.setInterval(interval);
								//读取数据是异步的，在mcu的连接时间内，无论取没取完数据，都把连接断开以免影响下一个mcu接收数据
								mcuCollectLogic.getAutoReportData();
								try {
									Thread.sleep(connectMcuTime*1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								//如果读取完毕后将数据存到数据库中
								if(mcuCollectLogic.isGetDataOver()){
									ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "mcu【"+mcus.get(j).getSn()+"】从设备读取数据成功,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
									mcuCollectLogic.saveAutoReportData(network,collectTime);
								}else{
									ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(j).getProject().getProjectUuid(), "mcu【"+mcus.get(j).getSn()+"】从设备读取数据失败！（原因：1、网络不通，2、mcu通讯时间太短，3、mcu故障）,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
									LogUtil.info("mcu【"+mcus.get(j).getSn()+"】从设备读取数据失败！（原因：1、网络不通，2、mcu通讯时间太短，3、mcu故障）,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
								}
								//无论数据有没有读到（没有读到就丢弃），mcu连接时间到了后就要关闭连接以准备接受下一个mcu的数据
								ApplicationUtil.endConnectAction(mcus.get(j).getNetwork());
							}
							//回收一下垃圾，并调用cancel，让task结束掉
							//System.gc();
							//this.cancel();
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "网络连接【"+network.getNetworkName()+"】本次读取数据完成************************@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
					}
				}, firstGetDataTime.getTime(),interval*6*10*1000);//mcu设置自报、采集时间以及采集间隔后就会立刻进行采集，再次之前端口要先打开，准备好接收数据
		
	}

	@Override
	public Calendar getNextCollectTime() {
		return null;
	}

	@Override
	public void saveAutoReportData(Network network, Calendar collectTime) {

		//存轴力计数据
		if(getStressDataList().size() > 0){
			IZCService zcService = (IZCService) ApplicationUtil.getWebApplicationContext().getBean("zcService");
			//根据所有轴力计找出这些轴力计对应的测点
			List<SurveyPoint_ZC> sps=new ArrayList<SurveyPoint_ZC>();
			IStressService stressService=(IStressService) ApplicationUtil.getWebApplicationContext().getBean("stressService");
			List<Stress> sList = stressService.getStressByProject(network.getProject().getProjectUuid());
			for(Stress s : sList){
				SurveyPoint_ZC sp=s.getSp_ZC();
				if(!sps.contains(sp)){
					sps.add(sp);
				}
			}
			//存放推送到前台的数据
			List<ZC_Data> dataList =new ArrayList<>();
			//根据测点，算出测点对应的数据，并保存
			for (SurveyPoint_ZC sp : sps) {
				//一个点下的所有轴力计的数据
				List<StressMeasureData> sDataList = new ArrayList<StressMeasureData>();
				for (Stress s : sList) {
					if(s.getSp_ZC().getSurveyPointUuid().equals(sp.getSurveyPointUuid())){
						for (StressMeasureData sData : getStressDataList()) {
							if(sData.getStressUuid().equals(s.getStressUuid())){
								sDataList.add(sData);
							}
						}
					}
				}
				//算出这个点的数据(求平均？)
				double dataAverage = 0.0;
				if(sDataList != null && sDataList.size()>0){
					List<String> tempSp = new ArrayList<>();
					tempSp.add(sp.getSurveyPointUuid());
					//最新一条数据
					List<ZC_Data> latestZCData = zcService.getLatestOneZCDatasBySurveyPoints(tempSp);
					double gapOffset = 0.0;
					double gapChangeRate = 0.0;
					List<Stress> stressList = new ArrayList<>();
					
					for(int i = 0;i < sDataList.size();i++){
						stressList.add(stressService.getEntity(sDataList.get(i).getStressUuid()));
					}
					
					/*******************计算平均值********************/
					dataAverage = calZC(stressList, sDataList);
					
					ZC_Data zcData = new ZC_Data();
					
					if(latestZCData != null && latestZCData.size() > 0){
						gapOffset = dataAverage - latestZCData.get(0).getCalValue();
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(collectTime.getTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestZCData.get(0).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						gapChangeRate = gapOffset/days;
						//保留4位小数
						BigDecimal b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					}
					//保留2位小数
					BigDecimal b = new BigDecimal(dataAverage); 
					dataAverage = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					zcData.setCalValue(dataAverage);
					zcData.setGapOffset(gapOffset);
					//测量时间
					zcData.setCollectTime(collectTime.getTime());
					zcData.setSurveyPoint(sp);
					zcData.setGapChangeRate(gapChangeRate);
					dataList.add(zcData);
					//存数据库
					zcService.saveEntity(zcData);
				}
			}
			//把最新一次轴力数据推往前台
			String msg = JsonUtil.beanToJson(dataList);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuZCStartCollect/"+network.getProject().getProjectUuid(), msg);
			
		}
		if (getCableDataList().size() > 0) {
			IMTService mtService = (IMTService) ApplicationUtil.getWebApplicationContext().getBean("mtService");
			// 锚索
			// 根据所有轴力计找出这些轴力计对应的测点
			List<SurveyPoint_MT> sps = new ArrayList<SurveyPoint_MT>();
			ICableMeterService cableService = (ICableMeterService) ApplicationUtil.getWebApplicationContext().getBean("cableMeterService");
			List<CableMeter> cList = cableService.getCableMeterByProject(network.getProject().getProjectUuid());
			for (CableMeter c : cList) {
				SurveyPoint_MT sp = c.getSp_MT();
				if (!sps.contains(sp)) {
					sps.add(sp);
				}
			}
			//存放推送到前台的数据
			List<MT_Data> dataList =new ArrayList<>();
			//根据测点，算出测点对应的数据，并保存
			for (SurveyPoint_MT sp : sps) {
				//一个点下的所有轴力计的数据
				List<CabelMeterMeasureData> cDataList=new ArrayList<>();
				for (CableMeter c : cList) {
					if(c.getSp_MT().getSurveyPointUuid().equals(sp.getSurveyPointUuid())){
						List<CabelMeterMeasureData> data = getCableDataList();
						for (CabelMeterMeasureData cData : data) {
							if(cData.getCableMeter().getCableMeterUuid().equals(c.getCableMeterUuid())){
								cDataList.add(cData);
							}
						}
					}
				}
				//算出这个点的数据
				double dataAverage = 0.0;
				if(cDataList != null && cDataList.size()>0){
					List<String> tempSp = new ArrayList<>();
					tempSp.add(sp.getSurveyPointUuid());
					//最新一条数据
					List<MT_Data> latestMTData = mtService.getLatestOneMTDatasBySurveyPoints(tempSp);
					double gapOffset = 0.0;
					double gapChangeRate = 0.0;
					//采集模数值，用','分隔
					String moduleData = "";
					for(int i = 0;i < cDataList.size();i++){
						//模数值字符串
						moduleData += Double.toString(cDataList.get(i).getModuleData()) + ',';
					}
					
					/*******************计算平均值********************/
					dataAverage = calMT(cDataList);
					
					MT_Data mtData = new MT_Data();
					if(latestMTData != null && latestMTData.size() > 0){
						gapOffset = dataAverage - latestMTData.get(0).getCalValue();
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(collectTime.getTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestMTData.get(0).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						//保留4位小数
						BigDecimal b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					}
					//保留2位小数
					BigDecimal b = new BigDecimal(dataAverage); 
					dataAverage = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					mtData.setCalValue(dataAverage);
					mtData.setGapOffset(gapOffset);
					//测量时间
					mtData.setCollectTime(collectTime.getTime());
					mtData.setSurveyPoint(sp);
					mtData.setGapChangeRate(gapChangeRate);
					
					LogUtil.info("mt模数：--------"+moduleData);
					
					mtData.setModuleData(moduleData);
					dataList.add(mtData);
					//存数据库
					mtService.saveEntity(mtData);
				}
			}
			//把最新一次轴力数据推往前台
			String msg = JsonUtil.beanToJson(dataList);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuMTStartCollect/"+network.getProject().getProjectUuid(), msg);
		}
		if(getWaterLevelDataList().size() > 0){
			ISWService swService = (ISWService) ApplicationUtil.getWebApplicationContext().getBean("swService");
			//根据所有水位计找出这些水位计对应的测点
			List<SurveyPoint_SW> sps=new ArrayList<>();
			ISurveyPoint_SWService sp_SWService=(ISurveyPoint_SWService) ApplicationUtil.getWebApplicationContext().getBean("sp_SWService");
			List<SurveyPoint_SW> wList = sp_SWService.getSP_SWsByProject(network.getProject().getProjectUuid());
			for(SurveyPoint_SW s : wList){
				if(!sps.contains(s)){
					sps.add(s);
				}
			}
			//根据测点，算出测点对应的数据，并保存
			List<WaterLevelMeasureData> waterDataList = getWaterLevelDataList();
			if(waterDataList != null && waterDataList.size() > 0){
				//存放推往前台的数据
				List<SW_Data> dataList = new ArrayList<>();
				for (SurveyPoint_SW sp : sps) {
					//一个点下的水位计的数据
					WaterLevelMeasureData waterLevelData = new WaterLevelMeasureData();
					for (WaterLevelMeasureData wData : waterDataList) {
						if(wData.getWlSurveyPointUuid().equals(sp.getSurveyPointUuid())){
							waterLevelData = wData;
						}
					}
					if(waterLevelData.getCalculateValue() == null){
						//循环完没有匹配到点，这个点没采到数据
						continue;
					}
					SW_Data swData=new SW_Data();
					swData.setCalValue(waterLevelData.getCalculateValue());
					//采集模数值
					swData.setModuleData(waterLevelData.getModuleData());
					//测量时间
					swData.setCollectTime(collectTime.getTime());
					swData.setSurveyPoint(sp);
					
					//计算单次变化量和累计变化量，并推到前台
					
					List<String> tempSp = new ArrayList<>();
					tempSp.add(sp.getSurveyPointUuid());
					//最新一条数据
					List<SW_Data> latestSWData = swService.getLatestOneSWDatasBySurveyPoints(tempSp);
					//第一个时间点的数据
					List<SW_Data> firstSWData = swService.getFirstOneSWDatasBySurveyPoints(tempSp);
					double gapOffset = 0.0;
					double gapChangeRate = 0.0;
					//计算累计位移
					//只在有数据的情况下才进行计算
					// 水位计算值单次位移
					if(latestSWData != null && latestSWData.size() > 0){
						gapOffset = swData.getCalValue()- latestSWData.get(0).getCalValue();
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(swData.getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestSWData.get(0).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					}
					//保留2位小数
					BigDecimal b = new BigDecimal(gapOffset); 
					gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					// 水位计算值累计位移
					double accumOffset = 0.0;
					if (firstSWData !=  null && firstSWData.size() > 0) {
						accumOffset= latestSWData.get(0).getCalValue()- firstSWData.get(0).getCalValue() + sp.getOriginalTotalValue();
					}
					b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					swData.setAccumOffset(accumOffset);
					swData.setGapOffset(gapOffset);
					swData.setGapChangeRate(gapChangeRate);
					dataList.add(swData);
					
					//存数据库
					swService.saveEntity(swData);
				}
				//把最新一次数据推往前台
				String msg = JsonUtil.beanToJson(dataList);
//				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuSWStartCollect/"+network.getProject().getProjectUuid(), msg);
			} else {
				String msg="水位测点数据列表为空！";
				System.out.println(msg+"\r\n");
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
			}
		} if(getCxDataList().size() > 0) {
			ICXService cxService = (ICXService) ApplicationUtil.getWebApplicationContext().getBean("cxService");
			//存放推送到前台的数据
			double gap = 0.0;
			double accum = 0.0;
			double rate = 0.0;
			//一个点下的所有测斜的数据
			List<CxMessureData> cDataList = getCxDataList();
			Clinometer clinometer = cDataList.get(0).getClinometer();
			cDataList = calCX(clinometer, cDataList);
			List<CX_Data> lastAllCxData = cxService.getOnePointLatestOneCXDataBySurveyPoint(clinometer.getSp_CX().getSurveyPointUuid());
			List<String> temp = new ArrayList<>();
			temp.add(clinometer.getSp_CX().getSurveyPointUuid());
			//头三条数据
			List<CX_Data> first3CxData = cxService.getThreeAscCXDataBySurveyPoint(temp);
			//头三条平均数据
			List<CX_Data> firstAveCxData = new ArrayList<>();
			if(first3CxData != null && first3CxData.size() > 0){
				List<Double> depths = new ArrayList<>();
				first3CxData.stream().forEach(p -> depths.add(p.getDepth()));
				HashSet h = new HashSet(depths);   
				depths.clear();   
				//去除depths重复的值
				depths.addAll(h);  
				for(int kk = 0; kk < depths.size(); kk++){
					double d = depths.get(kk);
					List<CX_Data> dataAtOneDepth = first3CxData.stream().filter(p -> p.getDepth() == d).collect(Collectors.toList());
					double aveCal = 0.0;
					for(CX_Data cd : dataAtOneDepth){
						aveCal += cd.getCalValue();
					}
					dataAtOneDepth.get(0).setCalValue(aveCal);
					firstAveCxData.add(dataAtOneDepth.get(0));
				}
			}
			CX_Data cxData = new CX_Data();
			for(int k = 0; k < cDataList.size(); k++){
				cxData.setDepth((k+1)*(Double.parseDouble(clinometer.getGap())));
				cxData.setCalValue(cDataList.get(k).getCalculateValue());
				cxData.setCollectTime(collectTime.getTime());
				cxData.setSurveyPoint(clinometer.getSp_CX());
				double depth = cxData.getDepth();
				//测量次序
				if(lastAllCxData != null && lastAllCxData.size() > 0){
					cxData.setSurveyOrder(lastAllCxData.get(0).getSurveyOrder() + 1);
					List<CX_Data> dataAtDepth = lastAllCxData.stream().filter(p -> p.getDepth() == depth).collect(Collectors.toList());
					if(dataAtDepth.size() > 0){
						gap = cxData.getCalValue() - dataAtDepth.get(0).getCalValue();
						
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cxData.getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dataAtDepth.get(0).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days++;
						}
						rate = Math.abs(gap / days);
						BigDecimal b = new BigDecimal(rate); 
						rate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						b = new BigDecimal(gap); 
						gap = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					}
				}else{
					cxData.setSurveyOrder(1);
				}
				
				if(firstAveCxData.size() > 0){
					List<CX_Data> firstDataAtDepth = firstAveCxData.stream().filter(p -> p.getDepth() == depth).collect(Collectors.toList());
					if(firstDataAtDepth.size() > 0){
						accum = cxData.getCalValue() - firstDataAtDepth.get(0).getCalValue();
						BigDecimal b = new BigDecimal(accum); 
						accum = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					}
				}
				cxData.setGapOffset(gap);
				cxData.setAccumOffset(accum);
				cxData.setChangeRate(rate);
				cxService.saveEntity(cxData);
			}
		}
		
		
		
		//发短信给项目监测人员和监测负责人。
		String phones = "";
		String contacts = network.getProject().getMonitorWorker();
		contacts += network.getProject().getMonitorLeader();
		while(contacts.contains("(")||contacts.contains(")")){
			String temp = contacts.substring(contacts.indexOf("(")+1,contacts.indexOf(")"));
			if(!phones.contains(temp)){
				phones += temp+",";
			}
			contacts = contacts.substring(contacts.indexOf(")")+1);
		}
		IWarnningDataService warningDataService = (IWarnningDataService) ApplicationUtil.getWebApplicationContext().getBean("warnningDataService");
		//计算是否超限并更新工程状态。
		warningDataService.calWarnningOffsetByMonitorItem("SW", network.getProject(),phones);
		warningDataService.calWarnningOffsetByMonitorItem("ZC", network.getProject(),phones);
		warningDataService.calWarnningOffsetByMonitorItem("MT", network.getProject(),phones);
		warningDataService.calWarnningOffsetByMonitorItem("CX", network.getProject(),phones);

		
	}



}
