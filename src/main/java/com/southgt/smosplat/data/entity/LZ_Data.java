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
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;

/**
 * 
 * 立柱竖向位移实体类
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
@Table(name="lz_data")
public class LZ_Data implements Serializable{
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="lz_uuid",unique=true,nullable=false,length=50)
	private String lzDataUuid;

	/**
	 * 生成时间
	 */
	@Column(name="survey_time")
	private Date surveyTime;
	
	/**
	 * 测点水准高程
	 */
	@Column(name="level_h",updatable=false)
	private double levelH;
	
	/**
	 * 高程单次变化量
	 */
	@Transient
	private double gapHOffset;
	
	/**
	 * 高程单次变化量变化率
	 */
	@Transient
	private double gapHOffsetChangeRate;
	/**
	 * 高程累计变化量
	 */
	@Transient
	private double accumHOffset;
	
	/**
	 * 监测点
	 */
	@ManyToOne(optional=false)
	@JoinColumn(name="survey_point_uuid")
	private SurveyPoint_LZ surveyPoint;
	
	/**
	 * 3次平均初始值
	 */
	@Transient
	private double OriginalAveH;

	public String getLzDataUuid() {
		return lzDataUuid;
	}

	public void setLzDataUuid(String lzDataUuid) {
		this.lzDataUuid = lzDataUuid;
	}

	public Date getSurveyTime() {
		return surveyTime;
	}

	public void setSurveyTime(Date surveyTime) {
		this.surveyTime = surveyTime;
	}

	public double getLevelH() {
		return levelH;
	}

	public void setLevelH(double levelH) {
		this.levelH = levelH;
	}

	public double getGapHOffset() {
		return gapHOffset;
	}

	public void setGapHOffset(double gapHOffset) {
		this.gapHOffset = gapHOffset;
	}

	public double getGapHOffsetChangeRate() {
		return gapHOffsetChangeRate;
	}

	public void setGapHOffsetChangeRate(double gapHOffsetChangeRate) {
		this.gapHOffsetChangeRate = gapHOffsetChangeRate;
	}

	public double getAccumHOffset() {
		return accumHOffset;
	}

	public void setAccumHOffset(double accumHOffset) {
		this.accumHOffset = accumHOffset;
	}

	public SurveyPoint_LZ getSurveyPoint() {
		return surveyPoint;
	}

	public void setSurveyPoint(SurveyPoint_LZ surveyPoint) {
		this.surveyPoint = surveyPoint;
	}

	public double getOriginalAveH() {
		return OriginalAveH;
	}

	public void setOriginalAveH(double originalAveH) {
		OriginalAveH = originalAveH;
	}

	@Override
	public String toString() {
		return "LZ_Data [lzDataUuid=" + lzDataUuid + ", surveyTime=" + surveyTime + ", levelH=" + levelH
				+ ", gapHOffset=" + gapHOffset + ", gapHOffsetChangeRate=" + gapHOffsetChangeRate + ", accumHOffset="
				+ accumHOffset + ", surveyPoint=" + surveyPoint + ", OriginalAveH=" + OriginalAveH + "]";
	}
	
}
