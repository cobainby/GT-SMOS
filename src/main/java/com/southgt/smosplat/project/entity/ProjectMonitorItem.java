package com.southgt.smosplat.project.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 工程与监测项的关联关系实体
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
@Table(name="project_monitor_item")
public class ProjectMonitorItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="project_monitor_item_uuid",nullable=false,unique=false,length=50)
	private String projectMonitorItemUuid;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="project_uuid")
	private Project project;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="monitor_item_uuid")
	private MonitorItem monitorItem;
	
	public ProjectMonitorItem() {
		super();
	}

	public ProjectMonitorItem(Project project, MonitorItem monitorItem) {
		super();
		this.project = project;
		this.monitorItem = monitorItem;
	}

	public String getProjectMonitorItemUuid() {
		return projectMonitorItemUuid;
	}

	public void setProjectMonitorItemUuid(String projectMonitorItemUuid) {
		this.projectMonitorItemUuid = projectMonitorItemUuid;
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
		return "ProjectMonitorItem [projectMonitorItemUuid=" + projectMonitorItemUuid + ", project=" + project
				+ ", monitorItem=" + monitorItem + "]";
	}
	
	
}
