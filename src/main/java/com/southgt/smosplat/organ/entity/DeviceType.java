package com.southgt.smosplat.organ.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="device_type")
public class DeviceType implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="dev_type_uuid",nullable=false,length=50,unique=true)
	private String devTypeUuid;
	
	/**
	 * 类型名称
	 */
	@Column(name="dev_type_name",nullable=false,length=50)
	private String devTypeName;
	
	/**
	 * 类型名称英文标识
	 */
	@Column(name="dev_type_ename",nullable=false,length=50)
	private String devTypeEname;
	
	/**
	 * 是否是自动采集设备，0：是，-1：不是
	 */
	@Column(name="is_auto")
	private Byte isAuto;

	public String getDevTypeUuid() {
		return devTypeUuid;
	}

	public void setDevTypeUuid(String devTypeUuid) {
		this.devTypeUuid = devTypeUuid;
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}

	public String getDevTypeEname() {
		return devTypeEname;
	}

	public void setDevTypeEname(String devTypeEname) {
		this.devTypeEname = devTypeEname;
	}

	public Byte getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(Byte isAuto) {
		this.isAuto = isAuto;
	}

	@Override
	public String toString() {
		return "DeviceType [devTypeUuid=" + devTypeUuid + ", devTypeName=" + devTypeName + ", devTypeEname="
				+ devTypeEname + ", isAuto=" + isAuto + "]";
	}
	
}
