package com.southgt.smosplat.organ.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.southgt.smosplat.project.entity.Mcu;

@Entity
@Table(name="device")
public class Device implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name="id",strategy="uuid2")
	@GeneratedValue(generator="id")
	@Column(name="device_uuid",nullable=false,unique=true,length=50)
	private String deviceUuid;
	
	/**
	 * 所属设备类型
	 */
	@ManyToOne
	@JoinColumn(name="dev_type_uuid")
	private DeviceType devType;
	
	/**
	 * 设备编号，一个机构下的所有设备编号必须唯一
	 */
	@Column(name="sn",length=50)
	private String sn;
	
	/**
	 * 生产厂家
	 */
	@Column(name="manufactor",length=50)
	private String manufactor;
	
	/**
	 * 检定/校准机构
	 */
	@Column(name="calibrated_org",length=50)
	private String calibratedOrg;
	
	/**
	 * 保管员
	 */
	@Column(name="keeper",length=50)
	private String keeper;
	
	/**
	 * 校准证号
	 */
	@Column(name="calibrated_num",length=50)
	private String calibratedNum;
	
	/**
	 * 检定/校准有效期
	 */
	@Column(name="exp_date",length=50)
	private Date expDate;
	
	/**
	 * 备注
	 */
	@Column(name="remark",length=250)
	private String remark;
	
	/**
	 * 设备型号，与设备型号表进行关联
	 */
	@ManyToOne
	@JoinColumn(name="dev_model_uuid")
	private DeviceModel deviceModel;
	
	/**
	 * 设备状态
	 * 0--启用
	 *-1--停用
	 */
	@Column(name="status",length=50)
	private Integer status;
	
	/**
	 * 項目ID
	 */
	@Column(name="project_uuid",length=50)
	private String projectUuid;
	
	/**
	 * 所属部门
	 */
	@ManyToOne
	@JoinColumn(name="organ_uuid")
	private Organ organ;
	
	/**
	 * MCU
	 */
	@ManyToOne
	@JoinColumn(name="mcu_uuid")
	private Mcu mcu;
	
	/**
	 * 模块号
	 */
	@Column(name="moduleNum")
	private Integer moduleNum;
	
	/**
	 * 通道编号
	 */
	@Column(name="pointNum")
	private Integer pointNum;
	
	public Integer getModuleNum() {
		return moduleNum;
	}

	public void setModuleNum(Integer moduleNum) {
		this.moduleNum = moduleNum;
	}
	
	public Integer getPointNum() {
		return pointNum;
	}

	public void setPointNum(Integer pointNum) {
		this.pointNum = pointNum;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public DeviceModel getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(DeviceModel deviceModel) {
		this.deviceModel = deviceModel;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public DeviceType getDevType() {
		return devType;
	}

	public void setDevType(DeviceType devType) {
		this.devType = devType;
	}

	public String getManufactor() {
		return manufactor;
	}

	public void setManufactor(String manufactor) {
		this.manufactor = manufactor;
	}

	public String getCalibratedOrg() {
		return calibratedOrg;
	}

	public void setCalibratedOrg(String calibratedOrg) {
		this.calibratedOrg = calibratedOrg;
	}

	public String getKeeper() {
		return keeper;
	}

	public void setKeeper(String keeper)  {
		this.keeper = keeper;
	}

	public String getCalibratedNum() {
		return calibratedNum;
	}

	public void setCalibratedNum(String calibratedNum) {
		this.calibratedNum = calibratedNum;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) throws ParseException {
		Date parsedMeasureTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expDate);
		this.expDate = parsedMeasureTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectUuid() {
		return projectUuid;
	}

	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;
	}

	public Mcu getMcu() {
		return mcu;
	}

	public void setMcu(Mcu mcu) {
		this.mcu = mcu;
	}

	@Override
	public String toString() {
		return "Device [deviceUuid=" + deviceUuid + ", devType=" + devType + ", sn=" + sn + ", manufactor=" + manufactor
				+ ", calibratedOrg=" + calibratedOrg + ", keeper=" + keeper + ", calibratedNum=" + calibratedNum
				+ ", expDate=" + expDate + ", remark=" + remark + ", deviceModel=" + deviceModel + ", status=" + status
				+ ", projectUuid=" + projectUuid + ", organ=" + organ + ", mcu=" + mcu + ", moduleNum=" + moduleNum
				+ ", pointNum=" + pointNum + "]";
	}

}












