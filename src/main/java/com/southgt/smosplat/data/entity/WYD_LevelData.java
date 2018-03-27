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
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;

/**
 * 
 * 水准数据实体类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月27日     姚家俊       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="wyd_level_data")
public class WYD_LevelData implements Serializable{
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="level_data_uuid",unique=true,nullable=false,length=50)
	private String levelDataUuid;

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
	private double gapHChangeRate;
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
	private SurveyPoint_WYD surveyPoint;
	
	/**
	 * 3次平均初始值
	 */
	@Transient
	private double OriginalAveH;

	public String getLevelDataUuid() {
		return levelDataUuid;
	}

	public void setLevelDataUuid(String levelDataUuid) {
		this.levelDataUuid = levelDataUuid;
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

	public double getGapHChangeRate() {
		return gapHChangeRate;
	}

	public void setGapHChangeRate(double gapHChangeRate) {
		this.gapHChangeRate = gapHChangeRate;
	}

	public double getAccumHOffset() {
		return accumHOffset;
	}

	public void setAccumHOffset(double accumHOffset) {
		this.accumHOffset = accumHOffset;
	}

	public SurveyPoint_WYD getSurveyPoint() {
		return surveyPoint;
	}

	public void setSurveyPoint(SurveyPoint_WYD surveyPoint) {
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
		return "WYD_LevelData [levelDataUuid=" + levelDataUuid + ", surveyTime=" + surveyTime + ", levelH=" + levelH
				+ ", gapHOffset=" + gapHOffset + ", gapHChangeRate=" + gapHChangeRate + ", accumHOffset=" + accumHOffset
				+ ", surveyPoint=" + surveyPoint + ", OriginalAveH=" + OriginalAveH + "]";
	}
}
