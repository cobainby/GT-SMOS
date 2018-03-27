package com.southgt.smosplat.project.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="mobile_survey_point")
public class Mobile_SurveyPoint implements Serializable {

	private static final long serialVersionUID = 7364435486069750532L;

	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="sp_uuid",unique=true,nullable=false,length=50)
	@JsonIgnore
	private String spUuid;
	
	@Column(name = "survey_point_uuid")
	private String surveyPointUuid;
	
	/** 测站id */
	@Column(name = "monitor_item_uuid")
	private String monitorItemUuid;
	
	@Column(name = "project_uuid")
	private String projectUuid;
	
	@Column(name = "station_uuid")
	private String stationUuid;
	
	@Column(name = "in_station_name")
	private String inStationName;
	
	@Column(name = "section_uuid")
	private String sectionUuid;
	
	@Column(name = "in_section_name")
	private String inSectionName;
	
	@Column(name = "survey_point_name")
	private String surveyPointName;
	
	@Column(name = "survey_point_east")
	private double surveyPointEast;
	
	@Column(name = "survey_point_north")
	private double surveyPointNorth;
	
	@Column(name = "survey_point_height")
	private double surveyPointHeight;
	
	@Column(name = "survey_point_ha")
	private double surveyPointHa;
	
	@Column(name = "survey_point_va")
	private double surveyPointVa;
	
	@Column(name = "survey_point_sd")
	private double surveyPointSd;
	
	@Column(name = "survey_point_prism_height")
	private double surveyPointPrismHeight;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "sp_type")
	private boolean spType;
	
	@Column(name = "base_point_uuid")
	private String basePointUuid;
	
	@Column(name = "order_create")
	@JsonIgnore
	private int orderCreate;

	public int getOrderCreate() {
		return orderCreate;
	}

	public void setOrderCreate(int orderCreate) {
		this.orderCreate = orderCreate;
	}

	public String getSpUuid() {
		return spUuid;
	}

	public void setSpUuid(String spUuid) {
		this.spUuid = spUuid;
	}

	public String getSurveyPointUuid() {
		return surveyPointUuid;
	}

	public void setSurveyPointUuid(String surveyPointUuid) {
		this.surveyPointUuid = surveyPointUuid;
	}

	public String getMonitorItemUuid() {
		return monitorItemUuid;
	}

	public void setMonitorItemUuid(String monitorItemUuid) {
		this.monitorItemUuid = monitorItemUuid;
	}

	public String getProjectUuid() {
		return projectUuid;
	}

	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;
	}

	public String getStationUuid() {
		return stationUuid;
	}

	public void setStationUuid(String stationUuid) {
		this.stationUuid = stationUuid;
	}

	public String getInStationName() {
		return inStationName;
	}

	public void setInStationName(String inStationName) {
		this.inStationName = inStationName;
	}

	public String getSectionUuid() {
		return sectionUuid;
	}

	public void setSectionUuid(String sectionUuid) {
		this.sectionUuid = sectionUuid;
	}

	public String getInSectionName() {
		return inSectionName;
	}

	public void setInSectionName(String inSectionName) {
		this.inSectionName = inSectionName;
	}

	public String getSurveyPointName() {
		return surveyPointName;
	}

	public void setSurveyPointName(String surveyPointName) {
		this.surveyPointName = surveyPointName;
	}

	public double getSurveyPointEast() {
		return surveyPointEast;
	}

	public void setSurveyPointEast(double surveyPointEast) {
		this.surveyPointEast = surveyPointEast;
	}

	public double getSurveyPointNorth() {
		return surveyPointNorth;
	}

	public void setSurveyPointNorth(double surveyPointNorth) {
		this.surveyPointNorth = surveyPointNorth;
	}

	public double getSurveyPointHeight() {
		return surveyPointHeight;
	}

	public void setSurveyPointHeight(double surveyPointHeight) {
		this.surveyPointHeight = surveyPointHeight;
	}

	public double getSurveyPointHa() {
		return surveyPointHa;
	}

	public void setSurveyPointHa(double surveyPointHa) {
		this.surveyPointHa = surveyPointHa;
	}

	public double getSurveyPointVa() {
		return surveyPointVa;
	}

	public void setSurveyPointVa(double surveyPointVa) {
		this.surveyPointVa = surveyPointVa;
	}

	public double getSurveyPointSd() {
		return surveyPointSd;
	}

	public void setSurveyPointSd(double surveyPointSd) {
		this.surveyPointSd = surveyPointSd;
	}

	public double getSurveyPointPrismHeight() {
		return surveyPointPrismHeight;
	}

	public void setSurveyPointPrismHeight(double surveyPointPrismHeight) {
		this.surveyPointPrismHeight = surveyPointPrismHeight;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isSpType() {
		return spType;
	}

	public void setSpType(boolean spType) {
		this.spType = spType;
	}

	public String getBasePointUuid() {
		return basePointUuid;
	}

	public void setBasePointUuid(String basePointUuid) {
		this.basePointUuid = basePointUuid;
	}

	@Override
	public String toString() {
		return "Mobile_SurveyPoint [surveyPointUuid=" + surveyPointUuid + ", monitorItemUuid=" + monitorItemUuid
				+ ", projectUuid=" + projectUuid + ", stationUuid=" + stationUuid + ", inStationName=" + inStationName
				+ ", sectionUuid=" + sectionUuid + ", inSectionName=" + inSectionName + ", surveyPointName="
				+ surveyPointName + ", surveyPointEast=" + surveyPointEast + ", surveyPointNorth=" + surveyPointNorth
				+ ", surveyPointHeight=" + surveyPointHeight + ", surveyPointHa=" + surveyPointHa + ", surveyPointVa="
				+ surveyPointVa + ", surveyPointSd=" + surveyPointSd + ", surveyPointPrismHeight="
				+ surveyPointPrismHeight + ", remark=" + remark + ", spType=" + spType + ", basePointUuid="
				+ basePointUuid + "]";
	}
	
	
}
