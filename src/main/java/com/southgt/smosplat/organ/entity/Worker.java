package com.southgt.smosplat.organ.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 人员实体类
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
@Table(name="worker")
public class Worker implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="worker_uuid",updatable=false,length=50)
	private String workerUuid;
	/**
	 * 人员名称
	 */
	@Column(name="worker_name",length=50)
	private String workerName;
	
	/**
	 * 手机号码
	 */
	@Column(name="phone",length=50)
	private String phone;
	
	/**
	 * 邮箱
	 */
	@Column(name="email",length=50)
	private String email;
	
	/**
	 * 上岗证号
	 */
	@Column(name="paper_id",length=50)
	private String paperID;
	
	/**
	 * 所属机构
	 * optional为false表示人员一定要属于一个机构
	 */
	@ManyToOne(optional=false)
	@JoinColumn(name="organ_uuid")
	private Organ organ;
	
	/**
	 * 性别
	 */
	@Column(name="sex")
	private String sex;
	
	/**
	 * 身份证号
	 */
	@Column(name="id_number")
	private String idNumber;
	
	/**
	 * 职务
	 */
	@Column(name="duty")
	private String duty;
	
	/**
	 * 职称
	 */
	@Column(name="job_title")
	private String jobTitle;
	
	/**
	 * 职称专业
	 */
	@Column(name="title_major")
	private String titleMajor;
	
	/**
	 * 检测鉴定工作资历(年)
	 */
	@Column(name="work_year")
	private String workYear;
	
	/**
	 * 何时/何校/何专业毕业
	 */
	@Column(name="graduation_info")
	private String graduationInfo;
	
	/**
	 * 学历
	 */
	@Column(name="education")
	private String education;
	
	/**
	 * 工作简历
	 */
	@Column(name="resume")
	private String resume;
	
	/**
	 * 注册类型
	 */
	@Column(name="registerd_type")
	private String registerdType; 
	
	/**
	 * 注册证书编号
	 */
	@Column(name="registerd_paper_id")
	private String registerdPaperId; 
	
	/**
	 * 执业专用章号
	 */
	@Column(name="character_id")
	private String characterId; 
	
	/**
	 * 人员类型（监测人/报告填写/报告审核人/报告批准人）
	 */
	@Column(name="worker_type")
    private String workerType;
	
	/**
	 * 对应的账号,optional为true表示一个worker可以没有一个account
	 */
	@OneToOne(cascade=CascadeType.ALL,optional=true)
	@JoinColumn(name="account_uuid")
	private Account account;

	public String getWorkerUuid() {
		return workerUuid;
	}

	public void setWorkerUuid(String workerUuid) {
		this.workerUuid = workerUuid;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaperID() {
		return paperID;
	}

	public void setPaperID(String paperID) {
		this.paperID = paperID;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getTitleMajor() {
		return titleMajor;
	}

	public void setTitleMajor(String titleMajor) {
		this.titleMajor = titleMajor;
	}

	public String getWorkYear() {
		return workYear;
	}

	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}

	public String getGraduationInfo() {
		return graduationInfo;
	}

	public void setGraduationInfo(String graduationInfo) {
		this.graduationInfo = graduationInfo;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public String getRegisterdType() {
		return registerdType;
	}

	public void setRegisterdType(String registerdType) {
		this.registerdType = registerdType;
	}

	public String getRegisterdPaperId() {
		return registerdPaperId;
	}

	public void setRegisterdPaperId(String registerdPaperId) {
		this.registerdPaperId = registerdPaperId;
	}

	public String getCharacterId() {
		return characterId;
	}

	public void setCharacterId(String characterId) {
		this.characterId = characterId;
	}

	public String getWorkerType() {
		return workerType;
	}

	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Worker [workerUuid=" + workerUuid + ", workerName=" + workerName + ", phone=" + phone + ", email="
				+ email + ", paperID=" + paperID + ", organ=" + organ + ",  sex=" + sex
				+ ", idNumber=" + idNumber + ", duty=" + duty + ", jobTitle=" + jobTitle + ", titleMajor=" + titleMajor
				+ ", workYear=" + workYear + ", graduationInfo=" + graduationInfo + ", education=" + education
				+ ", resume=" + resume + ", registerdType=" + registerdType + ", registerdPaperId=" + registerdPaperId
				+ ", characterId=" + characterId + ", workerType=" + workerType + ", account=" + account + "]";
	}

}
