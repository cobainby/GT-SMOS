package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName AzimuthAndVerticalAngle 
 * @Description TODO(这里用一句话描述这个类的作用)
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
public class AzimuthAndVerticalAngle {
	/**
	 * 方位角
	 */
	private double azimuthAngle;
	/**
	 * 竖直角
	 */
	private double verticalAngle;
	public double getAzimuthAngle() {
		return azimuthAngle;
	}
	public void setAzimuthAngle(double azimuthAngle) {
		this.azimuthAngle = azimuthAngle;
	}
	public double getVerticalAngle() {
		return verticalAngle;
	}
	public void setVerticalAngle(double verticalAngle) {
		this.verticalAngle = verticalAngle;
	}
	/** 
	* <p>Title: </p> 
	* <p>Description: </p>  
	*/
	public AzimuthAndVerticalAngle() {
		super();
		this.azimuthAngle=this.verticalAngle=Double.MAX_VALUE;
	}
	
}
