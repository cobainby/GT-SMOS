package com.southgt.smosplat.data.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;

/**
 * 全站仪原始数据
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月8日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="wys_original_data")
public class WYS_OriginalData implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="original_data_uuid",unique=true,nullable=false,length=50)
	private String originalDataUuid;
	/**
	 * 读盘位置(0:盘左，1:盘右)
	 */
	@Column(name="face")
	private Byte face;
	/**
	 * 水平角
	 */
	@Column(name="ha")
	private Double ha;
	/**
	 * 竖直角
	 */
	@Column(name="va")
	private Double va;
	/**
	 * 斜距
	 */
	@Column(name="sd")
	private Double sd;
	/**
	 * 观测时间
	 */
	@Column(name="survey_time")
	private Date surveyTime;
	/**
	 * 棱镜高
	 */
	@Column(name="prism_h")
	private Double prismH;
	/**
	 * 监测点
	 */
	@ManyToOne(optional=false)
	@JoinColumn(name="survey_point_uuid")
	private SurveyPoint_WYS surveyPoint;

	/**
	 * 测回序号
	 */
	@Column(name="repeat_measure_order")
	private Byte repeatMeasureOrder;
	/**
	 * 测回总数
	 */
	@Column(name="repeat_measure_count")
	private Byte repeatMeasureCount;
	/**
	 * 东坐标
	 */
	@Column(name="east")
	private Double east;
	public Double getEast() {
		return east;
	}
	/**
	 * 北坐标
	 */
	@Column(name="north")
	private Double north;
	/**
	 * 高坐标
	 */
	@Column(name="height")
	private Double height;
	
	/**
	 * 全站仪监测点类型。0：后视点第一次照准；1：后视点第二次照准；3：普通监测点
	 */
	@Column(name="point_type")
	private Byte pointType;
	
	public Byte getPointType() {
		return pointType;
	}

	public void setPointType(Byte pointType) {
		this.pointType = pointType;
	}

	public String getOriginalDataUuid() {
		return originalDataUuid;
	}
	public void setOriginalDataUuid(String originalDataUuid) {
		this.originalDataUuid = originalDataUuid;
	}
	public Byte getFace() {
		return face;
	}
	public void setFace(Byte face) {
		this.face = face;
	}
	public Double getHa() {
		return ha;
	}
	public void setHa(Double ha) {
		this.ha = ha;
	}
	public Double getVa() {
		return va;
	}
	public void setVa(Double va) {
		this.va = va;
	}
	public Double getSd() {
		return sd;
	}
	public void setSd(Double sd) {
		this.sd = sd;
	}
	public void setEast(Double east) {
		this.east = east;
	}
	public Double getNorth() {
		return north;
	}
	public void setNorth(Double north) {
		this.north = north;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Date getSurveyTime() {
		return surveyTime;
	}
//	public void setSurveyTime(String surveyTime) throws ParseException {
//		Date parsedDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(surveyTime);
//		this.surveyTime = parsedDate;
//	}
	public Double getPrismH() {
		return prismH;
	}
	public void setPrismH(Double prismH) {
		this.prismH = prismH;
	}
	public SurveyPoint_WYS getSurveyPoint() {
		return surveyPoint;
	}
	public void setSurveyPoint(SurveyPoint_WYS surveyPoint) {
		this.surveyPoint = surveyPoint;
	}
	public void setSurveyTime(Date surveyTime) {
		this.surveyTime = surveyTime;
	}
	public Byte getRepeatMeasureOrder() {
		return repeatMeasureOrder;
	}
	public void setRepeatMeasureOrder(Byte repeatMeasureOrder) {
		this.repeatMeasureOrder = repeatMeasureOrder;
	}
	public Byte getRepeatMeasureCount() {
		return repeatMeasureCount;
	}
	public void setRepeatMeasureCount(Byte repeatMeasureCount) {
		this.repeatMeasureCount = repeatMeasureCount;
	}

	@Override
	public String toString() {
		return "WYS_OriginalData [originalDataUuid=" + originalDataUuid + ", face=" + face + ", ha=" + ha + ", va=" + va
				+ ", sd=" + sd + ", surveyTime=" + surveyTime + ", prismH=" + prismH + ", surveyPoint=" + surveyPoint
				+ ", repeatMeasureOrder=" + repeatMeasureOrder + ", repeatMeasureCount=" + repeatMeasureCount
				+ ", east=" + east + ", north=" + north + ", height=" + height + "]";
	}
	
}
