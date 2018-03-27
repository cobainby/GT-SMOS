package com.southgt.smosplat.project.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.southgt.smosplat.organ.entity.Device;

/**
 * mcu实体
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月13日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="mcu")
public class Mcu implements Serializable {

	private static final long serialVersionUID = 7364435486069750532L;

	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="mcu_uuid",unique=true,nullable=false,length=50)
	private String mcuUuid;
	
	/**
	 * 记录创建时间，用于排序
	 */
	@Column(name="create_time",updatable=false)
	private Timestamp createTime;
	
	/**
	 * 箱号
	 */
	@Column(name="sn")
	private String sn;
	
	/**
	 * 设备电压
	 */
	@Column(name="voltage")
	private Double voltage;
	
	/**
	 * 数据获取方式：自报模式、采集模式
	 */
	@Column(name="data_type")
	private Integer dataType;
	
	/**
	 * 网络连接
	 */
	@OneToOne
	@JoinColumn(name="network_uuid")
	private Network network;
	
	/**
	 * 所属设备
	 */
	@Column(name="ref_device_uuid",length=50)
	private String deviceUuid;
	
	/**
	 * 所属项目
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="project_uuid")
	private Project project;
	
	public String getMcuUuid() {
		return mcuUuid;
	}

	public void setMcuUuid(String mcuUuid) {
		this.mcuUuid = mcuUuid;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.createTime = new Timestamp(format.parse(format.format(createTime)).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}
	
	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "Mcu [mcuUuid=" + mcuUuid + ", createTime=" + createTime + ", sn=" + sn + ", voltage=" + voltage
				+ ", dataType=" + dataType + ", network=" + network + ", deviceUuid=" + deviceUuid + ", project="
				+ project + "]";
	}

}






