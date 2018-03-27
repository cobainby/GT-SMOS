package com.southgt.smosplat.organ.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 设备型号实体
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月2日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="device_model")
public class DeviceModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="dev_model_uuid",nullable=false,length=50,unique=true)
	private String devModelUuid;
	
	/**
	 * 型号名称
	 */
	@Column(name="dev_model_name",nullable=false,length=50)
	private String devModelName;
	
	/**
	 * 所属设备类型
	 */
	@ManyToOne
	@JoinColumn(name="dev_type_uuid")
	private DeviceType deviceType;

	public String getDevModelUuid() {
		return devModelUuid;
	}

	public void setDevModelUuid(String devModelUuid) {
		this.devModelUuid = devModelUuid;
	}

	public String getDevModelName() {
		return devModelName;
	}

	public void setDevModelName(String devModelName) {
		this.devModelName = devModelName;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	@Override
	public String toString() {
		return "DeviceModel [devModelUuid=" + devModelUuid + ", devModelName=" + devModelName + ", deviceType="
				+ deviceType + "]";
	}
}
