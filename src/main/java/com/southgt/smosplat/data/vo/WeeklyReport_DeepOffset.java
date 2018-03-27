package com.southgt.smosplat.data.vo;
/**
 * 
 * 深层位移（测斜）周报数据抽象类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月28日     姚家俊       v1.0.0        create</p>
 *
 */
public abstract class WeeklyReport_DeepOffset {
	/**
	 * 点编号
	 */
	private String code;
	/**
	 * 深度(m),0.5m为一个单位
	 */
	private double depth;
	/**
	 * 周一的累计变化值(mm)
	 */
	private String monVal;
	/**
	 * 周二的累计变化值(mm)
	 */
	private String tueVal;
	/**
	 * 周三的累计变化值(mm)
	 */
	private String wedVal;
	/**
	 * 周四的累计变化值(mm)
	 */
	private String thurVal;
	/**
	 * 周五的累计变化值(mm)
	 */
	private String friVal;
	/**
	 * 周六的累计变化值(mm)
	 */
	private String satVal;
	/**
	 * 周日的累计变化值(mm)
	 */
	private String sunVal;
	/**
	 * 初始值
	 */
	private String originalVal;
	/**
	 * 初始日期
	 */
	private String originalDate;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getDepth() {
		return depth;
	}
	public void setDepth(double depth) {
		this.depth = depth;
	}
	public String getMonVal() {
		return monVal;
	}
	public void setMonVal(String monVal) {
		this.monVal = monVal;
	}
	public String getTueVal() {
		return tueVal;
	}
	public void setTueVal(String tueVal) {
		this.tueVal = tueVal;
	}
	public String getWedVal() {
		return wedVal;
	}
	public void setWedVal(String wedVal) {
		this.wedVal = wedVal;
	}
	public String getThurVal() {
		return thurVal;
	}
	public void setThurVal(String thurVal) {
		this.thurVal = thurVal;
	}
	public String getFriVal() {
		return friVal;
	}
	public void setFriVal(String friVal) {
		this.friVal = friVal;
	}
	public String getSatVal() {
		return satVal;
	}
	public void setSatVal(String satVal) {
		this.satVal = satVal;
	}
	public String getSunVal() {
		return sunVal;
	}
	public void setSunVal(String sunVal) {
		this.sunVal = sunVal;
	}
	public String getOriginalVal() {
		return originalVal;
	}
	public void setOriginalVal(String originalVal) {
		this.originalVal = originalVal;
	}
	public String getOriginalDate() {
		return originalDate;
	}
	public void setOriginalDate(String originalDate) {
		this.originalDate = originalDate;
	}
	@Override
	public String toString() {
		return "WeeklyReport_DeepOffset [code=" + code + ", depth=" + depth + ", monVal=" + monVal + ", tueVal="
				+ tueVal + ", wedVal=" + wedVal + ", thurVal=" + thurVal + ", friVal=" + friVal + ", satVal=" + satVal
				+ ", sunVal=" + sunVal + ", originalVal=" + originalVal + ", originalDate=" + originalDate + "]";
	}
	
}
