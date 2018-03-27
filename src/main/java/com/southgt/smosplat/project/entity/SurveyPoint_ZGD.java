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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 周边管线竖向位移
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月21日     YANG       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="survey_point_zgd")
public class SurveyPoint_ZGD implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="survey_point_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String surveyPointUuid;
	
	/**
	 * 测点编号
	 */
	@Column(name="code",length=50,nullable=false)
	private String code;
	
	/**
	 * 测点编号字符部分，用来辅助添加点
	 */
	@Column(name="code_char",length=50,nullable=false)
	private String codeChar;
	
	/**
	 * 记录创建时间，用于排序
	 */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time",updatable=false)
	private Date createTime;
	
	/**
	 * 初始累计值
	 */
	@Column(name="original_total_value")
	private Float originalTotalValue;
	
	/**
	 * 报警设置名称
	 */
	@ManyToOne
	@JoinColumn(name="warning_uuid",nullable=false)
	private Warning warning;
	

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
	
	/**
	 * 处理了的数据的uuid，用以判断当前监测点的报警情况。
	 */
	@Column(name="processed_data_uuid",length=50,updatable=true)
	private String processedDataUuid;

	public String getProcessedDataUuid() {
		return processedDataUuid;
	}

	public void setProcessedDataUuid(String processedDataUuid) {
		this.processedDataUuid = processedDataUuid;
	}

	public String getSurveyPointUuid() {
		return surveyPointUuid;
	}

	public void setSurveyPointUuid(String surveyPointUuid) {
		this.surveyPointUuid = surveyPointUuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeChar() {
		return codeChar;
	}

	public void setCodeChar(String codeChar) {
		this.codeChar = codeChar;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Float getOriginalTotalValue() {
		return originalTotalValue;
	}

	public void setOriginalTotalValue(Float originalTotalValue) {
		this.originalTotalValue = originalTotalValue;
	}

	public Warning getWarning() {
		return warning;
	}

	public void setWarning(Warning warning) {
		this.warning = warning;
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
