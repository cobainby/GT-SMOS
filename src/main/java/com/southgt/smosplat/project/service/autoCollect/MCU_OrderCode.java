package com.southgt.smosplat.project.service.autoCollect;
/**
 * 
 * mcu采集步骤
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author Administrator
 * <p>Modification History:</p>
 * <p> Date         Author   姚家俊   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年11月22日     Administrator       v1.0.0        create</p>
 *
 */
public class MCU_OrderCode {
	/**
	 * 连接完成
	 */
	public final static String CONNECT_END="connect_end";
	/**
	 * 读取数据
	 */
	public final static String READ_DATA="readData";
	/**
	 * 分析数据
	 */
	public final static String ANALYSE_DATA="analyseData";
	/**
	 * 
	 * 采集完成
	 */
	public final static String DONE="done";
	/**
	 * 设置自报功能开关
	 */
	public final static String SET_AUTO_REPORT="setAutoReport";
	/**
	 * 时间对准
	 */
	public final static String SET_TIME="setTime";
	/**
	 * 修改采样起点时间
	 */
	public final static String SET_COLLECT_START_TIME="setCollectStartTime";
	/**
	 * 设置自报周期
	 */
	public final static String SET_REPORT_PERIOD="setReportPeriod";
	/**
	 * 设置采样周期
	 */
	public final static String SET_COLLECT_PERIOD="setCollectPeriod";
	/**
	 * MCU采集数据
	 */
	public final static String MCU_COLLECTED_DATA="mcuCollectData";
	/**
	 * mcu开始测量（召测）
	 */
	public final static String MCU_Begin = "mcuBegin";
	/**
	 * mcu读取数据（召测）
	 */
	public final static String MCU_Read = "mcuRead";
	/**
	 * mcu测试。发一条关闭自报的指令
	 */
	public final static String MCU_Test = "mcuTest";
}
