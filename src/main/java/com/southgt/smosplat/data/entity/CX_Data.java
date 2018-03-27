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
import com.southgt.smosplat.project.entity.SurveyPoint_CX;

/**
 * 
 * 测斜数据实体
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月10日     姚家俊       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="cx_data")
public class CX_Data implements Serializable{
	
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="cx_data_uuid",nullable=false,unique=true,length=50)
	private String cxDataUuid;
	/**
	 * 深度
	 */
	@Column(name="depth")
	private double depth;
	/**
	 * 测斜计算值
	 */
	@Column(name="cal_value",updatable=false)
	private double calValue;
	/**
	 * 单次变化量
	 */
	@Column(name="gap_offset")
	private double gapOffset;
	/**
	 * 变化速率
	 */
	@Column(name="change_rate")
	private double changeRate;
	/**
	 * 累计变化量
	 */
	@Column(name="accum_offset")
	private double accumOffset;
	/**
	 * 所属点
	 */
	@ManyToOne
	@JoinColumn(name="survey_point_uuid")
	private SurveyPoint_CX surveyPoint;
	/**
	 * 测量时间
	 */
	@Column(name="collect_time")
	private Date collectTime;
	/**
	 * a0轴
	 */
	@Transient
	private double a0Val;
	/**
	 * a180轴
	 */
	@Transient
	private double a180Val;
	/**
	 * 测量序号
	 */
	@Column(name="survey_order")
	private int surveyOrder;
	
	public String getCxDataUuid() {
		return cxDataUuid;
	}
	public void setCxDataUuid(String cxDataUuid) {
		this.cxDataUuid = cxDataUuid;
	}
	public double getCalValue() {
		return calValue;
	}
	public void setCalValue(double calValue) {
		this.calValue = calValue;
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
	public SurveyPoint_CX getSurveyPoint() {
		return surveyPoint;
	}
	public void setSurveyPoint(SurveyPoint_CX surveyPoint) {
		this.surveyPoint = surveyPoint;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	
	public double getDepth() {
		return depth;
	}
	public void setDepth(double depth) {
		this.depth = depth;
	}
	public double getA0Val() {
		return a0Val;
	}
	public void setA0Val(double a0Val) {
		this.a0Val = a0Val;
	}
	public double getA180Val() {
		return a180Val;
	}
	public void setA180Val(double a180Val) {
		this.a180Val = a180Val;
	}
	public double getChangeRate() {
		return changeRate;
	}
	public void setChangeRate(double changeRate) {
		this.changeRate = changeRate;
	}
	public int getSurveyOrder() {
		return surveyOrder;
	}
	public void setSurveyOrder(int surveyOrder) {
		this.surveyOrder = surveyOrder;
	}
	@Override
	public String toString() {
		return "CX_Data [cxDataUuid=" + cxDataUuid + ", depth=" + depth + ", calValue=" + calValue + ", gapOffset="
				+ gapOffset + ", changeRate=" + changeRate + ", accumOffset=" + accumOffset + ", surveyPoint="
				+ surveyPoint + ", collectTime=" + collectTime + ", a0Val=" + a0Val + ", a180Val=" + a180Val
				+ ", surveyOrder=" + surveyOrder + "]";
	}
}
