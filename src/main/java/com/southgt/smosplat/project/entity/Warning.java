package com.southgt.smosplat.project.entity;

import java.io.Serializable;

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
 * 预警参数实体定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月27日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="warning")
public class Warning implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="warning_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String warningUuid;

	/**
	 * 报警名称
	 */
	@Column(name="warning_name",length=50)
	private String warningName;
	/**
	 * 单次变化率预警值
	 */
	@Column(name="early_single_rate")
	private Float earlySingleRate;
	/**
	 * 单次变化率报警值
	 */
	@Column(name="warn_single_rate")
	private Float warnSingleRate;
	/**
	 * 单次变化率控制值
	 */
	@Column(name="control_single_rate")
	private Float controlSingleRate;
	/**
	 * 累计变化量预警值
	 */
	@Column(name="early_accum")
	private Float earlyAccum;
	/**
	 * 累计变化量报警值
	 */
	@Column(name="warn_accum")
	private Float warnAccum;
	/**
	 * 累计变化量控制值
	 */
	@Column(name="control_accum")
	private Float controlAccum;
	
	/**
	 * 所属监测项
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="monitor_item_uuid")
	private MonitorItem monitorItem;
	
	/**
	 * 所属项目
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="project_uuid")
	private Project project;

	public String getWarningUuid() {
		return warningUuid;
	}

	public void setWarningUuid(String warningUuid) {
		this.warningUuid = warningUuid;
	}

	public String getWarningName() {
		return warningName;
	}

	public void setWarningName(String warningName) {
		this.warningName = warningName;
	}

	public Float getEarlySingleRate() {
		return earlySingleRate;
	}

	public void setEarlySingleRate(Float earlySingleRate) {
		this.earlySingleRate = earlySingleRate;
	}

	public Float getWarnSingleRate() {
		return warnSingleRate;
	}

	public void setWarnSingleRate(Float warnSingleRate) {
		this.warnSingleRate = warnSingleRate;
	}

	public Float getControlSingleRate() {
		return controlSingleRate;
	}

	public void setControlSingleRate(Float controlSingleRate) {
		this.controlSingleRate = controlSingleRate;
	}

	public Float getEarlyAccum() {
		return earlyAccum;
	}

	public void setEarlyAccum(Float earlyAccum) {
		this.earlyAccum = earlyAccum;
	}

	public Float getWarnAccum() {
		return warnAccum;
	}

	public void setWarnAccum(Float warnAccum) {
		this.warnAccum = warnAccum;
	}

	public Float getControlAccum() {
		return controlAccum;
	}

	public void setControlAccum(Float controlAccum) {
		this.controlAccum = controlAccum;
	}

	public MonitorItem getMonitorItem() {
		return monitorItem;
	}

	public void setMonitorItem(MonitorItem monitorItem) {
		this.monitorItem = monitorItem;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	
}
