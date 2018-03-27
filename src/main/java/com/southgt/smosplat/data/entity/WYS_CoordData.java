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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;

/**
 * 
 * 全站仪解算数据实体
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月25日     姚家俊       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="wys_coord_data")
public class WYS_CoordData implements Serializable{

	private static final long serialVersionUID = 6680764518302137168L;
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="coord_data_uuid",unique=true,nullable=false,length=50)
	private String coordDataUuid;

	/**
	 * 生成时间
	 */
	@Column(name="survey_time")
	private Date surveyTime;
	
	/**
	 * 解算水平角
	 */
	@Column(name="caculate_ha")
	private Double caculateHA;
	/**
	 * 解算竖直角
	 */
	@Column(name="caculate_va")
	private Double caculateVA;
	/**
	 * 解算斜距
	 */
	@Column(name="caculate_sd")
	private Double caculateSD;
	/**
	 * 东坐标
	 */
	@Column(name="caculate_e",updatable=false)
	private Double caculateE;
	/**
	 * 北坐标
	 */
	@Column(name="caculate_n",updatable=false)
	private Double caculateN;
	/**
	 * 高程
	 */
	@Column(name="caculate_h",updatable=false)
	private Double caculateH;
	/**
	 * 监测点
	 */
	@ManyToOne(optional=false)
	@JoinColumn(name="survey_point_uuid")
	private SurveyPoint_WYS surveyPoint;
	/**
	 * 东单次变化量
	 */
	@Transient
	private double gapEOffset;
	/**
	 * 水平位移变化速率
	 */
	@Transient
	private double changeRate;
	
	/**
	 * 竖向位移变化速率
	 */
	@Transient
	private double hChangeRate;
	/**
	 * 东累计变化量
	 */
	@Transient
	private double accumEOffset;
	/**
	 * 北单次变化量
	 */
	@Transient
	private double gapNOffset;
	/**
	 * 北累积变化量
	 */
	@Transient
	private double accumNOffset;
	/**
	 * 高程单次变化量
	 */
	@Transient
	private double gapHOffset;
	/**
	 * 高程累计变化量
	 */
	@Transient
	private double accumHOffset;
	
	public String getCoordDataUuid() {
		return coordDataUuid;
	}



	public void setCoordDataUuid(String coordDataUuid) {
		this.coordDataUuid = coordDataUuid;
	}
	
	
	
	public Date getSurveyTime() {
		return surveyTime;
	}



	public void setSurveyTime(Date surveyTime) {
		this.surveyTime = surveyTime;
	}



	public Double getCaculateHA() {
		return caculateHA;
	}



	public void setCaculateHA(Double caculateHA) {
		this.caculateHA = caculateHA;
	}



	public Double getCaculateVA() {
		return caculateVA;
	}



	public void setCaculateVA(Double caculateVA) {
		this.caculateVA = caculateVA;
	}



	public Double getCaculateSD() {
		return caculateSD;
	}



	public void setCaculateSD(Double caculateSD) {
		this.caculateSD = caculateSD;
	}



	public Double getCaculateE() {
		return caculateE;
	}



	public void setCaculateE(Double caculateE) {
		this.caculateE = caculateE;
	}



	public Double getCaculateN() {
		return caculateN;
	}



	public void setCaculateN(Double caculateN) {
		this.caculateN = caculateN;
	}



	public Double getCaculateH() {
		return caculateH;
	}



	public void setCaculateH(Double caculateH) {
		this.caculateH = caculateH;
	}



	public SurveyPoint_WYS getSurveyPoint() {
		return surveyPoint;
	}



	public void setSurveyPoint(SurveyPoint_WYS surveyPoint) {
		this.surveyPoint = surveyPoint;
	}



	public double getGapEOffset() {
		return gapEOffset;
	}



	public void setGapEOffset(double gapEOffset) {
		this.gapEOffset = gapEOffset;
	}



	public double getAccumEOffset() {
		return accumEOffset;
	}



	public void setAccumEOffset(double accumEOffset) {
		this.accumEOffset = accumEOffset;
	}



	public double getChangeRate() {
		return changeRate;
	}



	public void setChangeRate(double changeRate) {
		this.changeRate = changeRate;
	}



	public double gethChangeRate() {
		return hChangeRate;
	}



	public void sethChangeRate(double hChangeRate) {
		this.hChangeRate = hChangeRate;
	}



	public double getGapNOffset() {
		return gapNOffset;
	}



	public void setGapNOffset(double gapNOffset) {
		this.gapNOffset = gapNOffset;
	}



	public double getAccumNOffset() {
		return accumNOffset;
	}



	public void setAccumNOffset(double accumNOffset) {
		this.accumNOffset = accumNOffset;
	}



	public double getGapHOffset() {
		return gapHOffset;
	}



	public void setGapHOffset(double gapHOffset) {
		this.gapHOffset = gapHOffset;
	}



	public double getAccumHOffset() {
		return accumHOffset;
	}



	public void setAccumHOffset(double accumHOffset) {
		this.accumHOffset = accumHOffset;
	}



	@Override
	public String toString() {
		return "WYS_CoordData [coordDataUuid=" + coordDataUuid + ", surveyTime=" + surveyTime + ", caculateHA="
				+ caculateHA + ", caculateVA=" + caculateVA + ", caculateSD=" + caculateSD + ", caculateE=" + caculateE
				+ ", caculateN=" + caculateN + ", caculateH=" + caculateH + ", surveyPoint=" + surveyPoint
				+ ", gapEOffset=" + gapEOffset + ", changeRate=" + changeRate + ", hChangeRate=" + hChangeRate
				+ ", accumEOffset=" + accumEOffset + ", gapNOffset=" + gapNOffset + ", accumNOffset=" + accumNOffset
				+ ", gapHOffset=" + gapHOffset + ", accumHOffset=" + accumHOffset + "]";
	}
	
}
