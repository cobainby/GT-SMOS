package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName MeasureData
 * @Description 测量数据结构类
 * 
 * @version v1.0.0 
 * @author 姚家俊
 * 
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年7月11日     姚家俊       v1.0.0        create</p>
 *
 *
 */
public final class MeasureData //观测点结构体
{
	public String name;
	public double angleH; //观测的水平角
	public double angleV; //观测的竖直角
	public double dis; //观测斜距
	public int type; //0为观测点，1为控制点
	public double east; //东坐标
	public double north; //北坐标
	public double height; //高程
	public double a;
	public double b;
	public double l;
	public double dx;
	public double dy;
	public double azimuth;
	public double s;
	public double la;
	public double lb;
	public double ll;

	@Override
	public MeasureData clone()
	{
		MeasureData varCopy = new MeasureData();

		varCopy.name = this.name;
		varCopy.angleH = this.angleH;
		varCopy.angleV = this.angleV;
		varCopy.dis = this.dis;
		varCopy.type = this.type;
		varCopy.east = this.east;
		varCopy.north = this.north;
		varCopy.height = this.height;
		varCopy.a = this.a;
		varCopy.b = this.b;
		varCopy.l = this.l;
		varCopy.dx = this.dx;
		varCopy.dy = this.dy;
		varCopy.azimuth = this.azimuth;
		varCopy.s = this.s;
		varCopy.la = this.la;
		varCopy.lb = this.lb;
		varCopy.ll = this.ll;

		return varCopy;
	}
}
