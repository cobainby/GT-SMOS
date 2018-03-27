package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName Coordinate 
 * @Description 坐标类
 * 
 * @version v1.0.0 
 * @author 姚家俊
 * 
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年7月7日     姚家俊       v1.0.0        create</p>
 *
 *
 */
public class Coordinate {
	/**
	 * 计算东坐标
	 */
	private double dE;
	/**
	 * 计算北坐标
	 */
	private double dN;
	/**
	 * 计算高程
	 */
	private double dH;
	
	/**
	 * 东坐标精度
	 */
	private double dSigmaEast;
	/**
	 * 北坐标精度
	 */
	private double dSigmaNorth;
	
	/**
	 * 高程精度
	 */
	private double dSigmaHeight;
	
	/**
	 * 解算后的网格因子
	 */
	private double dScale;

	
	public double getdE() {
		return dE;
	}
	public void setdE(double dE) {
		this.dE = dE;
	}
	public double getdN() {
		return dN;
	}
	public void setdN(double dN) {
		this.dN = dN;
	}
	public double getdH() {
		return dH;
	}
	public void setdH(double dH) {
		this.dH = dH;
	}
	public double getdSigmaEast() {
		return dSigmaEast;
	}
	public void setdSigmaEast(double dSigmaEast) {
		this.dSigmaEast = dSigmaEast;
	}
	public double getdSigmaNorth() {
		return dSigmaNorth;
	}
	public void setdSigmaNorth(double dSigmaNorth) {
		this.dSigmaNorth = dSigmaNorth;
	}
	public double getdSigmaHeight() {
		return dSigmaHeight;
	}
	public void setdSigmaHeight(double dSigmaHeight) {
		this.dSigmaHeight = dSigmaHeight;
	}
	public double getdScale() {
		return dScale;
	}
	public void setdScale(double dScale) {
		this.dScale = dScale;
	}
	/** 
	* <p>Title: </p> 
	* <p>Description: </p>  
	*/
	public Coordinate() {
		super();
		this.dE=this.dH=this.dN=this.dScale=this.dSigmaEast=this.dSigmaNorth=this.dSigmaHeight=Double.MAX_VALUE;
	}
	
}
