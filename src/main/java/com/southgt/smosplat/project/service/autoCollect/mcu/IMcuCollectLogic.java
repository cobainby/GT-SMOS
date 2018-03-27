package com.southgt.smosplat.project.service.autoCollect.mcu;

import java.util.Calendar;
import java.util.List;

import org.xsocket.connection.INonBlockingConnection;

import com.southgt.smosplat.data.vo.CabelMeterMeasureData;
import com.southgt.smosplat.data.vo.CxMessureData;
import com.southgt.smosplat.data.vo.StressMeasureData;
import com.southgt.smosplat.project.entity.Clinometer;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Stress;

/**
 * 
 * mcu采集接口
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author Administrator
 * <p>Modification History:</p>
 * <p> Date         Author   姚家俊   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月9日     Administrator       v1.0.0        create</p>
 *
 */
public interface IMcuCollectLogic extends IAbstract_McuCollectLogic{

	/**
	 * 设置自报模式
	 * @date  2017年4月24日 下午6:13:57
	 * @return void
	 * @param a
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月24日     mohaolin      v1.0          create</p>
	 *
	 */
	void setAutoReport(boolean a);
	/**
	 * 
	 * 初始化mcu
	 * @date  2018年1月22日 下午3:45:23
	 * 
	 * @param firstCollectTime
	 * @param interval
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月22日     姚家俊      v1.0          create</p>
	 *
	 */
	void init(Calendar firstCollectTime,int interval);
	
	/**
	 * 发送命令
	 * @date  2017年4月24日 下午6:13:04
	 * @param commond
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月24日     mohaolin      v1.0          create</p>
	 *
	 */
	void sendCommond(String commond);
	/**
	 * 验证发送指令是否成功（通过返回的数据是否有OK）
	 * @date  2016年11月24日 下午3:57:35
	 * 
	 * @param @param connection
	 * @param @param b 全站仪自动采集用到，如果是true，需要发送上一条指令，如果是false，发送下一条
	 * @param @return
	 * @return boolean
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  Administrator
	 * <p>Modification History:</p>
	 * <p>Date         Author  姚家俊    Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年11月24日     Administrator      v1.0          create</p>
	 *
	 */
	public boolean validateCommond(INonBlockingConnection connection,boolean b);
	/**
	 * 读取mcu自报发送的数据
	 * @date  2017年4月27日 下午2:04:53
	 * @return void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月27日     mohaolin      v1.0          create</p>
	 *
	 */
	void getAutoReportData();
	
	/**
	 * 
	 * 计算轴力支撑
	 * @date  2018年1月12日 下午1:05:39
	 * 
	 * @return
	 * double
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月12日     姚家俊      v1.0          create</p>
	 *
	 */
	public double calZC(List<Stress> stressList, List<StressMeasureData> sDataList);
	
	/**
	 * 
	 * 计算测斜 
	 * @date  2018年1月15日 上午9:28:56
	 * 
	 * @param clinometer
	 * @param sDataList
	 * @return
	 * List<CxMessureData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月15日     姚家俊      v1.0          create</p>
	 *
	 */
	public List<CxMessureData> calCX(Clinometer clinometer, List<CxMessureData> cDataList);
	/**
	 * 
	 * 计算锚索
	 * @date  2018年1月12日 下午1:36:16
	 * 
	 * @param stressList
	 * @param sDataList
	 * @return
	 * double
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月12日     姚家俊      v1.0          create</p>
	 *
	 */
	public double calMT(List<CabelMeterMeasureData> mtDataList);
	/**
	 * 分析自报的数据，看看有没有接收完成 
	 * @date  2017年4月27日 下午3:02:00
	 * @return int
	 * @param data
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月27日     mohaolin      v1.0          create</p>
	 *
	 */
	int analyseAutoCollectData(String data);
	/**
	 * 
	 * 开始采集
	 * @date  2017年6月13日 下午3:18:04
	 * 
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月13日     姚家俊      v1.0          create</p>
	 *
	 */
	void beginCollect();
	/**
	 * 
	 * 获取召测数据
	 * @date  2017年7月6日 上午8:54:05
	 * 
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月6日     姚家俊      v1.0          create</p>
	 *
	 */
	void getCallCollectData();
	/**
	 * 
	 * 测试mcu
	 * @date  2017年7月6日 上午8:54:05
	 * 
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月6日     姚家俊      v1.0          create</p>
	 *
	 */
	void testMcu();
	/**
	 * 
	 * @param uploadFileSrc
	 * 设置上传路径
	 */
	
	public void projectStartCollectByNetwork(Network network, List<Mcu> mcus,Calendar firstGetDataTime, int interval,int connectMcuTime, String uploadStr) throws Exception;
	
	/**
	 * 
	 * 保存数据
	 * @date  2018年1月30日 下午6:45:32
	 * 
	 * @param network
	 * @param collectTime
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月30日     姚家俊      v1.0          create</p>
	 *
	 */
	public void saveAutoReportData(Network network,Calendar collectTime);
}
