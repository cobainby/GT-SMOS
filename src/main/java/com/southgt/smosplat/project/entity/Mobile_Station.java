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
@Table(name="mobile_station")
public class Mobile_Station implements Serializable {

	private static final long serialVersionUID = 7364435486069750532L;

	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="stationid",unique=true,nullable=false,length=50)
	@JsonIgnore
	private String stationid;
	
	@Column(name = "station_uuid")
	private String stationUuid;
	
	/** 测站id */
	@Column(name = "monitor_item_uuid")
	private String monitorItemUuid;
	
	@Column(name = "project_uuid")
	private String projectUuid;
	
	@Column(name = "round_limit_uuid")
	private String roundLimitUuid;
			
	@Column(name = "ts_uuid")
	private String tsUuid;
	
	@Column(name = "ts_name")
	private String tsName;
	
	@Column(name = "station_name")
	private String stationName;
		
	@Column(name = "bp_name")
	private String bpName;
	
	@Column(name = "bp_e")
	private double bpE;
	
	@Column(name = "bp_n")
	private double bpN;
	
	@Column(name = "bp_h")
	private double bpH;
	
	@Column(name = "device_height")
	private double deviceHeight;
	
	@Column(name = "occ")
	private double occ;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "back_point_list")
	private String backPointList;
	
	@Column(name = "order_create")
	@JsonIgnore
	private int orderCreate;

	public String getStationid() {
		return stationid;
	}

	public void setStationid(String stationid) {
		this.stationid = stationid;
	}

	public int getOrderCreate() {
		return orderCreate;
	}

	public void setOrderCreate(int orderCreate) {
		this.orderCreate = orderCreate;
	}

	public String getStationUuid() {
		return stationUuid;
	}

	public void setStationUuid(String stationUuid) {
		this.stationUuid = stationUuid;
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

	public String getRoundLimitUuid() {
		return roundLimitUuid;
	}

	public void setRoundLimitUuid(String roundLimitUuid) {
		this.roundLimitUuid = roundLimitUuid;
	}

	public String getTsUuid() {
		return tsUuid;
	}

	public void setTsUuid(String tsUuid) {
		this.tsUuid = tsUuid;
	}

	public String getTsName() {
		return tsName;
	}

	public void setTsName(String tsName) {
		this.tsName = tsName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public double getBpE() {
		return bpE;
	}

	public void setBpE(double bpE) {
		this.bpE = bpE;
	}

	public double getBpN() {
		return bpN;
	}

	public void setBpN(double bpN) {
		this.bpN = bpN;
	}

	public double getBpH() {
		return bpH;
	}

	public void setBpH(double bpH) {
		this.bpH = bpH;
	}

	public double getDeviceHeight() {
		return deviceHeight;
	}

	public void setDeviceHeight(double deviceHeight) {
		this.deviceHeight = deviceHeight;
	}

	public double getOcc() {
		return occ;
	}

	public void setOcc(double occ) {
		this.occ = occ;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBackPointList() {
		return backPointList;
	}

	public void setBackPointList(String backPointList) {
		this.backPointList = backPointList;
	}

	@Override
	public String toString() {
		return "Mobile_Station [stationUuid=" + stationUuid + ", monitorItemUuid=" + monitorItemUuid + ", projectUuid="
				+ projectUuid + ", roundLimitUuid=" + roundLimitUuid + ", tsUuid=" + tsUuid + ", tsName=" + tsName
				+ ", stationName=" + stationName + ", bpName=" + bpName + ", bpE=" + bpE + ", bpN=" + bpN + ", bpH="
				+ bpH + ", deviceHeight=" + deviceHeight + ", occ=" + occ + ", remark=" + remark + ", backPointList="
				+ backPointList + "]";
	}
	
	
}
