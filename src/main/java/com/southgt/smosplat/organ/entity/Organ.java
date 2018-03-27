package com.southgt.smosplat.organ.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 机构实体类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="organ")
public class Organ implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="organ_uuid",nullable=false,unique=false,length=50)
	private String organUuid;
	
	/**
	 * 机构名称
	 */
	@Column(name="organ_name",nullable=false,length=50)
	private String organName;
	
	/**
	 * 机构联系人
	 */
	@Column(name="contact_worker_uuid")
	private String contactWorker;
	
	/**
	 * 监督员
	 */
	@Column(name="supervisor_uuid")
	private String supervisor;
	
	/**
	 * 设立时间
	 */
	@Column(name="established_time")
	private String establishedTime;
	
	/**
	 * 机构地址
	 */
	@Column(name="adress")
	private String adress;
	
	/**
	 * 机构电话
	 */
	@Column(name="telephone")
	private String telephone;
	
	/**
	 * 电子邮箱
	 */
	@Column(name="email")
	private String email;
	
	/**
	 * 邮政编码
	 */
	@Column(name="postcode")
	private String postcode;
	
	/**
	 * 组织机构代码
	 */
	@Column(name="organ_code")
	private String organCode;
	
	/**
	 * 检测资质证书编号
	 */
	@Column(name="detact_number")
	private String detactNumber;
	
	/**
	 * 计量认证证书编号
	 */
	@Column(name="metering_number")
	private String meteringNumber;
	
	/**
	 * 法定代表人
	 */
	@Column(name="representative")
	private String representative;
	
	/**
	 * 技术负责人
	 */
	@Column(name="tech_director")
	private String techDirector;

	public String getOrganUuid() {
		return organUuid;
	}

	public void setOrganUuid(String organUuid) {
		this.organUuid = organUuid;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getContactWorker() {
		return contactWorker;
	}

	public void setContactWorker(String contactWorker) {
		this.contactWorker = contactWorker;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getEstablishedTime() {
		return establishedTime;
	}

	public void setEstablishedTime(String establishedTime) {
		this.establishedTime = establishedTime;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getOrganCode() {
		return organCode;
	}

	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}

	public String getDetactNumber() {
		return detactNumber;
	}

	public void setDetactNumber(String detactNumber) {
		this.detactNumber = detactNumber;
	}

	public String getMeteringNumber() {
		return meteringNumber;
	}

	public void setMeteringNumber(String meteringNumber) {
		this.meteringNumber = meteringNumber;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getTechDirector() {
		return techDirector;
	}

	public void setTechDirector(String techDirector) {
		this.techDirector = techDirector;
	}

	@Override
	public String toString() {
		return "Organ [organUuid=" + organUuid + ", organName=" + organName + ", contactWorker=" + contactWorker
				+ ", supervisor=" + supervisor + ", establishedTime=" + establishedTime + ", adress=" + adress
				+ ", telephone=" + telephone + ", email=" + email + ", postcode=" + postcode + ", organCode="
				+ organCode + ", detactNumber=" + detactNumber + ", meteringNumber=" + meteringNumber
				+ ", representative=" + representative + ", techDirector=" + techDirector + "]";
	}
	
}
