package com.southgt.smosplat.project.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 网络连接实体
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
@Table(name="network")
public class Network implements Serializable {

	private static final long serialVersionUID = 4756435754083880110L;

	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="network_uuid",unique=true,nullable=false,length=50)
	private String networkUuid;
	
	/**
	 * 记录创建时间，用于排序
	 */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time",updatable=false)
	private Date createTime;
	
	/**
	 * 网络连接名称
	 */
	@Column(name="network_name",length=50)
	private String networkName;
	
	/**
	 * ip
	 */
	@Column(name="ip",length=50)
	private String ip;
	
	/**
	 * 端口号
	 */
	@Column(name="port")
	private Integer port;
	
	/**
	 * 接入方式（服务端：0，客户端：1）
	 */
	@Column(name="type")
	private Integer type;
	
	/**
	 * 项目
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="project_uuid")
	private Project project;
	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNetworkUuid() {
		return networkUuid;
	}

	public void setNetworkUuid(String networkUuid) {
		this.networkUuid = networkUuid;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "Network [networkUuid=" + networkUuid + ", createTime=" + createTime + ", networkName=" + networkName
				+ ", ip=" + ip + ", port=" + port + ", type=" + type + ", project=" + project + "]";
	}

}
