package com.southgt.smosplat.organ.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.southgt.smosplat.project.entity.Project;

/**
 * 账户与可查看工程之间的关联关系
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月8日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="account_project")
public class AccountProject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="account_project_uuid",nullable=false,unique=false,length=50)
	private String accountProjectUuid;

	@ManyToOne
	@JoinColumn(name="account_uuid")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name="project_uuid")
	private Project project;
	
	public AccountProject() {
		super();
	}

	public AccountProject(Account account, Project project) {
		super();
		this.account=account;
		this.project=project;
	}

	public String getAccountProjectUuid() {
		return accountProjectUuid;
	}

	public void setAccountProjectUuid(String accountProjectUuid) {
		this.accountProjectUuid = accountProjectUuid;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "AccountProject [accountProjectUuid=" + accountProjectUuid + ", account=" + account + ", project="
				+ project + "]";
	}
	
}
