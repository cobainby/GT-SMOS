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
 * 
 * 现场图片实体类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年9月20日     姚家俊       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="site_pic")
public class SitePic implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="site_pic_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String sitePicUuid;
	
	/**
	 * 照片名字（上传时的毫秒值）
	 */
	@Column(name="pic_name",length=50)
	String picName;
	
	/**
	 * 照片描述
	 */
	@Column(name="description",length=150)
	String description;

	/**
	 * 所属项目
	 */
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="projectUuid")
	Project project;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getSitePicUuid() {
		return sitePicUuid;
	}

	public void setSitePicUuid(String sitePicUuid) {
		this.sitePicUuid = sitePicUuid;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "SitePic [sitePicUuid=" + sitePicUuid + ", picName=" + picName + ", description=" + description
				+ ", project=" + project + "]";
	}
	
}	

