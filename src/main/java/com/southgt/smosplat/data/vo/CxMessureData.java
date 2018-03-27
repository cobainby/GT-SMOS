package com.southgt.smosplat.data.vo;

import java.io.Serializable;
import java.util.Date;

import com.southgt.smosplat.project.entity.Clinometer;
/**
 * 
 * 测得的测斜数据
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年1月12日     姚家俊       v1.0.0        create</p>
 *
 */
public class CxMessureData{


	/**
	 * 频率值
	 */
	private Double hzData;
	
	/**
	 * 温度值
	 */
	private Double temperatureValue;
	
	/**
	 * 计算值(mm)
	 */
	private Double calculateValue;
	
	/**
	 * 记录创建时间
	 */
	private Date createTime;
	
	/**
	 * 在一次采集当中mcu群首个mcu采集时间
	 */
	private Date mcuGroupCollectDate;
	/**
	 * 单次变化量
	 */
	private double gapOffset;
	/**
	 * 累计变化量
	 */
	private double accumOffset;
	
	/**
	 * 所属锚索监测点（孔）
	 */
	private String cxSpUuid;
	
	/**
	 * 所属测斜仪
	 */
	private Clinometer clinometer;

	public Double getHzData() {
		return hzData;
	}

	public void setHzData(Double hzData) {
		this.hzData = hzData;
	}

	public Double getTemperatureValue() {
		return temperatureValue;
	}

	public void setTemperatureValue(Double temperatureValue) {
		this.temperatureValue = temperatureValue;
	}

	public Double getCalculateValue() {
		return calculateValue;
	}

	public void setCalculateValue(Double calculateValue) {
		this.calculateValue = calculateValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getMcuGroupCollectDate() {
		return mcuGroupCollectDate;
	}

	public void setMcuGroupCollectDate(Date mcuGroupCollectDate) {
		this.mcuGroupCollectDate = mcuGroupCollectDate;
	}

	public double getGapOffset() {
		return gapOffset;
	}

	public void setGapOffset(double gapOffset) {
		this.gapOffset = gapOffset;
	}

	public double getAccumOffset() {
		return accumOffset;
	}

	public void setAccumOffset(double accumOffset) {
		this.accumOffset = accumOffset;
	}

	public String getCxSpUuid() {
		return cxSpUuid;
	}

	public void setCxSpUuid(String cxSpUuid) {
		this.cxSpUuid = cxSpUuid;
	}

	public Clinometer getClinometer() {
		return clinometer;
	}

	public void setClinometer(Clinometer clinometer) {
		this.clinometer = clinometer;
	}
	
}
