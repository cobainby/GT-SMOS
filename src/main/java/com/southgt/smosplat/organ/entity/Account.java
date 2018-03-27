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
 * @since 2016.4.21
 * @modify 
 * @author mohaolin
 * 
 */
@Entity
@Table(name="account")
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="account_uuid",updatable=false,length=50)
	private String accountUuid;
	/**
	 * 登陆名
	 */
	@Column(name="login_name",length=50)
	private String loginName;
	/**
	 * 账户名
	 */
	@Column(name="account_name",length=50)
	private String accountName;
	/**
	 * 密码
	 */
	@Column(name="password",length=50)
	private String password;
	
	/**
	 * 创建人
	 */
	@Column(name="creator_account_uuid")
	private String creatorUuid;
	
	/**
	 * 关联的人员,mappedBy表示只在worker表中增加一列外键来维持关联关系，optional为false，表示一个account必须要有一个worker,用来防止一个account记录没有一个对应的worker记录
	 */
	@OneToOne(cascade=CascadeType.ALL,mappedBy="account",optional=false)
	@JoinColumn(name="worker_uuid")
	private Worker worker;
	
	/**
	 * 账户对应的角色，账户必须要具有一个角色，且在账户端维护关联关系，多个账户可以拥有同一个角色
	 */
	@ManyToOne(optional=false)
	@JoinColumn(name="role_uuid")
	private Role role;

	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreatorUuid() {
		return creatorUuid;
	}

	public void setCreatorUuid(String creatorUuid) {
		this.creatorUuid = creatorUuid;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Account [accountUuid=" + accountUuid + ", loginName=" + loginName + ", accountName=" + accountName
				+ ", password=" + password + ", creatorUuid=" + creatorUuid + ", worker=" + worker + ", role=" + role
				+ "]";
	}

}
