package com.southgt.smosplat.project.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.southgt.smosplat.organ.entity.Device;

/**
 * 
 * 自动测斜仪实体
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年1月14日     姚家俊       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="clinometer")
public class Clinometer implements Serializable{

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="clinometer_Uuid",length=50,nullable=false,unique=true,updatable=false)
	private String clinometerUuid;
	
	/**
	 * 所属监测点
	 */
	@ManyToOne
	@JoinColumn(name="surveyPointUuid")
	SurveyPoint_CX sp_CX;
	
	/**
	 * 对应的设备
	 */
	@OneToOne
	@JoinColumn(name="device_uuid")
	private Device device;
	
	/**
	 * 测斜名称
	 */
	@Column(name="name",length=50)
	private String name;
	
	/**
	 * 初始值
	 */
	@Column(name="start_value")
	private Double startValue;
	
	/**
	 * 标定系数值
	 */
	@Column(name="calibrated_mod")
	private Double calibratedMod;
	
	/**
	 * 所属项目
	 */
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="projectUuid")
	private Project project;
	
	/**
	 * 设备编号
	 */
	@Column(name="dev_code",length=50)
	private String devCode;
	
	
	/**
	 * 0.5m/1m间隔
	 */
	@Column(name="gap",length=5)
	private String gap;

	public String getGap() {
		return gap;
	}

	public void setGap(String gap) {
		this.gap = gap;
	}

	public String getClinometerUuid() {
		return clinometerUuid;
	}

	public void setClinometerUuid(String clinometerUuid) {
		this.clinometerUuid = clinometerUuid;
	}

	public SurveyPoint_CX getSp_CX() {
		return sp_CX;
	}

	public void setSp_CX(SurveyPoint_CX sp_CX) {
		this.sp_CX = sp_CX;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getStartValue() {
		return startValue;
	}

	public void setStartValue(Double startValue) {
		this.startValue = startValue;
	}

	public Double getCalibratedMod() {
		return calibratedMod;
	}

	public void setCalibratedMod(Double calibratedMod) {
		this.calibratedMod = calibratedMod;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getDevCode() {
		return devCode;
	}

	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}

	
	
}
