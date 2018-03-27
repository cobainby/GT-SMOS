package com.southgt.smosplat.data.vo;

import com.southgt.smosplat.project.entity.CableMeter;

public class CabelMeterMeasureData {
	/**
	 * 测到的值
	 */
	private Double moduleData;
	
	/**
	 * 温度值
	 */
	private Double temperatureValue;
	
	/**
	 * 计算值
	 */
	private Double calculateValue;
	
	/**
	 * 所属锚索
	 */
	private CableMeter cableMeter;

	public Double getModuleData() {
		return moduleData;
	}

	public void setModuleData(Double moduleData) {
		this.moduleData = moduleData;
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

	public CableMeter getCableMeter() {
		return cableMeter;
	}

	public void setCableMeter(CableMeter cableMeter) {
		this.cableMeter = cableMeter;
	}

	@Override
	public String toString() {
		return "CabelMeterMeasureData [modValue=" + moduleData + ", temperatureValue=" + temperatureValue
				+ ", calculateValue=" + calculateValue + ", cableMeter=" + cableMeter + "]";
	}
}
