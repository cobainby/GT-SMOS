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
@Table(name="role_item")
public class RoleItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="role_item_uuid",nullable=false,unique=false,length=50)
	private String roleItemUuid;

	/**
	 * 权限项名称
	 */
	@Column(name="role_item_name",nullable=false,length=50)
	private String roleItemName;
	
	/**
	 * 权限项序号
	 */
	@Column(name="number",nullable=false)
	private Integer number;
	
	/**
	 * 权限项描述
	 */
	@Column(name="role_item_desc",nullable=false,length=255)
	private String roleItemDesc;
	
	/**
	 * 拥有该权限的角色
	 */
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY,mappedBy="roleItem",cascade={CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval=true)
	private List<RoleRoleItem> roleRoleItems=new ArrayList<RoleRoleItem>();
	
	public String getRoleItemUuid() {
		return roleItemUuid;
	}

	public void setRoleItemUuid(String roleItemUuid) {
		this.roleItemUuid = roleItemUuid;
	}

	public String getRoleItemName() {
		return roleItemName;
	}

	public void setRoleItemName(String roleItemName) {
		this.roleItemName = roleItemName;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getRoleItemDesc() {
		return roleItemDesc;
	}

	public void setRoleItemDesc(String roleItemDesc) {
		this.roleItemDesc = roleItemDesc;
	}

	public List<RoleRoleItem> getRoleRoleItems() {
		return roleRoleItems;
	}

	public void setRoleRoleItems(List<RoleRoleItem> roleRoleItems) {
		this.roleRoleItems = roleRoleItems;
	}

	@Override
	public String toString() {
		return "RoleItem [roleItemUuid=" + roleItemUuid + ", roleItemName=" + roleItemName + ", number=" + number
				+ ", roleItemDesc=" + roleItemDesc + ", roleRoleItems=" + roleRoleItems + "]";
	}
}
