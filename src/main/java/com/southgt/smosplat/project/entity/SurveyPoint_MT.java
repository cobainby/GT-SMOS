package com.southgt.smosplat.project.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 锚杆内力实体
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月21日     YANG       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="survey_point_mt")
public class SurveyPoint_MT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="survey_point_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String surveyPointUuid;
	
	/**
	 * 测点编号
	 */
	@Column(name="code",length=50,nullable=false)
	private String code;
	
	/**
	 * 设备类型
	 */
	@Column(name="device_type",length=50,nullable=false)
	private String deviceType;
	
	/**
	 * 设备型号
	 */
	@Column(name="dev_model",length=50)
	private String deviceModel;
	
	/**
	 * 设备编号
	 */
	@Column(name="dev_code",length=50)
	private String devCode;
	
	/**
	 * 频率
	 */
	@Column(name="frequency")
	private Float frequency;
	
	/**
	 * 标定系数值
	 */
	@Column(name="calibrated_mod")
	private Double calibratedMod;
	
	/**
	 * 初始累计值
	 */
	@Column(name="original_total_value")
	private Float originalTotalValue;
	
	/**
	 * 报警设置名称
	 */
	@ManyToOne
	@JoinColumn(name="warning_uuid",nullable=false)
	private Warning warning;

	/**
	 * 所属监测项
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="monitor_item_uuid")
	private MonitorItem monitorItem;
	
	/**
	 * 所属项目
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="project_uuid")
	private Project project;
	
	/**
	 * 处理了的数据的uuid，用以判断当前监测点的报警情况。
	 */
	@Column(name="processed_data_uuid",length=50,updatable=true)
	private String processedDataUuid;

	public String getProcessedDataUuid() {
		return processedDataUuid;
	}

	public void setProcessedDataUuid(String processedDataUuid) {
		this.processedDataUuid = processedDataUuid;
	}

	public String getSurveyPointUuid() {
		return surveyPointUuid;
	}

	public void setSurveyPointUuid(String surveyPointUuid) {
		this.surveyPointUuid = surveyPointUuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDevCode() {
		return devCode;
	}

	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}

	public Float getFrequency() {
		return frequency;
	}

	public void setFrequency(Float frequency) {
		this.frequency = frequency;
	}

	public Double getCalibratedMod() {
		return calibratedMod;
	}

	public void setCalibratedMod(Double calibratedMod) {
		this.calibratedMod = calibratedMod;
	}

	public Float getOriginalTotalValue() {
		return originalTotalValue;
	}

	public void setOriginalTotalValue(Float originalTotalValue) {
		this.originalTotalValue = originalTotalValue;
	}

	public Warning getWarning() {
		return warning;
	}

	public void setWarning(Warning warning) {
		this.warning = warning;
	}

	public MonitorItem getMonitorItem() {
		return monitorItem;
	}

	public void setMonitorItem(MonitorItem monitorItem) {
		this.monitorItem = monitorItem;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "SurveyPoint_MT [surveyPointUuid=" + surveyPointUuid + ", code=" + code + ", deviceType=" + deviceType
				+ ", deviceModel=" + deviceModel + ", devCode=" + devCode + ", frequency=" + frequency
				+ ", calibratedMod=" + calibratedMod + ", originalTotalValue=" + originalTotalValue + ", warning="
				+ warning + ", monitorItem=" + monitorItem + ", project=" + project + ", processedDataUuid="
				+ processedDataUuid + "]";
	}


	

}
