package com.southgt.smosplat.project.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 监测点实体定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月29日     mohaolin       v1.0.0        create</p>
 *
 */
@Entity
@Table(name="survey_point")
public class SurveyPoint implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")
	@Column(name="survey_point_uuid",length=50,nullable=false,unique=true,updatable=false)
	private String surveyPointUuid;
	
	/**
	 * 测点编号
	 */
	@Column(name="code",length=50,nullable=false)
	private String code;
	
	/**
	 * 测点编号字符部分，用来辅助添加点
	 */
	@Column(name="code_char",length=50,nullable=false)
	private String codeChar;
	
	/**
	 * 记录创建时间，用于排序
	 */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time",updatable=false)
	private Date createTime;
	
	/**
	 * 报警设置名称
	 */
	@ManyToOne
	@JoinColumn(name="warning_uuid",nullable=false)
	private Warning warning;
	
	/**
	 * 初始累计值
	 */
	@Column(name="original_total_value")
	private Float originalTotalValue;
	
	/**
	 * 虚拟断面
	 */
	@ManyToOne
	@JoinColumn(name="section_uuid")
	private Section section;
	
	/**
	 * 测点深度
	 */
	@Column(name="deep")
	private Float deep;
	
	/**
	 * 标定常数
	 */
	@Column(name="constant")
	private Float constant;
	
	/**
	 * 仪器初始模数
	 */
	@Column(name="original_module")
	private Float originalModule;
	
	/**
	 * 标定系数calibration coefficient
	 */
	@Column(name="cc")
	private Float cc;
	
	/**
	 * 仪器编号
	 */
	@Column(name="device_code",length=50)
	private String deviceCode;
	
	/**
	 * 监测元件编号
	 */
	@Column(name="monitor_element_code",length=50)
	private String monitorElementCode;
	
	/**
	 * 频率
	 */
	@Column(name="frequency")
	private Float frequency;
	
	/**
	 * 设备类型
	 */
	@Column(name="device_type",length=50)
	private String deviceType;
	
	/**
	 * 测点使用的mcu
	 */
	@ManyToOne
	@JoinColumn(name="mcu_uuid")
	private Mcu mcu; 
	
	/**
	 * 测点使用的mcu的模块号
	 */
	@Column(name="module_num")
	private Byte moduleNum;
	
	/**
	 * 测点使用的mcu的通道编号
	 */
	@Column(name="channel_num")
	private Byte channelNum;
	
	/**
	 * 设备编号1
	 */
	@Column(name="device_code_1",length=50)
	private String deviceCode1;
	
	/**
	 * 频率1
	 */
	@Column(name="frequency1")
	private Float frequency1;
	
	/**
	 * 标定系数1 calibration coefficient
	 */
	@Column(name="cc1")
	private Float cc1;
	
	/**
	 * 设备编号2
	 */
	@Column(name="device_code_2",length=50)
	private String deviceCode2;
	
	/**
	 * 频率2
	 */
	@Column(name="frequency2")
	private Float frequency2;
	
	/**
	 * 标定系数2 calibration coefficient
	 */
	@Column(name="cc2")
	private Float cc2;
	
	/**
	 * 设备编号3
	 */
	@Column(name="device_code_3",length=50)
	private String deviceCode3;
	
	/**
	 * 频率3
	 */
	@Column(name="frequency3")
	private Float frequency3;
	
	/**
	 * 标定系数3 calibration coefficient
	 */
	@Column(name="cc3")
	private Float cc3;
	
	/**
	 * 设备编号4
	 */
	@Column(name="device_code_4",length=50)
	private String deviceCode4;
	
	/**
	 * 频率4
	 */
	@Column(name="frequency4")
	private Float frequency4;
	
	/**
	 * 标定系数4 calibration coefficient
	 */
	@Column(name="cc4")
	private Float cc4;
	
	/**
	 * 设备编号5
	 */
	@Column(name="device_code_5",length=50)
	private String deviceCode5;
	
	/**
	 * 频率5
	 */
	@Column(name="frequency5")
	private Float frequency5;
	
	/**
	 * 标定系数5 calibration coefficient
	 */
	@Column(name="cc5")
	private Float cc5;
	
	/**
	 * 设备编号6
	 */
	@Column(name="device_code_6",length=50)
	private String deviceCode6;
	
	/**
	 * 频率6
	 */
	@Column(name="frequency6")
	private Float frequency6;
	
	/**
	 * 标定系数6 calibration coefficient
	 */
	@Column(name="cc6")
	private Float cc6;
	
	/**
	 * 钢筋面积
	 */
	@Column(name="steel_area")
	private Float steelArea;
	
	/**
	 * (支撑)截面面积
	 */
	@Column(name="section_area")
	private Float sectionArea;
	
	/**
	 * 混凝土弹性模量 elastic modulus of concrete
	 */
	@Column(name="ec")
	private Float ec;
	
	/**
	 * 钢筋弹性模量 elastic modulus of steel
	 */
	@Column(name="es")
	private Float es;
	
	/**
	 * 所属监测项
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="monitor_item_uuid")
	private MonitorItem monitorItem;
	
	/**
	 * 所属项目
	 */
	@JsonIgnore
	@ManyToOne(optional=false)
	@JoinColumn(name="project_uuid")
	private Project project;

	public String getSurveyPointUuid() {
		return surveyPointUuid;
	}

	public void setSurveyPointUuid(String surveyPointUuid) {
		this.surveyPointUuid = surveyPointUuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeChar() {
		return codeChar;
	}

	public void setCodeChar(String codeChar) {
		this.codeChar = codeChar;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Warning getWarning() {
		return warning;
	}

	public void setWarning(Warning warning) {
		this.warning = warning;
	}

	public Float getOriginalTotalValue() {
		return originalTotalValue;
	}

	public void setOriginalTotalValue(Float originalTotalValue) {
		this.originalTotalValue = originalTotalValue;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Float getDeep() {
		return deep;
	}

	public void setDeep(Float deep) {
		this.deep = deep;
	}

	public Float getConstant() {
		return constant;
	}

	public void setConstant(Float constant) {
		this.constant = constant;
	}

	public Float getOriginalModule() {
		return originalModule;
	}

	public void setOriginalModule(Float originalModule) {
		this.originalModule = originalModule;
	}

	public Float getCc() {
		return cc;
	}

	public void setCc(Float cc) {
		this.cc = cc;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getMonitorElementCode() {
		return monitorElementCode;
	}

	public void setMonitorElementCode(String monitorElementCode) {
		this.monitorElementCode = monitorElementCode;
	}

	public Float getFrequency() {
		return frequency;
	}

	public void setFrequency(Float frequency) {
		this.frequency = frequency;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public Mcu getMcu() {
		return mcu;
	}

	public void setMcu(Mcu mcu) {
		this.mcu = mcu;
	}

	public Byte getModuleNum() {
		return moduleNum;
	}

	public void setModuleNum(Byte moduleNum) {
		this.moduleNum = moduleNum;
	}

	public Byte getChannelNum() {
		return channelNum;
	}

	public void setChannelNum(Byte channelNum) {
		this.channelNum = channelNum;
	}

	public String getDeviceCode1() {
		return deviceCode1;
	}

	public void setDeviceCode1(String deviceCode1) {
		this.deviceCode1 = deviceCode1;
	}

	public Float getFrequency1() {
		return frequency1;
	}

	public void setFrequency1(Float frequency1) {
		this.frequency1 = frequency1;
	}

	public Float getCc1() {
		return cc1;
	}

	public void setCc1(Float cc1) {
		this.cc1 = cc1;
	}

	public String getDeviceCode2() {
		return deviceCode2;
	}

	public void setDeviceCode2(String deviceCode2) {
		this.deviceCode2 = deviceCode2;
	}

	public Float getFrequency2() {
		return frequency2;
	}

	public void setFrequency2(Float frequency2) {
		this.frequency2 = frequency2;
	}

	public Float getCc2() {
		return cc2;
	}

	public void setCc2(Float cc2) {
		this.cc2 = cc2;
	}

	public String getDeviceCode3() {
		return deviceCode3;
	}

	public void setDeviceCode3(String deviceCode3) {
		this.deviceCode3 = deviceCode3;
	}

	public Float getFrequency3() {
		return frequency3;
	}

	public void setFrequency3(Float frequency3) {
		this.frequency3 = frequency3;
	}

	public Float getCc3() {
		return cc3;
	}

	public void setCc3(Float cc3) {
		this.cc3 = cc3;
	}

	public String getDeviceCode4() {
		return deviceCode4;
	}

	public void setDeviceCode4(String deviceCode4) {
		this.deviceCode4 = deviceCode4;
	}

	public Float getFrequency4() {
		return frequency4;
	}

	public void setFrequency4(Float frequency4) {
		this.frequency4 = frequency4;
	}

	public Float getCc4() {
		return cc4;
	}

	public void setCc4(Float cc4) {
		this.cc4 = cc4;
	}

	public String getDeviceCode5() {
		return deviceCode5;
	}

	public void setDeviceCode5(String deviceCode5) {
		this.deviceCode5 = deviceCode5;
	}

	public Float getFrequency5() {
		return frequency5;
	}

	public void setFrequency5(Float frequency5) {
		this.frequency5 = frequency5;
	}

	public Float getCc5() {
		return cc5;
	}

	public void setCc5(Float cc5) {
		this.cc5 = cc5;
	}

	public String getDeviceCode6() {
		return deviceCode6;
	}

	public void setDeviceCode6(String deviceCode6) {
		this.deviceCode6 = deviceCode6;
	}

	public Float getFrequency6() {
		return frequency6;
	}

	public void setFrequency6(Float frequency6) {
		this.frequency6 = frequency6;
	}

	public Float getCc6() {
		return cc6;
	}

	public void setCc6(Float cc6) {
		this.cc6 = cc6;
	}

	public Float getSteelArea() {
		return steelArea;
	}

	public void setSteelArea(Float steelArea) {
		this.steelArea = steelArea;
	}

	public Float getSectionArea() {
		return sectionArea;
	}

	public void setSectionArea(Float sectionArea) {
		this.sectionArea = sectionArea;
	}

	public Float getEc() {
		return ec;
	}

	public void setEc(Float ec) {
		this.ec = ec;
	}

	public Float getEs() {
		return es;
	}

	public void setEs(Float es) {
		this.es = es;
	}

	public MonitorItem getMonitorItem() {
		return monitorItem;
	}

	public void setMonitorItem(MonitorItem monitorItem) {
		this.monitorItem = monitorItem;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "SurveyPoint [surveyPointUuid=" + surveyPointUuid + ", code=" + code + ", codeChar=" + codeChar
				+ ", createTime=" + createTime + ", warning=" + warning + ", originalTotalValue=" + originalTotalValue
				+ ", section=" + section + ", deep=" + deep + ", constant=" + constant + ", originalModule="
				+ originalModule + ", cc=" + cc + ", deviceCode=" + deviceCode + ", monitorElementCode="
				+ monitorElementCode + ", frequency=" + frequency + ", deviceType=" + deviceType + ", mcu=" + mcu
				+ ", moduleNum=" + moduleNum + ", channelNum=" + channelNum + ", deviceCode1=" + deviceCode1
				+ ", frequency1=" + frequency1 + ", cc1=" + cc1 + ", deviceCode2=" + deviceCode2 + ", frequency2="
				+ frequency2 + ", cc2=" + cc2 + ", deviceCode3=" + deviceCode3 + ", frequency3=" + frequency3 + ", cc3="
				+ cc3 + ", deviceCode4=" + deviceCode4 + ", frequency4=" + frequency4 + ", cc4=" + cc4
				+ ", deviceCode5=" + deviceCode5 + ", frequency5=" + frequency5 + ", cc5=" + cc5 + ", deviceCode6="
				+ deviceCode6 + ", frequency6=" + frequency6 + ", cc6=" + cc6 + ", steelArea=" + steelArea
				+ ", sectionArea=" + sectionArea + ", ec=" + ec + ", es=" + es + ", monitorItem=" + monitorItem
				+ ", project=" + project + "]";
	}
	
	
}
