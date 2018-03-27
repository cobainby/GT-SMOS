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

/**
 * 手机——后视点
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年11月14日     YANG       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="mobile_back_point")
public class Mobile_BackPoint implements Serializable {
	private static final long serialVersionUID = 7364435486069750532L;

	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="bp_uuid",unique=true,nullable=false,length=50)
	@JsonIgnore
	private String bpUuid;
	
	/** 手机端uuid */
	@Column(name = "back_point_uuid")
	private String backPointUuid;
	
	/** 测站id */
	@Column(name = "station_uuid")
	private String stationUuid;
	
	/**基准点*/
	@Column(name = "base_point_uuid")
	private String basePointUuid;

	@Column(name = "back_point_name")
	private String backPointName;	
	
	@Column(name = "back_point_east")
	private double backPointEast;

	@Column(name = "back_point_north")
	private double backPointNorth;
	
	@Column(name = "back_point_height")
	private double backPointHeight;

	@Column(name = "back_point_suy_east")
	private double backPointSuyEast;

	@Column(name = "back_point_suy_north")
	private double backPointSuyNorth;

	@Column(name = "back_point_suy_height")
	private double backPointSuyHeight;
	
	@Column(name = "back_point_ha")
	private double backPointHa;
	
	@Column(name = "back_point_va")
	private double backPointVa;
	
	@Column(name = "back_point_sd")
	private double backPointSd;
	
	@Column(name = "back_point_prism_height")
	private double backPointPrismHeight;

	@Column(name = "order_create")
	@JsonIgnore
	private int orderCreate;

	public String getBpUuid() {
		return bpUuid;
	}

	public void setBpUuid(String bpUuid) {
		this.bpUuid = bpUuid;
	}

	public int getOrderCreate() {
		return orderCreate;
	}

	public void setOrderCreate(int orderCreate) {
		this.orderCreate = orderCreate;
	}

	public String getBackPointUuid() {
		return backPointUuid;
	}

	public void setBackPointUuid(String backPointUuid) {
		this.backPointUuid = backPointUuid;
	}

	public String getStationUuid() {
		return stationUuid;
	}

	public void setStationUuid(String stationUuid) {
		this.stationUuid = stationUuid;
	}

	public String getBasePointUuid() {
		return basePointUuid;
	}

	public void setBasePointUuid(String basePointUuid) {
		this.basePointUuid = basePointUuid;
	}

	public String getBackPointName() {
		return backPointName;
	}

	public void setBackPointName(String backPointName) {
		this.backPointName = backPointName;
	}

	public double getBackPointEast() {
		return backPointEast;
	}

	public void setBackPointEast(double backPointEast) {
		this.backPointEast = backPointEast;
	}

	public double getBackPointNorth() {
		return backPointNorth;
	}

	public void setBackPointNorth(double backPointNorth) {
		this.backPointNorth = backPointNorth;
	}

	public double getBackPointHeight() {
		return backPointHeight;
	}

	public void setBackPointHeight(double backPointHeight) {
		this.backPointHeight = backPointHeight;
	}

	public double getBackPointSuyEast() {
		return backPointSuyEast;
	}

	public void setBackPointSuyEast(double backPointSuyEast) {
		this.backPointSuyEast = backPointSuyEast;
	}

	public double getBackPointSuyNorth() {
		return backPointSuyNorth;
	}

	public void setBackPointSuyNorth(double backPointSuyNorth) {
		this.backPointSuyNorth = backPointSuyNorth;
	}

	public double getBackPointSuyHeight() {
		return backPointSuyHeight;
	}

	public void setBackPointSuyHeight(double backPointSuyHeight) {
		this.backPointSuyHeight = backPointSuyHeight;
	}

	public double getBackPointHa() {
		return backPointHa;
	}

	public void setBackPointHa(double backPointHa) {
		this.backPointHa = backPointHa;
	}

	public double getBackPointVa() {
		return backPointVa;
	}

	public void setBackPointVa(double backPointVa) {
		this.backPointVa = backPointVa;
	}

	public double getBackPointSd() {
		return backPointSd;
	}

	public void setBackPointSd(double backPointSd) {
		this.backPointSd = backPointSd;
	}

	public double getBackPointPrismHeight() {
		return backPointPrismHeight;
	}

	public void setBackPointPrismHeight(double backPointPrismHeight) {
		this.backPointPrismHeight = backPointPrismHeight;
	}

	@Override
	public String toString() {
		return "MobileBackPoint [backPointUuid=" + backPointUuid + ", stationUuid=" + stationUuid + ", basePointUuid="
				+ basePointUuid + ", backPointName=" + backPointName + ", backPointEast=" + backPointEast
				+ ", backPointNorth=" + backPointNorth + ", backPointHeight=" + backPointHeight + ", backPointSuyEast="
				+ backPointSuyEast + ", backPointSuyNorth=" + backPointSuyNorth + ", backPointSuyHeight="
				+ backPointSuyHeight + ", backPointHa=" + backPointHa + ", backPointVa=" + backPointVa
				+ ", backPointSd=" + backPointSd + ", backPointPrismHeight=" + backPointPrismHeight + "]";
	}
	
	
}
