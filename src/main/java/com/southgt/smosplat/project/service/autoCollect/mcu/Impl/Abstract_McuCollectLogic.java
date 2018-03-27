package com.southgt.smosplat.project.service.autoCollect.mcu.Impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xsocket.connection.INonBlockingConnection;

import com.southgt.smosplat.data.vo.CabelMeterMeasureData;
import com.southgt.smosplat.data.vo.CxMessureData;
import com.southgt.smosplat.data.vo.StressMeasureData;
import com.southgt.smosplat.data.vo.WaterLevelMeasureData;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.service.autoCollect.mcu.IAbstract_McuCollectLogic;

/**
 * 
 * mcu采集抽象类
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年1月31日     姚家俊       v1.0.0        create</p>
 *
 */
public abstract class Abstract_McuCollectLogic implements IAbstract_McuCollectLogic{
	
	/**
	 * mcu实体
	 */
	protected List<Mcu> mcus = new ArrayList<>();
	
	/**
	 * mcu报文源文件上传地址
	 */
	protected String uploadFileSrc;
	
	/**
	 * 第一次设置是否完毕
	 */
	protected boolean isSetOver=false;
	
	/**
	 * 是否读取mcu数据完毕
	 */
	protected boolean isGetDataOver=false;
	
	/**
	 * 测试mcu是否成功
	 */
	protected boolean isTestOver=false;
	
	/**
	 * mcu采集到的总数据，因为数据会分多次返回，所以需要这个变量
	 */
	protected String data="";
	
	/**
	 * 采集周期
	 */
	protected int interval;
	
	/**
	 * 这个mcu所连接的设备列表
	 */
	protected List<Device> deviceList;
	
	/**
	 * 这个mcu所连接的水位数据列表
	 */
	protected List<WaterLevelMeasureData> waterLevelDataList = new ArrayList<>();;
	
	/**
	 * 临时存放每个轴力计设备测到的数据
	 */
	protected List<StressMeasureData> stressDataList = new ArrayList<>();
	
	/**
	 * 临时存放每个锚索计设备测到的数据
	 */
	protected List<CabelMeterMeasureData> cableDataList = new ArrayList<>();
	
	/**
	 * 临时存放每个测斜仪设备测到的数据
	 */
	protected List<CxMessureData> cxDataList = new ArrayList<>();
	/**
	 * 通讯接口
	 */
	protected INonBlockingConnection connection;
	
	/**
	 * 首次回数据时间
	 */
	protected Calendar mcu_firstCollectTime;
	
	/**
	 * 定时器下次的采集起点
	 */
	protected Calendar nextCollectTime = null;
	
	public Calendar getNextCollectTime() {
		return nextCollectTime;
	}

	public Calendar getFirstCollectTime() {
		return mcu_firstCollectTime;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getUploadFileSrc() {
		return uploadFileSrc;
	}

	public void setUploadFileSrc(String uploadFileSrc) {
		this.uploadFileSrc = uploadFileSrc;
	}
	
	public INonBlockingConnection getConnection() {
		return connection;
	}

	public void setConnection(INonBlockingConnection connection) {
		this.connection = connection;
	}

	public List<Mcu> getMcus() {
		return mcus;
	}

	public void setMcus(List<Mcu> mcus) {
		this.mcus = mcus;
	}

	public boolean isSetOver() {
		return isSetOver;
	}

	public void setSetOver(boolean isSetOver) {
		this.isSetOver = isSetOver;
	}

	public boolean isGetDataOver() {
		return isGetDataOver;
	}

	public void setGetDataOver(boolean isGetDataOver) {
		this.isGetDataOver = isGetDataOver;
	}

	public boolean isTestOver() {
		return isTestOver;
	}

	public void setTestOver(boolean isTestOver) {
		this.isTestOver = isTestOver;
	}

	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	public List<CabelMeterMeasureData> getCableDataList() {
		return cableDataList;
	}

	public void setCableDataList(List<CabelMeterMeasureData> cableDataList) {
		this.cableDataList = cableDataList;
	}


	public List<WaterLevelMeasureData> getWaterLevelDataList() {
		return waterLevelDataList;
	}

	public void setWaterLevelDataList(List<WaterLevelMeasureData> waterLevelDataList) {
		this.waterLevelDataList = waterLevelDataList;
	}

	public List<StressMeasureData> getStressDataList() {
		return stressDataList;
	}

	public void setStressDataList(List<StressMeasureData> stressDataList) {
		this.stressDataList = stressDataList;
	}

	public List<CxMessureData> getCxDataList() {
		return cxDataList;
	}

	public void setCxDataList(List<CxMessureData> cxDataList) {
		this.cxDataList = cxDataList;
	}

}
