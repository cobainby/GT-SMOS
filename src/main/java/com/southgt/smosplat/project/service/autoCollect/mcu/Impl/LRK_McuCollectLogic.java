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
import java.util.stream.Collectors;

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
import com.southgt.smosplat.project.service.IClinometerService;
import com.southgt.smosplat.project.service.IStressService;
import com.southgt.smosplat.project.service.ISurveyPoint_SWService;
import com.southgt.smosplat.project.service.IWarnningDataService;
import com.southgt.smosplat.project.service.autoCollect.ConnectCallback;
import com.southgt.smosplat.project.service.autoCollect.MCU_OrderCode;
import com.southgt.smosplat.project.service.autoCollect.mcu.IMcuCollectLogic;

/**
 * 
 * 联睿科模块采集逻辑
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年1月12日     姚家俊       v1.0.0        create</p>
 *
 */
public class LRK_McuCollectLogic extends Abstract_McuCollectLogic implements IMcuCollectLogic {
	
	//第一个mcu的sn
	String firstMcuSn = "";

	private void modifyRptAndSmpPeriod(Calendar firstCollectTime, int interval) {
		try {
			ApplicationUtil.doConnectAction(mcus.get(0).getNetwork(), new ConnectCallback(mcus.get(0).getNetwork()) {
				@Override
				public void onConnected(INonBlockingConnection connection,String flag,boolean b) {
					//保存connection，用于下一次发送指令
					setConnection(connection);
					//不允许发送test指令
					this.setAllowSendTest(false);
					String segmentData = validateReturnString(connection);
					if(segmentData.contains("$LRKKJ$")){
						if(segmentData.lastIndexOf("END") == segmentData.length()-3){
							// 默认返回原始值，测斜波特率用9600；初始值为0表示不需要初始值
							String command = String.format(
									"$LRKKJ$;ZX_HC:%1$s;CX_LX:%2$s;CX_K:%3$s;CX_CS:%4$s;CX_BT:%5$s;ZX_LX1:%6$s;ZX_LX2:%7$s;ZX_LX3:%8$s;ZX_LX4:%9$s;ZX_K1:%10$s;ZX_K2:%11$s;ZX_K3:%12$s;ZX_K4:%13$s;ZX_CS1:%14$s;ZX_CS2:%15$s;ZX_CS3:%16$s;ZX_CS4:%17$s;END",
									interval, "0", "0", "0", "1", "5", "5", "5", "5", "0", "0", "0", "0", "0", "0", "0", "0");
							sendCommond(command);
							
							mcu_firstCollectTime = Calendar.getInstance();
							mcu_firstCollectTime.add(Calendar.SECOND, interval * 60);
							//提前30秒打开端口
							mcu_firstCollectTime.add(Calendar.SECOND, -30);
							command += "@";
							command += new SimpleDateFormat("hh:mm:ss").format(new Date());
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), command);
							isSetOver = true;
							ApplicationUtil.endConnectAction(mcus.get(0).getNetwork());
							
							//得到mcu连着的所有设备
							IDeviceService deviceService = (IDeviceService) ApplicationUtil.getWebApplicationContext().getBean("deviceService");
							List<Device> deviceList=deviceService.getDevicesByMcu(mcus.get(0).getMcuUuid());
							setDeviceList(deviceList);
							int result = analyseAutoCollectData(segmentData);
							switch (result) {
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
							LogUtil.info(mcus.get(0).getSn()+"自报result:" + result);
							//数据接收完毕，入库
							if (result == 0) {
								// 数据全部拿到后，断开连接
								this.setActionFlag(MCU_OrderCode.CONNECT_END);
								isGetDataOver = true;
							}
						}
					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
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
	}

	@Override
	public void setAutoReport(boolean a) {
		//联睿科默认自报模式，不能关闭。
		
	}

	@Override
	public boolean validateCommond(INonBlockingConnection connection, boolean b) {
		// b是重发上一条指令的标识
		if (b) {
			return false;
		}
		System.out.println("收到数据" + "\r\n");
		String data = "";
		try {
			int d = connection.available();
			System.out.println("数据长度：" + d);
			if (d > 0) {
				ByteBuffer[] buffer = connection.readByteBufferByLength(d);
				for (int i = 0; i < buffer.length; i++) {
					data += byteBufferToString(buffer[i]);
				}
				System.out.println("received:" + new Date().toString() + data + "\r\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(data.contains("$LRKKJ$")){
			if(data.lastIndexOf("END")==data.length()-3){
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
	
	/**
	 * 解析每一段数据，将其整到一起 
	 */
	public String validateReturnString(INonBlockingConnection connection) {
		String result = "";
		try {
			int d = connection.available();
			System.out.println("数据长度：" + d + "\r\n");
			if (d > 0) {
				ByteBuffer[] buffer = connection.readByteBufferByLength(d);
				for (int i = 0; i < buffer.length; i++) {
					String temp = byteBufferToString(buffer[i]);
					System.out.println(buffer[i].toString() + "\r\n");
					data += temp;
				}
				System.out.println("result:" + result + "/////" + new Date() + "\r\n");
				if (data.lastIndexOf("END") == data.length() - 3) {
					result = data;
					data = "";
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	private void initial(){
		this.cableDataList = new ArrayList<>();
		this.stressDataList = new ArrayList<>();
		this.waterLevelDataList = new ArrayList<>();
		this.cxDataList = new ArrayList<>();
//		this.deviceList = new ArrayList<>();
//		this.isGetDataOver = false;
//		this.isSetOver = false;
//		this.mcus = new ArrayList<>();
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
						
						if(segmentData.contains("END")){//数据帧
							//每次收到数据后5s内要配置模块，否则端口关闭，并且回数据周期变成一小时一次。
							String command = String.format(
									"$LRKKJ$;ZX_HC:%1$s;CX_LX:%2$s;CX_K:%3$s;CX_CS:%4$s;CX_BT:%5$s;ZX_LX1:%6$s;ZX_LX2:%7$s;ZX_LX3:%8$s;ZX_LX4:%9$s;ZX_K1:%10$s;ZX_K2:%11$s;ZX_K3:%12$s;ZX_K4:%13$s;ZX_CS1:%14$s;ZX_CS2:%15$s;ZX_CS3:%16$s;ZX_CS4:%17$s;END",
									interval, "0", "0", "0", "1", "5", "5", "5", "5", "0", "0", "0", "0", "0", "0", "0", "0");
							sendCommond(command);
							command += "@";
							command += new SimpleDateFormat("hh:mm:ss").format(new Date());
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), command);
							String[] portions = segmentData.split(";");
							//箱号
							String sn = portions[2].split(":")[1];
							if(firstMcuSn.equals("")){
								firstMcuSn = sn;
								nextCollectTime = Calendar.getInstance();
							}else if(firstMcuSn.equals(sn)){
								nextCollectTime.setTime(new Date());
							}
						}
						
						System.out.println("分析的data:"+segmentData+"\r\n");
						LogUtil.info(mcus.get(0).getSn()+"分析的data:"+segmentData);
						if(segmentData.equals("")){
							break;
						}
						initial();
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
							/*-------------入库------------*/
							saveAutoReportData(mcus.get(0).getNetwork(), Calendar.getInstance());
							// 数据全部拿到后，断开连接
							this.setActionFlag(MCU_OrderCode.CONNECT_END);
							isGetDataOver = true;
						}
						break;
					}
				}
			});
		} catch (Exception e) {
			LogUtil.error(e.toString());
		}
	}

	@Override
	public int analyseAutoCollectData(String data) {
		String[] dataArray = data.split("\r\n", -1);
		//最新的数据（预防一下发几条数据过来）
		String msgData = "";
		List<Mcu> thisMcu = new ArrayList<>();
		try{
			for (int k = 0; k < dataArray.length; k++) {
				if(dataArray[k].contains("$LRKKJ$") && dataArray[k].contains("END")){//有头有尾，这是完整的一怔数据
					msgData = dataArray[k];
					String[] portions = msgData.split(";");
					//箱号
					String sn = portions[2].split(":")[1];
//					if(!sn.equals(mcus.get(0).getSn())){
//						//不是这个mcu的报文
//						msgData = "";
//					}else{
//						//箱号开始的index，截出需要的数据
//						int a = data.indexOf(mcus.get(0).getSn());
//						//找到对应的报文的开头
//						String b = data.substring(a - 32, data.length());
//						//找到对应报文的结尾
//						int bb = b.indexOf("END");
//						int aa = b.indexOf(mcus.get(0).getSn());
//						//截取对应的完整报文
//						msgData = b.substring(aa - 32, bb + 3);
//						break;
//					}
					//这个报文对应的mcu
					thisMcu = mcus.stream().filter(p -> p.getSn().equals(sn)).collect(Collectors.toList());
				}else{
					continue;
				}
			}
			if(msgData != "" && msgData.contains("$LRKKJ$") && thisMcu.size() > 0){
				String[] portions = msgData.split(";");
				//找出对应的模块号、通道号连接的设备信息
				for(int i = 0; i < 5; i ++){
					//找到设备型号
					String deviceType="";
					Device device=null;
					String mcuUuid = thisMcu.get(0).getMcuUuid();
					//这个mcu下面的设备列表
					List<Device> thisDeviceList = deviceList.stream().filter(p -> p.getMcu().getMcuUuid().equals(mcuUuid)).collect(Collectors.toList());
					for (Device d : thisDeviceList) {
						//默认模块号是1；通道号1-5；
						if(d.getPointNum() == i+1){
							deviceType = d.getDevType().getDevTypeName();
							device = d;
							break;
						}
					}
					double hzData = 0.0;
					String cxDataStr = "";
					if(i < 4){
						//频率读数未取得数据
						if(portions[2*i + 8].split(":")[1].equals("0.000")){
							continue;
						}
						hzData = Double.parseDouble(portions[2*i + 8].split(":")[1]);
					}else if(i == 4){
						if(portions[16].split(":")[1].equals("0.000")){
							continue;
						}
						//测斜
						cxDataStr = portions[16].split(":")[1];
					}
					//根据设备型号做不同的处理
					//根据模块号和通道号存数据
					if(deviceType.contains("混凝土应变计")||deviceType.contains("钢支撑应变计")||deviceType.contains("混凝土应力计")){
						//找到对应的轴力设备
						IStressService stressService=(IStressService) ApplicationUtil.getWebApplicationContext().getBean("stressService");
						Stress stress = stressService.getStressByDevice(device.getDeviceUuid());
						StressMeasureData sData = new StressMeasureData();
						sData.setModValue(hzData);
//						sData.setTemperatureValue(tempdata);
						double CollectModule = hzData; //采集频率
						double OriginalValue = stress.getStartModValue(); //初始频率
						double calculateValue=(CollectModule * CollectModule - OriginalValue * OriginalValue) * stress.getCalibratedMod();
						sData.setCalculateValue(calculateValue);
						sData.setStressUuid(stress.getStressUuid());
						stressDataList.add(sData);
					}else if(deviceType.contains("锚索计")){
						//找到对应的轴力设备
						ICableMeterService cabelMeterService=(ICableMeterService) ApplicationUtil.getWebApplicationContext().getBean("cableMeterService");
						CableMeter cableMeter = cabelMeterService.getCableMeterByDevice(device.getDeviceUuid());
						CabelMeterMeasureData cData = new CabelMeterMeasureData();
						cData.setCableMeter(cableMeter);
//						cData.setTemperatureValue(tempdata);
						cData.setModuleData(hzData);
						cableDataList.add(cData);
					}else if(deviceType.contains("轴力计")){
						//找到对应的轴力设备
						IStressService stressService=(IStressService) ApplicationUtil.getWebApplicationContext().getBean("stressService");
						Stress stress = stressService.getStressByDevice(device.getDeviceUuid());
						StressMeasureData sData=new StressMeasureData();
						sData.setStressUuid(stress.getStressUuid());
//						sData.setTemperatureValue(tempdata);
						sData.setModValue(hzData);//采集频率
						sData.setStressUuid(stress.getStressUuid());
						stressDataList.add(sData);
					}else if(deviceType.contains("水位计")){
						//找到对应的水位设备
						ISurveyPoint_SWService sp_SWService = (ISurveyPoint_SWService) ApplicationUtil.getWebApplicationContext().getBean("sp_SWService");
						SurveyPoint_SW spSW = sp_SWService.getSPSWByDevice(device.getDeviceUuid());
						WaterLevelMeasureData wData = new WaterLevelMeasureData();
						wData.setModuleData(hzData);
//						wData.setTemperatureValue(tempdata);
//						double collectModule = Math.sqrt(hzData * 1000); //采集频率
//						double originalValue = Math.sqrt(spSW.getOriginalModule() * 1000); //初始频率
						double g = 9.8;//重力加速度
//						double calculateValue = -1 * (spSW.getConstant() * (collectModule - originalValue) * 1000 / g + spSW.getDeep() * 1000);//20160229 by yao&su rui ming
//						calculateValue /=1000;
						double calculateValue = (spSW.getConstant() * (spSW.getOriginalModule() - hzData) / g - spSW.getDeep()) * 1000;//by 邱北刘 & yao 20170615
						//四舍五入，保留两位小数
						BigDecimal b = new BigDecimal(calculateValue); 
						calculateValue = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						wData.setCalculateValue(calculateValue);
						wData.setWlSurveyPointUuid(spSW.getSurveyPointUuid());
						waterLevelDataList.add(wData);
					}else if(deviceType.contains("测斜")){//选择1是0.5m，选择2是1m，出来的值都是累计值，可以直接展示；选择0是角度计算值。
						//找到对应的测斜设备
						IClinometerService clinometerService=(IClinometerService) ApplicationUtil.getWebApplicationContext().getBean("clinometerService");
						//根据深度？？？？？？？？？？？？？？？
						Clinometer clinometer = clinometerService.getClinometerByDevice(device.getDeviceUuid());
						String[] cxData = cxDataStr.split(",");
						for(int m = 0; m < cxData.length; m++){
							CxMessureData cData = new CxMessureData();
							cData.setHzData(Double.parseDouble(cxData[m]));
							cData.setClinometer(clinometer);
							cxDataList.add(cData);
						}

					}
				}
			}else{
				//报文为空/不全
				return 1;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("分析字符串出错，return2"+"\r\n");
			LogUtil.error("分析字符串出错，return2");
			return 2;
		}
		//正常
		return 0;
	}


	@Override
	public void beginCollect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCallCollectData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testMcu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double calZC(List<Stress> stressList, List<StressMeasureData> sDataList) {
		double dataAverage = 0.0;
		List<Double> calList = new ArrayList<>();
		for(int i = 0; i < sDataList.size(); i++){
			calList.add(stressList.get(i).getCalibratedMod() * sDataList.get(i).getCalculateValue());
		}
		//小于2就不用算了
		if(calList.size() > 2){
			double DATA1 = (calList.get(0) + calList.get(2))/2;
			double DATA2 = 0.0;
			if(calList.size() > 3){
				DATA2 = (calList.get(1) + calList.get(3))/2;
				dataAverage = (DATA1 + DATA2)/2;
			}else{
				dataAverage = DATA1;
			}
		}
		return dataAverage;
	}

	@Override
	public double calMT(List<CabelMeterMeasureData> mtDataList) {
		double dataAverage = 0.0;
		double dataSum = 0.0;
		// 这个点下的锚索
		CableMeter cableMeter = mtDataList.get(0).getCableMeter();
		if (cableMeter.getDevice().getDevType().getDevTypeName().contains("锚索计")) {
			double sumCollectMod = 0.0;
			double sumOriginalMod = 0.0;
			for (int i = 0; i < mtDataList.size(); i++) {
				CableMeter cableMeter1 = mtDataList.get(i).getCableMeter();
				double OriginalValue = cableMeter1.getSp_MT().getFrequency();// 初始频率
				double collectModule = mtDataList.get(i).getModuleData(); // 采集模数转为采集频率
				sumCollectMod += collectModule;
				sumOriginalMod += OriginalValue;
			}
			dataSum = (sumCollectMod + sumOriginalMod)*(sumCollectMod - sumOriginalMod)*cableMeter.getSp_MT().getCalibratedMod();
			dataAverage = dataSum/(mtDataList.size()*mtDataList.size());
		}
		
		return dataAverage;
	}

	@Override
	public List<CxMessureData> calCX(Clinometer clinometer, List<CxMessureData> cDataList) {
		for (CxMessureData c : cDataList) {
			c.setCalculateValue(c.getHzData()*clinometer.getCalibratedMod());
		}
		return cDataList;
	}

	@Override
	public void init(Calendar firstCollectTime, int interval) {
		modifyRptAndSmpPeriod(firstCollectTime, interval);
		
	}

	@Override
	public void projectStartCollectByNetwork(Network network, List<Mcu> mcus, Calendar firstGetDataTime, int interval, int connectMcuTime, String uploadStr) throws Exception {
		//读取数据计数，用来算获取数据的时间
		if(mcus.size() == 0){
			return;
		}
		setMcus(mcus);
		//异步去读取数据
		//循环所有mcu，每隔一定时间（mcu的通讯时间）读取一个mcu的数据,直到所有mcu循环读取完毕后手动关闭连接
		//读取数据时的时间就是所有mcu的采集时间
		Calendar collectTime=Calendar.getInstance();
		collectTime.setTime(firstGetDataTime.getTime());
		//除了第一次，每取一次数据，采集时间都加上间隔
		collectTime.add(Calendar.SECOND, interval*6*10);
		//次数+1
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "mcu【"+mcus.get(0).getSn()+"】开始读取数据！,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));

			setUploadFileSrc(uploadStr);
			//得到mcu连着的所有设备
			IDeviceService deviceService = (IDeviceService) ApplicationUtil.getWebApplicationContext().getBean("deviceService");
			List<Device> wholeDeviceList = new ArrayList<>();
			for(int k = 0; k< mcus.size(); k++){
				List<Device> deviceList = deviceService.getDevicesByMcu(mcus.get(k).getMcuUuid());
				wholeDeviceList.addAll(deviceList);
			}
			//这个网络下的所有设备
			setDeviceList(wholeDeviceList);
			setInterval(interval);
			//读取数据是异步的，在mcu的连接时间内，无论取没取完数据，都把连接断开以免影响下一个mcu接收数据
			getAutoReportData();
			try {
				Thread.sleep(connectMcuTime*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//如果读取完毕后将数据存到数据库中
			if(isGetDataOver()){
				
			}else{
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(0).getProject().getProjectUuid(), "mcu【"+mcus.get(0).getSn()+"】从设备读取数据失败！（原因：1、网络不通，2、mcu通讯时间太短，3、mcu故障）,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
				LogUtil.info("mcu【"+mcus.get(0).getSn()+"】从设备读取数据失败！（原因：1、网络不通，2、mcu通讯时间太短，3、mcu故障）,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
			}
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "网络连接【"+network.getNetworkName()+"】本次读取数据完成************************@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
	}

	@Override
	public void saveAutoReportData(Network network, Calendar collectTime) {
		//存数据到数据库，并发送通知到前台
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
//						ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
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
