package com.southgt.smosplat.data.vo;

import java.io.Serializable;

/**
 * 每个轴力计测到的数据
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月28日     mohaolin       v1.0.0        create</p>
 *
 */
public class StressMeasureData implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 测到的值
	 */
	private Double modValue;
	
	/**
	 * 温度值
	 */
	private Double temperatureValue;
	
	/**
	 * 计算值
	 */
	private Double calculateValue;
	
	/**
	 * 所属轴力
	 */
	private String stressUuid;

	public Double getModValue() {
		return modValue;
	}

	public void setModValue(Double modValue) {
		this.modValue = modValue;
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

	public String getStressUuid() {
		return stressUuid;
	}

	public void setStressUuid(String stressUuid) {
		this.stressUuid = stressUuid;
	}

	@Override
	public String toString() {
		return "StressMeasureData [modValue=" + modValue + ", temperatureValue=" + temperatureValue
				+ ", calculateValue=" + calculateValue + ", stressUuid=" + stressUuid + "]";
	}

}
