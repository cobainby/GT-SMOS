package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName Disp 
 * @Description 东、北、高位移
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
public class Disp {
	/**
	 * 东位移
	 */
	private double disp_E;
	/**
	 * 北位移
	 */
	private double disp_N;
	/**
	 * 高位移
	 */
	private double disp_H;
	public double getDisp_E() {
		return disp_E;
	}
	public void setDisp_E(double disp_E) {
		this.disp_E = disp_E;
	}
	public double getDisp_N() {
		return disp_N;
	}
	public void setDisp_N(double disp_N) {
		this.disp_N = disp_N;
	}
	public double getDisp_H() {
		return disp_H;
	}
	public void setDisp_H(double disp_H) {
		this.disp_H = disp_H;
	}
	/** 
	* <p>Title: </p> 
	* <p>Description: </p>  
	*/
	public Disp() {
		super();
		this.disp_E=this.disp_N=this.disp_H=Double.MAX_VALUE;
	}
	
}
