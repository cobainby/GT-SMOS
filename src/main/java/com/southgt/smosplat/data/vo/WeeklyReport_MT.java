package com.southgt.smosplat.data.vo;
/**
 * 
 * 锚索周报数据
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月28日     姚家俊       v1.0.0        create</p>
 *
 */
public class WeeklyReport_MT {
	/**
	 * 点编号
	 */
	private String code;
	/**
	 * 周一的变化量(kN)
	 */
	private double monVal;
	/**
	 * 周二的变化量(kN)
	 */
	private double tueVal;
	/**
	 * 周三的变化量(kN)
	 */
	private double wedVal;
	/**
	 * 周四的变化量(kN)
	 */
	private double thurVal;
	/**
	 * 周五的变化量(kN)
	 */
	private double friVal;
	/**
	 * 周六的变化量(kN)
	 */
	private double satVal;
	/**
	 * 周日的变化量(kN)
	 */
	private double sunVal;
	/**
	 * 内力初始值(kN)
	 */
	private double originalVal;
	/**
	 * 本周变化量(kN)
	 */
	private double sumChange;
	/**
	 * 本周变化速率(kN/d)
	 */
	private double sumChangeRate;
	/**
	 * 当前应力值(kN)
	 */
	private double currentVal;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getMonVal() {
		return monVal;
	}
	public void setMonVal(double monVal) {
		this.monVal = monVal;
	}
	public double getTueVal() {
		return tueVal;
	}
	public void setTueVal(double tueVal) {
		this.tueVal = tueVal;
	}
	public double getWedVal() {
		return wedVal;
	}
	public void setWedVal(double wedVal) {
		this.wedVal = wedVal;
	}
	public double getThurVal() {
		return thurVal;
	}
	public void setThurVal(double thurVal) {
		this.thurVal = thurVal;
	}
	public double getFriVal() {
		return friVal;
	}
	public void setFriVal(double friVal) {
		this.friVal = friVal;
	}
	public double getSatVal() {
		return satVal;
	}
	public void setSatVal(double satVal) {
		this.satVal = satVal;
	}
	public double getSunVal() {
		return sunVal;
	}
	public void setSunVal(double sunVal) {
		this.sunVal = sunVal;
	}
	public double getOriginalVal() {
		return originalVal;
	}
	public void setOriginalVal(double originalVal) {
		this.originalVal = originalVal;
	}
	public double getSumChange() {
		return sumChange;
	}
	public void setSumChange(double sumChange) {
		this.sumChange = sumChange;
	}
	public double getSumChangeRate() {
		return sumChangeRate;
	}
	public void setSumChangeRate(double sumChangeRate) {
		this.sumChangeRate = sumChangeRate;
	}
	public double getCurrentVal() {
		return currentVal;
	}
	public void setCurrentVal(double currentVal) {
		this.currentVal = currentVal;
	}
	@Override
	public String toString() {
		return "WeeklyReport_MT [code=" + code + ", monVal=" + monVal + ", tueVal=" + tueVal + ", wedVal=" + wedVal
				+ ", thurVal=" + thurVal + ", friVal=" + friVal + ", satVal=" + satVal + ", sunVal=" + sunVal
				+ ", originalVal=" + originalVal + ", sumChange=" + sumChange + ", sumChangeRate=" + sumChangeRate
				+ ", currentVal=" + currentVal + "]";
	}
}
