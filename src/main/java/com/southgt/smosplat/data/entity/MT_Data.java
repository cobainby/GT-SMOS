package com.southgt.smosplat.data.entity;

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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;

/**
 * 
 * 锚杆内力数据实体
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月12日     姚家俊       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="mt_data")
public class MT_Data implements Serializable {
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="mt_data_uuid",nullable=false,unique=true,length=50)
	private String mtDataUuid;
	
	/**
	 * 所属点
	 */
	@ManyToOne
	@JoinColumn(name="survey_point_uuid")
	private SurveyPoint_MT surveyPoint;
	
	/**
	 * 计算值
	 */
	@Column(name="cal_value",updatable=false)
	private Double calValue;
	
	/**
	 * 上次计算值
	 */
	@Transient
	private Double lastCalValue;
	
	/**
	 * 采集模数值，用','分隔
	 */
	@Column(name="module_data")
	private String moduleData;
	
	/**
	 * 测量时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="collect_time")
	private Date collectTime;
	
	/**
	 * 轴力计算值单次位移
	 */
	@Transient
	private double gapOffset;
	
	/**
	 * 单次变化量变化速率
	 */
	@Transient
	private double gapChangeRate;
	
	/**
	 * 轴力计算值累计位移
	 */
//	@Transient
//	private double accumOffset;
	
	/**
	 * 初始内力值
	 */
	@Transient
	private double oriCalVal;

	public String getMtDataUuid() {
		return mtDataUuid;
	}

	public void setMtDataUuid(String mtDataUuid) {
		this.mtDataUuid = mtDataUuid;
	}

	public Double getLastCalValue() {
		return lastCalValue;
	}

	public void setLastCalValue(Double lastCalValue) {
		this.lastCalValue = lastCalValue;
	}

	public SurveyPoint_MT getSurveyPoint() {
		return surveyPoint;
	}

	public void setSurveyPoint(SurveyPoint_MT surveyPoint) {
		this.surveyPoint = surveyPoint;
	}

	public Double getCalValue() {
		return calValue;
	}

	public void setCalValue(Double calValue) {
		this.calValue = calValue;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public double getGapOffset() {
		return gapOffset;
	}

	public void setGapOffset(double gapOffset) {
		this.gapOffset = gapOffset;
	}

	public double getGapChangeRate() {
		return gapChangeRate;
	}

	public void setGapChangeRate(double gapChangeRate) {
		this.gapChangeRate = gapChangeRate;
	}

//	public double getAccumOffset() {
//		return accumOffset;
//	}
//
//	public void setAccumOffset(double accumOffset) {
//		this.accumOffset = accumOffset;
//	}

	public String getModuleData() {
		return moduleData;
	}

	public void setModuleData(String moduleData) {
		this.moduleData = moduleData;
	}


	public double getOriCalVal() {
		return oriCalVal;
	}

	public void setOriCalVal(double oriCalVal) {
		this.oriCalVal = oriCalVal;
	}

	@Override
	public String toString() {
		return "MT_Data [mtDataUuid=" + mtDataUuid + ", surveyPoint=" + surveyPoint + ", calValue=" + calValue
				+ ", lastCalValue=" + lastCalValue + ", moduleData=" + moduleData + ", collectTime=" + collectTime
				+ ", gapOffset=" + gapOffset + ", gapChangeRate=" + gapChangeRate + ", oriCalVal=" + oriCalVal + "]";
	}
	
}
