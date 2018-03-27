package com.southgt.smosplat.data.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

//@Entity
//@Table(name="water_level_measure_data")
public class WaterLevelMeasureData implements Serializable {
	
	private static final long serialVersionUID = 3471531834741893000L;
	
//	@Id
//	@GenericGenerator(name="id",strategy="uuid2")
//	@GeneratedValue(generator="id")
//	@Column(name="water_level_measure_data_uuid",unique=true,nullable=false,length=50)
//	private String waterLevelMeasureDataUuid;
	
	/**
	 * 模数值
	 */
//	@Column(name="mod_value",length=50)
	private Double moduleData;
	
	/**
	 * 温度值
	 */
//	@Column(name="temperature_value",length=50)
	private Double temperatureValue;
	
	/**
	 * 计算值(mm)
	 */
//	@Column(name="calculate_value",length=50)
	private Double calculateValue;
	
	/**
	 * 记录创建时间
	 */
//	@CreationTimestamp
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="create_time",updatable=false)
	private Date createTime;
	
	/**
	 * 在一次采集当中mcu群首个mcu采集时间
	 */
//	@Column(name="start_date")
	private Date mcuGroupCollectDate;
	/**
	 * 单次变化量
	 */
//	@Transient
	private double gapOffset;
	/**
	 * 累计变化量
	 */
//	@Transient
	private double accumOffset;
	
	/**
	 * 所属水位点Uuid
	 */
	private String wlSurveyPointUuid; 

	public String getWlSurveyPointUuid() {
		return wlSurveyPointUuid;
	}

	public void setWlSurveyPointUuid(String wlSurveyPointUuid) {
		this.wlSurveyPointUuid = wlSurveyPointUuid;
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

//	public String getWaterLevelMeasureDataUuid() {
//		return waterLevelMeasureDataUuid;
//	}
//
//	public void setWaterLevelMeasureDataUuid(String waterLevelMeasureDataUuid) {
//		this.waterLevelMeasureDataUuid = waterLevelMeasureDataUuid;
//	}

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

	@Override
	public String toString() {
		return "WaterLevelMeasureData [moduleData=" + moduleData + ", temperatureValue=" + temperatureValue
				+ ", calculateValue=" + calculateValue + ", createTime=" + createTime + ", mcuGroupCollectDate="
				+ mcuGroupCollectDate + ", gapOffset=" + gapOffset + ", accumOffset=" + accumOffset
				+ ", wlSurveyPointUuid=" + wlSurveyPointUuid + "]";
	}
	
}







