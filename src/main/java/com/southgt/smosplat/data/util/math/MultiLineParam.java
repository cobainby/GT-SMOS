package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName MutiLineParam 
 * @Description 用最小二乘法拟合二元多次曲线
		二元多次线性方程拟合曲线
		例如：y=a0+a1*x 返回值则为a0 a1
		例如：y=a0+a1*x+a2*x*x 返回值则为a0 a1 a2
		 @param arrX 已知点的x坐标集合
		 @param arrY 已知点的y坐标集合
		 @param dimension 方程的最高次数
		 @param Parameter 返回的函数系数
		 @param refData 拟合后的y坐标集合
		 @param refarrYOffset 实测与拟合差值集合
		 @param errorString 错误信息
		 @return 
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
public class MultiLineParam {
	/**
	 * 返回的函数系数
	 */
	private double[] parameter;
	/**
	 * 拟合后的y坐标集合
	 */
	private double[] refarrY;
	/**
	 * 实测与拟合差值集合
	 */
	private double[] refarrYOffset;
	/**
	 * 错误信息
	 */
	private String errorString;
	public double[] getParameter() {
		return parameter;
	}
	public void setParameter(double[] parameter) {
		this.parameter = parameter;
	}
	public double[] getRefarrY() {
		return refarrY;
	}
	public void setRefarrY(double[] refarrY) {
		this.refarrY = refarrY;
	}
	public double[] getRefarrYOffset() {
		return refarrYOffset;
	}
	public void setRefarrYOffset(double[] refarrYOffset) {
		this.refarrYOffset = refarrYOffset;
	}
	public String getErrorString() {
		return errorString;
	}
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
	
}
