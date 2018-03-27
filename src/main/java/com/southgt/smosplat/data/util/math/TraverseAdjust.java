package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName TraverseAdjust 
 * @Description  无定向导线平差
 * 
 * @version v1.0.0 
 * @author 姚家俊
 * 
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年7月8日     姚家俊       v1.0.0        create</p>
 *
 *
 */
public class TraverseAdjust {
	/**
	 * 控制点东坐标
	 */
	private double[] controlPointE;
	/**
	 * 控制点北坐标
	 */
	private double[] controlPointN;
	
	/**
	 * 控制点高程
	 */
	private double[] controlPointH;
	/**
	 * 平距
	 */
	java.util.ArrayList<Double> hD;
	public double[] getControlPointE() {
		return controlPointE;
	}
	public void setControlPointE(double[] controlPointE) {
		this.controlPointE = controlPointE;
	}
	public double[] getControlPointN() {
		return controlPointN;
	}
	public void setControlPointN(double[] controlPointN) {
		this.controlPointN = controlPointN;
	}
	public double[] getControlPointH() {
		return controlPointH;
	}
	public void setControlPointH(double[] controlPointH) {
		this.controlPointH = controlPointH;
	}
	public java.util.ArrayList<Double> gethD() {
		return hD;
	}
	public void sethD(java.util.ArrayList<Double> hD) {
		this.hD = hD;
	}
	
}
