package com.southgt.smosplat.project.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 手机_基准点实体
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
@Table(name="mobile_base_point")
public class Mobile_BasePoint implements Serializable {

	private static final long serialVersionUID = 7364435486069750532L;

	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="bp_uuid",unique=true,nullable=false,length=50)
	@JsonIgnore
	private String bpUuid;
	
	@Column(name = "base_point_uuid")
	private String basePointUuid;
	
	@Column(name = "monitor_item_uuid")
	private String monitorItemUuid;
	
	@Column(name = "project_uuid")
	private String projectUuid;
	
	@Column(name = "base_point_name")
	private String basePointName;
	
	@Column(name = "east")
	private double east;
	
	@Column(name = "north")
	private double north;
	
	@Column(name = "height")
	private double height;		
		
	@Column(name = "remark")
	private String remark;
	
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

	public String getBasePointUuid() {
		return basePointUuid;
	}

	public void setBasePointUuid(String basePointUuid) {
		this.basePointUuid = basePointUuid;
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

	public String getBasePointName() {
		return basePointName;
	}

	public void setBasePointName(String basePointName) {
		this.basePointName = basePointName;
	}

	public double getEast() {
		return east;
	}

	public void setEast(double east) {
		this.east = east;
	}

	public double getNorth() {
		return north;
	}

	public void setNorth(double north) {
		this.north = north;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Mobile_BasePoint [basePointUuid=" + basePointUuid + ", monitorItemUuid=" + monitorItemUuid
				+ ", projectUuid=" + projectUuid + ", basePointName=" + basePointName + ", east=" + east + ", north="
				+ north + ", height=" + height + ", remark=" + remark + "]";
	}
	
	
	
}
