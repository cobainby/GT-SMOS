package com.southgt.smosplat.project.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 监测项实体
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
@Table(name="monitor_item")
public class MonitorItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="monitor_item_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String monitorItemUuid;
	
	/**
	 * 监测项名称
	 */
	@Column(name="monitor_item_name",length=50)
	private String monitorItemName;
	
	/**
	 * 监测项编号
	 */
	@Column(name="number")
	private Byte number=-1;
	
	/**
	 * 编码格式
	 */
	@Column(name="code",length=50)
	private String code;
	
	/**
	 * 是否有断面参数设置,0：有，-1：没有
	 */
	@Column(name="has_section_setting")
	private Byte hasSectionSetting=(byte)-1;
	
	/**
	 * 是否有自动化采集设置，0：有，-1：没有
	 */
	@Column(name="has_auto_setting")
	private Byte hasAutoSetting=(byte)-1;
	
	/**
	 * 使用到该监测项的所有工程
	 */
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY,mappedBy="monitorItem",cascade={CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval=true)
	private List<ProjectMonitorItem> projectMonitorItems=new ArrayList<ProjectMonitorItem>();

	public String getMonitorItemUuid() {
		return monitorItemUuid;
	}

	public void setMonitorItemUuid(String monitorItemUuid) {
		this.monitorItemUuid = monitorItemUuid;
	}

	public String getMonitorItemName() {
		return monitorItemName;
	}

	public void setMonitorItemName(String monitorItemName) {
		this.monitorItemName = monitorItemName;
	}

	public Byte getNumber() {
		return number;
	}

	public void setNumber(Byte number) {
		this.number = number;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ProjectMonitorItem> getProjectMonitorItems() {
		return projectMonitorItems;
	}

	public void setProjectMonitorItems(List<ProjectMonitorItem> projectMonitorItems) {
		this.projectMonitorItems = projectMonitorItems;
	}

	public Byte getHasSectionSetting() {
		return hasSectionSetting;
	}

	public void setHasSectionSetting(Byte hasSectionSetting) {
		this.hasSectionSetting = hasSectionSetting;
	}

	public Byte getHasAutoSetting() {
		return hasAutoSetting;
	}

	public void setHasAutoSetting(Byte hasAutoSetting) {
		this.hasAutoSetting = hasAutoSetting;
	}

	@Override
	public String toString() {
		return "MonitorItem [monitorItemUuid=" + monitorItemUuid + ", monitorItemName=" + monitorItemName + ", number="
				+ number + ", code=" + code + ", hasSectionSetting=" + hasSectionSetting + ", hasAutoSetting="
				+ hasAutoSetting + ", projectMonitorItems=" + projectMonitorItems + "]";
	}
	
}
