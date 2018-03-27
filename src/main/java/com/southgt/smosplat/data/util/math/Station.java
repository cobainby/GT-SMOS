package com.southgt.smosplat.data.util.math;

/**
 * 
 * 测站实体类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月25日     姚家俊       v1.0.0        create</p>
 *
 */
public class Station {
	/**
	 * 测站东坐标
	 */
	private double east;
	/**
	 * 测站北坐标
	 */
	private double north;
	/**
	 * 测站高程
	 */
	private double height;
	/**
	 * 测站仪器高
	 */
	private double instrumentHeight;
	/**
	 * 测站名
	 */
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getEast() {
		return east;
	}
	public void setEast(double east) {
		this.east = east;
	}
	public double getNorth() {
		return north;
	}
	public void setNorth(double north) {
		this.north = north;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getInstrumentHeight() {
		return instrumentHeight;
	}
	public void setInstrumentHeight(double instrumentHeight) {
		this.instrumentHeight = instrumentHeight;
	} 

}
