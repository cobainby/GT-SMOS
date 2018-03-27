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

/**
 * 角色与权限之间的关联关系实体
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
@Table(name="role_role_item")
public class RoleRoleItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="role_role_item_uuid",nullable=false,unique=false,length=50)
	private String roleRoleItemUuid;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="role_uuid")
	private Role role;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="role_item_uuid")
	private RoleItem roleItem;
	
	public RoleRoleItem() {
		super();
	}

	public RoleRoleItem(Role role, RoleItem roleItem) {
		super();
		this.role = role;
		this.roleItem = roleItem;
	}

	public String getRoleRoleItemUuid() {
		return roleRoleItemUuid;
	}

	public void setRoleRoleItemUuid(String roleRoleItemUuid) {
		this.roleRoleItemUuid = roleRoleItemUuid;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public RoleItem getRoleItem() {
		return roleItem;
	}

	public void setRoleItem(RoleItem roleItem) {
		this.roleItem = roleItem;
	}

	@Override
	public String toString() {
		return "RoleRoleItem [roleRoleItemUuid=" + roleRoleItemUuid + ", role=" + role + ", roleItem=" + roleItem + "]";
	}
	
	
}
