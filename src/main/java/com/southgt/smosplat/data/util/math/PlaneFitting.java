package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName PlaneFitting 
 * @Description 平面计算
		 X = x0 + k * Math.Cos(alpha) * (x - OldXAvg) + k * Math.Sin(alpha) * (y - OldYAvg);
		 Y = y0 + k * Math.Cos(alpha) * (y - OldYAvg) - k * Math.Sin(alpha) * (x - OldXAvg);
		 或
		 X = x0 + a * (x - OldXAvg) + b * (Y - OldYAvg);
		 Y = y0 + a * (Y - OldYAvg) - b * (X - OldXAvg);
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
/**
 * 		 @param x0
		 @param y0
		 @param a
		 @param b
		 @param k
		 @param alpha
		 @param xoffset
		 @param yoffset
		 @param errorString
 */
public class PlaneFitting {
	private double x0;
	private double y0;
	private double h0;
	private double k;
	private double alpha;
	private double oldAAvg;
	private double oldYAvg;
	private double oldHAvg;
	private double[] xoffset;
	private double[] yoffset;
	private double[] hoffset;
	/**
	 * 点位中误差
	 */
	private double rMSE;
	private String errorString;
	public double getX0() {
		return x0;
	}
	public void setX0(double x0) {
		this.x0 = x0;
	}
	public double getY0() {
		return y0;
	}
	public void setY0(double y0) {
		this.y0 = y0;
	}
	public double getH0() {
		return h0;
	}
	public void setH0(double h0) {
		this.h0 = h0;
	}
	public double getK() {
		return k;
	}
	public void setK(double k) {
		this.k = k;
	}
	public double getAlpha() {
		return alpha;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	public double getOldAAvg() {
		return oldAAvg;
	}
	public void setOldAAvg(double oldAAvg) {
		this.oldAAvg = oldAAvg;
	}
	public double getOldYAvg() {
		return oldYAvg;
	}
	public void setOldYAvg(double oldYAvg) {
		this.oldYAvg = oldYAvg;
	}
	public double getOldHAvg() {
		return oldHAvg;
	}
	public void setOldHAvg(double oldHAvg) {
		this.oldHAvg = oldHAvg;
	}
	public double[] getXoffset() {
		return xoffset;
	}
	public void setXoffset(double[] xoffset) {
		this.xoffset = xoffset;
	}
	public double[] getYoffset() {
		return yoffset;
	}
	public void setYoffset(double[] yoffset) {
		this.yoffset = yoffset;
	}
	public double[] getHoffset() {
		return hoffset;
	}
	public void setHoffset(double[] hoffset) {
		this.hoffset = hoffset;
	}
	public double getrMSE() {
		return rMSE;
	}
	public void setrMSE(double rMSE) {
		this.rMSE = rMSE;
	}
	public String getErrorString() {
		return errorString;
	}
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
	

}
