package com.southgt.smosplat.project.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.southgt.smosplat.organ.entity.Device;

@Entity
@Table(name="cable_meter")
public class CableMeter implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="cable_meter_Uuid",length=50,nullable=false,unique=true,updatable=false)
	private String cableMeterUuid;
	
	/**
	 * 所属监测点
	 */
	@ManyToOne
	@JoinColumn(name="surveyPointUuid")
	private SurveyPoint_MT sp_MT;
	
	/**
	 * 对应的设备
	 */
	@OneToOne
	@JoinColumn(name="device_uuid")
	private Device device;
	
	/**
	 * 所属项目
	 */
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="projectUuid")
	private Project project;
	
	public String getCableMeterUuid() {
		return cableMeterUuid;
	}

	public void setCableMeterUuid(String cableMeterUuid) {
		this.cableMeterUuid = cableMeterUuid;
	}

	public SurveyPoint_MT getSp_MT() {
		return sp_MT;
	}

	public void setSp_MT(SurveyPoint_MT sp_MT) {
		this.sp_MT = sp_MT;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "CableMeter [cableMeterUuid=" + cableMeterUuid + ", sp_MT=" + sp_MT + ", device=" + device + ", project="
				+ project + "]";
	}


}
