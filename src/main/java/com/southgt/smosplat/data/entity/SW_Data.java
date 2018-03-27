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
import com.southgt.smosplat.project.entity.SurveyPoint_SW;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;

/**
 * 
 * 水位数据
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月2日     姚家俊       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="sw_data")
public class SW_Data implements Serializable{
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="sw_data_uuid",nullable=false,unique=true,length=50)
	private String swDataUuid;
	
	/**
	 * 所属点
	 */
	@ManyToOne
	@JoinColumn(name="survey_point_uuid")
	private SurveyPoint_SW surveyPoint;
	
	/**
	 * 计算值(mm)
	 */
	@Column(name="cal_value",updatable=false)
	private Double calValue;
	
	/**
	 * 模数值
	 */
	@Column(name="module_data")
	private Double moduleData;
	
	/**
	 * 测量时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="collect_time")
	private Date collectTime;
	
	/**
	 * 水位计算值单次位移
	 */
	@Transient
	private double gapOffset;
	
	/**
	 * 单次变化量变化速率
	 */
	@Transient
	private double gapChangeRate;
	
	/**
	 * 水位计算值累计位移
	 */
	@Transient
	private double accumOffset;
	
	/**
	 * 初始内力值
	 */
	@Transient
	private double oriCalVal;

	public String getSwDataUuid() {
		return swDataUuid;
	}

	public void setSwDataUuid(String swDataUuid) {
		this.swDataUuid = swDataUuid;
	}

	public Double getCalValue() {
		return calValue;
	}

	public void setCalValue(Double calValue) {
		this.calValue = calValue;
	}

	public Double getModuleData() {
		return moduleData;
	}

	public void setModuleData(Double moduleData) {
		this.moduleData = moduleData;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public SurveyPoint_SW getSurveyPoint() {
		return surveyPoint;
	}

	public void setSurveyPoint(SurveyPoint_SW surveyPoint) {
		this.surveyPoint = surveyPoint;
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

	public double getAccumOffset() {
		return accumOffset;
	}

	public void setAccumOffset(double accumOffset) {
		this.accumOffset = accumOffset;
	}

	public double getOriCalVal() {
		return oriCalVal;
	}

	public void setOriCalVal(double oriCalVal) {
		this.oriCalVal = oriCalVal;
	}

	@Override
	public String toString() {
		return "SW_Data [swDataUuid=" + swDataUuid + ", surveyPoint=" + surveyPoint + ", calValue=" + calValue
				+ ", moduleData=" + moduleData + ", collectTime=" + collectTime + ", gapOffset=" + gapOffset
				+ ", gapChangeRate=" + gapChangeRate + ", accumOffset=" + accumOffset + ", oriCalVal=" + oriCalVal
				+ "]";
	}
}
