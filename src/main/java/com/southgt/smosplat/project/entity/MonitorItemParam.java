package com.southgt.smosplat.project.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

/**
 * 监测项参数实体
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月21日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="monitor_item_param")
public class MonitorItemParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="monitor_item_param_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String monitorItemParamUuid;
	
	/**
	 * 记录创建时间
	 */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time",updatable=false)
	private Date createTime;
	
	/**
	 * 监测频率
	 */
	@Column(name="frequency")
	private Byte frequency;
	
	/**
	 * 所属工程,一个工程有很多监测项的设置
	 */
	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="project_uuid")
	private Project project;
	/**
	 * 所属监测项，一个监测项在不同的工程中也有不同的设置
	 */
	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="monitor_item_uuid")
	private MonitorItem monitorItem;
	public String getMonitorItemParamUuid() {
		return monitorItemParamUuid;
	}
	public void setMonitorItemParamUuid(String monitorItemParamUuid) {
		this.monitorItemParamUuid = monitorItemParamUuid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Byte getFrequency() {
		return frequency;
	}
	public void setFrequency(Byte frequency) {
		this.frequency = frequency;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public MonitorItem getMonitorItem() {
		return monitorItem;
	}
	public void setMonitorItem(MonitorItem monitorItem) {
		this.monitorItem = monitorItem;
	}
	@Override
	public String toString() {
		return "MonitorItemParam [monitorItemParamUuid=" + monitorItemParamUuid + ", createTime=" + createTime
				+ ", frequency=" + frequency + ", project=" + project + ", monitorItem=" + monitorItem + "]";
	}
	
}
