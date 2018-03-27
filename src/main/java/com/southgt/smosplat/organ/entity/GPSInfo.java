package com.southgt.smosplat.organ.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年10月13日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="gps_info")
public class GPSInfo implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	*/
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="gps_info_uuid",unique=true,nullable=false,length=50)
	private String gpsInfoUuid;
	
	/**
	 * 上传时间
	 */
	@Column(name="time")
	private Timestamp time;
	
	/**
	 * 经度
	 */
	@Column(name="lon")
	private Double lon;
	
	/**
	 * 纬度
	 */
	@Column(name="lat")
	private Double lat;
	
	/**
	 * 是否在工地范围内,是：0，不是：-1
	 */
	@Column(name="within")
	private Integer within=0;
	
	/**
	 * 与其关联的账户
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="account_uuid")
	private Account account;
	
	/**
	 * 哪个工程下的uuid
	 */
	@Column(name="project_uuid")
	private String projectUuid;

	public String getGpsInfoUuid() {
		return gpsInfoUuid;
	}

	public void setGpsInfoUuid(String gpsInfoUuid) {
		this.gpsInfoUuid = gpsInfoUuid;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	public Integer getWithin() {
		return within;
	}

	public void setWithin(Integer within) {
		this.within = within;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public String getProjectUuid() {
		return projectUuid;
	}

	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;
	}

	@Override
	public String toString() {
		return "GPSInfo [gpsInfoUuid=" + gpsInfoUuid + ", time=" + time + ", lon=" + lon + ", lat=" + lat + ", within="
				+ within + ", account=" + account + ", projectUuid=" + projectUuid + "]";
	}

}
