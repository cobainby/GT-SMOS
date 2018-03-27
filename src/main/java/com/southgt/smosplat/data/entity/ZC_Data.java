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
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;

/**
 * 轴力数据
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月27日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="zc_data")
public class ZC_Data implements Serializable {
	
	private static final long serialVersionUID = -5536498800272951555L;

	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="zc_data_uuid",nullable=false,unique=true,length=50)
	private String zcDataUuid;
	
	/**
	 * 所属点
	 */
	@ManyToOne
	@JoinColumn(name="survey_point_uuid")
	private SurveyPoint_ZC surveyPoint;
	
	/**
	 * 计算值
	 */
	@Column(name="cal_value",updatable=false)
	private Double calValue;
	
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
	 * 头3次数据求平均初始内力值
	 */
	@Transient
	private double oriCalVal;

	public Double getCalValue() {
		return calValue;
	}

	public void setCalValue(Double calValue) {
		this.calValue = calValue;
	}

	public String getZcDataUuid() {
		return zcDataUuid;
	}

	public void setZcDataUuid(String zcDataUuid) {
		this.zcDataUuid = zcDataUuid;
	}

	public SurveyPoint_ZC getSurveyPoint() {
		return surveyPoint;
	}

	public void setSurveyPoint(SurveyPoint_ZC surveyPoint) {
		this.surveyPoint = surveyPoint;
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

	public double getOriCalVal() {
		return oriCalVal;
	}

	public void setOriCalVal(double oriCalVal) {
		this.oriCalVal = oriCalVal;
	}

	@Override
	public String toString() {
		return "ZC_Data [zcDataUuid=" + zcDataUuid + ", surveyPoint=" + surveyPoint + ", calValue=" + calValue
				+ ", collectTime=" + collectTime + ", gapOffset=" + gapOffset + ", gapChangeRate=" + gapChangeRate
				+ ", oriCalVal=" + oriCalVal + "]";
	}

}
