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
@Table(name="stress")
public class Stress implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="stress_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String stressUuid;
	
	/**
	 * 轴力名称
	 */
	@Column(name="name",length=50)
	private String name;
	
	/**
	 * 设备编号
	 */
	@Column(name="dev_code",length=50)
	private String devCode;
	
	/**
	 * 初始模数值
	 */
	@Column(name="start_mod_value")
	private Double startModValue;
	
	/**
	 * 初始温度值
	 */
	@Column(name="start_temperature")
	private Double startTemperature;
	
	/**
	 * 标定系数值
	 */
	@Column(name="calibrated_mod")
	private Double calibratedMod;
	
	/**
	 * K值
	 */
	@Column(name="prmk")
	private Double prmK;
	
	/**
	 * 计算值
	 */
	@Column(name="calculated_value")
	private Double calculatedValue;
	
	/**
	 * 弹性模量
	 */
	@Column(name="elastricity")
	private Double elastricity;
	
	/**
	 * 所属监测点
	 */
	@ManyToOne
	@JoinColumn(name="surveyPointUuid")
	private SurveyPoint_ZC sp_ZC;
	
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
	
	public String getStressUuid() {
		return stressUuid;
	}

	public void setStressUuid(String stressUuid) {
		this.stressUuid = stressUuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDevCode() {
		return devCode;
	}

	public Double getStartModValue() {
		return startModValue;
	}

	public void setStartModValue(Double startModValue) {
		this.startModValue = startModValue;
	}

	public Double getStartTemperature() {
		return startTemperature;
	}

	public void setStartTemperature(Double startTemperature) {
		this.startTemperature = startTemperature;
	}

	public Double getCalibratedMod() {
		return calibratedMod;
	}

	public void setCalibratedMod(Double calibratedMod) {
		this.calibratedMod = calibratedMod;
	}

	public Double getPrmK() {
		return prmK;
	}

	public void setPrmK(Double prmK) {
		this.prmK = prmK;
	}

	public Double getCalculatedValue() {
		return calculatedValue;
	}

	public void setCalculatedValue(Double calculatedValue) {
		this.calculatedValue = calculatedValue;
	}

	public Double getElastricity() {
		return elastricity;
	}

	public void setElastricity(Double elastricity) {
		this.elastricity = elastricity;
	}

	public SurveyPoint_ZC getSp_ZC() {
		return sp_ZC;
	}

	public void setSp_ZC(SurveyPoint_ZC sp_ZC) {
		this.sp_ZC = sp_ZC;
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
		return "Stress [stressUuid=" + stressUuid + ", name=" + name + ", devCode=" + devCode + ", startModValue="
				+ startModValue + ", startTemperature=" + startTemperature + ", calibratedMod=" + calibratedMod
				+ ", prmK=" + prmK + ", calculatedValue=" + calculatedValue + ", elastricity=" + elastricity
				+ ", sp_ZC=" + sp_ZC + ", device=" + device + ", project=" + project + "]";
	}

}
