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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.southgt.smosplat.organ.entity.Organ;

/**
 * 工程实体
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月19日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="project")
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="project_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String projectUuid;
	/**
	 * 工程名称
	 */
	@Column(name="project_name",length=100)
	private String projectName;
	/**
	 * 工程编号
	 */
	@Column(name="code",length=50)
	private String code;
	/**
	 * 工程地址
	 */
	@Column(name="address",length=255)
	private String address;
	/**
	 * 经度
	 */
	@Column(name="lon")
	private Float lon;
	/**
	 * 纬度
	 */
	@Column(name="lat")
	private Float lat;
	/**
	 * 项目类型
	 */
	@Column(name="project_type")
	private Byte projectType;
	/**
	 * 支护结构
	 */
	@Column(name="structure",length=50)
	private String structure;
	/**
	 * 基坑深度
	 */
	@Column(name="deep")
	private Float deep;
	/**
	 * 基坑周长
	 */
	@Column(name="perimeter")
	private Float perimeter;
	/**
	 * 监测负责人
	 */
	@Column(name="monitor_leader",length=2000)
	private String monitorLeader;
	/**
	 * 监测人员
	 */
	@Column(name="monitor_worker",length=2000)
	private String monitorWorker;
	/**
	 * 安全监督单位
	 */
	@Column(name="supervise_company",length=50)
	private String superviseCompany;
	/**
	 * 监督人员
	 */
	@Column(name="supervise_worker",length=50)
	private String superviseWorker;
	/**
	 * 安全等级
	 */
	@Column(name="safe_level",length=10)
	private String safeLevel;
	
	/**
	 * 采集状态
	 */
	@Column(name="collect_status")
	private Byte collectStatus=-1;
	
	/**
	 * 采集间隔
	 */
	@Column(name="collect_interval",length=5)
	private String collectInterval;
	
	/**
	 * 监督编号
	 */
	@Column(name="supervise_code",length=50)
	private String superviseCode;
	/**
	 * 建设单位
	 */
	@Column(name="build_company",length=50)
	private String buildCompany;
	/**
	 * 建设单位负责人
	 */
	@Column(name="build_leader_name",length=50)
	private String buildLeaderName;
	/**
	 * 建设单位负责人电话
	 */
	@Column(name="build_leader_phone",length=50)
	private String buildLeaderPhone;
	/**
	 * 建设单位联系人
	 */
	@Column(name="build_contact_name",length=50)
	private String buildContactName;
	/**
	 * 建设单位联系人电话
	 */
	@Column(name="build_contact_phone",length=50)
	private String buildContactPhone;
	/**
	 * 建设单位联系人1
	 */
	@Column(name="build_contact_name1",length=50)
	private String buildContactName1;
	/**
	 * 建设单位联系人电话人
	 */
	@Column(name="build_contact_phone1",length=50)
	private String buildContactPhone1;
	/**
	 * 设计单位
	 */
	@Column(name="design_company",length=50)
	private String designCompany;
	/**
	 * 设计单位负责人
	 */
	@Column(name="design_leader_name",length=50)
	private String designLeaderName;
	/**
	 * 设计单位负责人电话
	 */
	@Column(name="design_leader_phone",length=50)
	private String designLeaderPhone;
	/**
	 * 设计单位联系人
	 */
	@Column(name="design_contact_name",length=50)
	private String designContactName;
	/**
	 * 设计单位联系人电话
	 */
	@Column(name="design_contact_phone",length=50)
	private String designContactPhone;
	/**
	 * 设计单位联系人1
	 */
	@Column(name="design_contact_name1",length=50)
	private String designContactName1;
	/**
	 * 设计单位联系人电话1
	 */
	@Column(name="design_contact_phone1",length=50)
	private String designContactPhone1;
	/**
	 * 施工单位
	 */
	@Column(name="construct_company",length=50)
	private String constructCompany;
	/**
	 * 施工单位负责人
	 */
	@Column(name="construct_leader_name",length=50)
	private String constructLeaderName;
	/**
	 * 施工单位负责人电话
	 */
	@Column(name="construct_leader_phone",length=50)
	private String constructLeaderPhone;
	/**
	 * 施工单位联系人
	 */
	@Column(name="construct_contact_name",length=50)
	private String constructContactName;
	/**
	 * 施工单位联系人电话
	 */
	@Column(name="construct_contact_phone",length=50)
	private String constructContactPhone;
	/**
	 * 施工单位联系人1
	 */
	@Column(name="construct_contact_name1",length=50)
	private String constructContactName1;
	/**
	 * 施工单位联系人电话1
	 */
	@Column(name="construct_contact_phone1",length=50)
	private String constructContactPhone1;
	/**
	 * 监理单位
	 */
	@Column(name="supervisor_company",length=50)
	private String supervisorCompany;
	/**
	 * 监理单位负责人
	 */
	@Column(name="supervisor_leader_name",length=50)
	private String supervisorLeaderName;
	/**
	 * 监理单位负责人电话
	 */
	@Column(name="supervisor_leader_phone",length=50)
	private String supervisorLeaderPhone;
	/**
	 * 监理单位联系人
	 */
	@Column(name="supervisor_contact_name",length=50)
	private String supervisorContactName;
	/**
	 * 监理单位联系人电话
	 */
	@Column(name="supervisor_contact_phone",length=50)
	private String supervisorContactPhone;
	/**
	 * 监理单位联系人1
	 */
	@Column(name="supervisor_contact_name1",length=50)
	private String supervisorContactName1;
	/**
	 * 监理单位联系人电话1
	 */
	@Column(name="supervisor_contact_phone1",length=50)
	private String supervisorContactPhone1;
	
	/**
	 * 是否可用，如果该工程被删除了，设为-1，否则为0表示可用
	 */
	@Column(name="available")
	private Integer available=0;
	
	
	@ManyToOne(optional=false)
	@JoinColumn(name="organ_uuid")
	private Organ organ;
	
	/**
	 * 工程的所有监测项
	 */
	//@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY,mappedBy="project",cascade={CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval=true)
	private List<ProjectMonitorItem> projectMonitorItems=new ArrayList<ProjectMonitorItem>();
	
	/**
	 * 项目报警状态(0:正常，1:报警超限，2:控制超限)
	 */
	@Column(name="warning_satus",length=50)
	private String warningStatus="0";
	
	public String getProjectUuid() {
		return projectUuid;
	}

	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Byte getProjectType() {
		return projectType;
	}

	public void setProjectType(Byte projectType) {
		this.projectType = projectType;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public Float getDeep() {
		return deep;
	}

	public void setDeep(Float deep) {
		this.deep = deep;
	}

	public Float getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(Float perimeter) {
		this.perimeter = perimeter;
	}

	public String getMonitorLeader() {
		return monitorLeader;
	}

	public void setMonitorLeader(String monitorLeader) {
		this.monitorLeader = monitorLeader;
	}

	public String getMonitorWorker() {
		return monitorWorker;
	}

	public void setMonitorWorker(String monitorWorker) {
		this.monitorWorker = monitorWorker;
	}

	public String getSuperviseCompany() {
		return superviseCompany;
	}

	public void setSuperviseCompany(String superviseCompany) {
		this.superviseCompany = superviseCompany;
	}

	public String getSuperviseWorker() {
		return superviseWorker;
	}

	public void setSuperviseWorker(String superviseWorker) {
		this.superviseWorker = superviseWorker;
	}

	public String getSafeLevel() {
		return safeLevel;
	}

	public void setSafeLevel(String safeLevel) {
		this.safeLevel = safeLevel;
	}

	public String getSuperviseCode() {
		return superviseCode;
	}

	public void setSuperviseCode(String superviseCode) {
		this.superviseCode = superviseCode;
	}

	public String getBuildCompany() {
		return buildCompany;
	}

	public void setBuildCompany(String buildCompany) {
		this.buildCompany = buildCompany;
	}

	public String getBuildLeaderName() {
		return buildLeaderName;
	}

	public void setBuildLeaderName(String buildLeaderName) {
		this.buildLeaderName = buildLeaderName;
	}

	public String getBuildLeaderPhone() {
		return buildLeaderPhone;
	}

	public void setBuildLeaderPhone(String buildLeaderPhone) {
		this.buildLeaderPhone = buildLeaderPhone;
	}

	public String getBuildContactName() {
		return buildContactName;
	}

	public void setBuildContactName(String buildContactName) {
		this.buildContactName = buildContactName;
	}

	public String getBuildContactPhone() {
		return buildContactPhone;
	}

	public void setBuildContactPhone(String buildContactPhone) {
		this.buildContactPhone = buildContactPhone;
	}

	public String getBuildContactName1() {
		return buildContactName1;
	}

	public void setBuildContactName1(String buildContactName1) {
		this.buildContactName1 = buildContactName1;
	}

	public String getBuildContactPhone1() {
		return buildContactPhone1;
	}

	public void setBuildContactPhone1(String buildContactPhone1) {
		this.buildContactPhone1 = buildContactPhone1;
	}

	public String getDesignCompany() {
		return designCompany;
	}

	public void setDesignCompany(String designCompany) {
		this.designCompany = designCompany;
	}

	public String getDesignLeaderName() {
		return designLeaderName;
	}

	public void setDesignLeaderName(String designLeaderName) {
		this.designLeaderName = designLeaderName;
	}

	public String getDesignLeaderPhone() {
		return designLeaderPhone;
	}

	public void setDesignLeaderPhone(String designLeaderPhone) {
		this.designLeaderPhone = designLeaderPhone;
	}

	public String getDesignContactName() {
		return designContactName;
	}

	public void setDesignContactName(String designContactName) {
		this.designContactName = designContactName;
	}

	public String getDesignContactPhone() {
		return designContactPhone;
	}

	public void setDesignContactPhone(String designContactPhone) {
		this.designContactPhone = designContactPhone;
	}

	public String getDesignContactName1() {
		return designContactName1;
	}

	public void setDesignContactName1(String designContactName1) {
		this.designContactName1 = designContactName1;
	}

	public String getDesignContactPhone1() {
		return designContactPhone1;
	}

	public void setDesignContactPhone1(String designContactPhone1) {
		this.designContactPhone1 = designContactPhone1;
	}

	public String getConstructCompany() {
		return constructCompany;
	}

	public void setConstructCompany(String constructCompany) {
		this.constructCompany = constructCompany;
	}

	public String getConstructLeaderName() {
		return constructLeaderName;
	}

	public void setConstructLeaderName(String constructLeaderName) {
		this.constructLeaderName = constructLeaderName;
	}

	public String getConstructLeaderPhone() {
		return constructLeaderPhone;
	}

	public void setConstructLeaderPhone(String constructLeaderPhone) {
		this.constructLeaderPhone = constructLeaderPhone;
	}

	public String getConstructContactName() {
		return constructContactName;
	}

	public void setConstructContactName(String constructContactName) {
		this.constructContactName = constructContactName;
	}

	public String getConstructContactPhone() {
		return constructContactPhone;
	}

	public void setConstructContactPhone(String constructContactPhone) {
		this.constructContactPhone = constructContactPhone;
	}

	public String getConstructContactName1() {
		return constructContactName1;
	}

	public void setConstructContactName1(String constructContactName1) {
		this.constructContactName1 = constructContactName1;
	}

	public String getConstructContactPhone1() {
		return constructContactPhone1;
	}

	public void setConstructContactPhone1(String constructContactPhone1) {
		this.constructContactPhone1 = constructContactPhone1;
	}

	public String getSupervisorCompany() {
		return supervisorCompany;
	}

	public void setSupervisorCompany(String supervisorCompany) {
		this.supervisorCompany = supervisorCompany;
	}

	public String getSupervisorLeaderName() {
		return supervisorLeaderName;
	}

	public void setSupervisorLeaderName(String supervisorLeaderName) {
		this.supervisorLeaderName = supervisorLeaderName;
	}

	public String getSupervisorLeaderPhone() {
		return supervisorLeaderPhone;
	}

	public void setSupervisorLeaderPhone(String supervisorLeaderPhone) {
		this.supervisorLeaderPhone = supervisorLeaderPhone;
	}

	public String getSupervisorContactName() {
		return supervisorContactName;
	}

	public void setSupervisorContactName(String supervisorContactName) {
		this.supervisorContactName = supervisorContactName;
	}

	public String getSupervisorContactPhone() {
		return supervisorContactPhone;
	}

	public void setSupervisorContactPhone(String supervisorContactPhone) {
		this.supervisorContactPhone = supervisorContactPhone;
	}

	public String getSupervisorContactName1() {
		return supervisorContactName1;
	}

	public void setSupervisorContactName1(String supervisorContactName1) {
		this.supervisorContactName1 = supervisorContactName1;
	}

	public String getSupervisorContactPhone1() {
		return supervisorContactPhone1;
	}

	public void setSupervisorContactPhone1(String supervisorContactPhone1) {
		this.supervisorContactPhone1 = supervisorContactPhone1;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public List<ProjectMonitorItem> getProjectMonitorItems() {
		return projectMonitorItems;
	}

	public void setProjectMonitorItems(List<ProjectMonitorItem> projectMonitorItems) {
		this.projectMonitorItems = projectMonitorItems;
	}

	public Byte getCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(Byte collectStatus) {
		this.collectStatus = collectStatus;
	}

	public String getCollectInterval() {
		return collectInterval;
	}

	public void setCollectInterval(String collectInterval) {
		this.collectInterval = collectInterval;
	}

	public String getWarningStatus() {
		return warningStatus;
	}

	public void setWarningStatus(String warningStatus) {
		this.warningStatus = warningStatus;
	}
	
	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "Project [projectUuid=" + projectUuid + ", projectName=" + projectName + ", code=" + code + ", address="
				+ address + ", lon=" + lon + ", lat=" + lat + ", projectType=" + projectType + ", structure="
				+ structure + ", deep=" + deep + ", perimeter=" + perimeter + ", monitorLeader=" + monitorLeader
				+ ", monitorWorker=" + monitorWorker + ", superviseCompany=" + superviseCompany + ", superviseWorker="
				+ superviseWorker + ", safeLevel=" + safeLevel + ", collectStatus=" + collectStatus
				+ ", collectInterval=" + collectInterval + ", superviseCode=" + superviseCode + ", buildCompany="
				+ buildCompany + ", buildLeaderName=" + buildLeaderName + ", buildLeaderPhone=" + buildLeaderPhone
				+ ", buildContactName=" + buildContactName + ", buildContactPhone=" + buildContactPhone
				+ ", buildContactName1=" + buildContactName1 + ", buildContactPhone1=" + buildContactPhone1
				+ ", designCompany=" + designCompany + ", designLeaderName=" + designLeaderName + ", designLeaderPhone="
				+ designLeaderPhone + ", designContactName=" + designContactName + ", designContactPhone="
				+ designContactPhone + ", designContactName1=" + designContactName1 + ", designContactPhone1="
				+ designContactPhone1 + ", constructCompany=" + constructCompany + ", constructLeaderName="
				+ constructLeaderName + ", constructLeaderPhone=" + constructLeaderPhone + ", constructContactName="
				+ constructContactName + ", constructContactPhone=" + constructContactPhone + ", constructContactName1="
				+ constructContactName1 + ", constructContactPhone1=" + constructContactPhone1 + ", supervisorCompany="
				+ supervisorCompany + ", supervisorLeaderName=" + supervisorLeaderName + ", supervisorLeaderPhone="
				+ supervisorLeaderPhone + ", supervisorContactName=" + supervisorContactName
				+ ", supervisorContactPhone=" + supervisorContactPhone + ", supervisorContactName1="
				+ supervisorContactName1 + ", supervisorContactPhone1=" + supervisorContactPhone1 + ", available="
				+ available + ", organ=" + organ + ", projectMonitorItems=" + projectMonitorItems + ", warningStatus="
				+ warningStatus + "]";
	}

	
}
