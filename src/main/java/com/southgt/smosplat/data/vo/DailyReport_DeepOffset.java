package com.southgt.smosplat.data.vo;
/**
 * 
 * 测斜日报数据抽象类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年7月5日     姚家俊       v1.0.0        create</p>
 *
 */
public abstract class DailyReport_DeepOffset {
	/**
	 * 点号
	 */
	private String code;
	/**
	 * 当天最大变化量
	 */
	private double maxChangeRate;
	/**
	 * 当天最大累计变化量
	 */
	private double maxAccumOffset;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getMaxChangeRate() {
		return maxChangeRate;
	}
	public void setMaxChangeRate(double maxChangeRate) {
		this.maxChangeRate = maxChangeRate;
	}
	public double getMaxAccumOffset() {
		return maxAccumOffset;
	}
	public void setMaxAccumOffset(double maxAccumOffset) {
		this.maxAccumOffset = maxAccumOffset;
	}
	@Override
	public String toString() {
		return "DailyReport_DeepOffset [code=" + code + ", maxChangeRate=" + maxChangeRate + ", maxAccumOffset="
				+ maxAccumOffset + "]";
	}
}
