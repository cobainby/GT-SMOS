package com.southgt.smosplat.organ.entity;

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
 * 角色实体类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月7日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="role_uuid",nullable=false,unique=false,length=50)
	private String roleUuid;
	
	/**
	 * 角色名称
	 */
	@Column(name="role_name",nullable=false,length=50)
	private String roleName;
	
	/**
	 * 角色标识，用唯一的一个数字代表该角色,0:超级管理员，1：机构管理员，2：普通账号，3：访客账号
	 */
	@Column(name="mark")
	private Byte mark;
	
	/**
	 * 拥有的权限集合
	 */
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY,mappedBy="role",cascade={CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval=true)
	private List<RoleRoleItem> roleRoleItems=new ArrayList<RoleRoleItem>();
	
	public String getRoleUuid() {
		return roleUuid;
	}

	public void setRoleUuid(String roleUuid) {
		this.roleUuid = roleUuid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Byte getMark() {
		return mark;
	}

	public void setMark(Byte mark) {
		this.mark = mark;
	}

	public List<RoleRoleItem> getRoleRoleItems() {
		return roleRoleItems;
	}

	public void setRoleRoleItems(List<RoleRoleItem> roleRoleItems) {
		this.roleRoleItems = roleRoleItems;
	}
	
}
