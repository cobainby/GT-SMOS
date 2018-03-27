package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName Vetor 
 * @Description @param  b   a vector (Array1D> of length equal to the first dimension
              of A.
  @return x   a vector (Array1D> so that L*U*x = b(piv), if B is 
              nonconformant, returns 0x0 (null) array.   
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
public class Vetor {
	/**
	 * a vector (Array1D> so that L*U*x = b(piv)
	 */
	private double x[];
	/**
	 * a vector (Array1D> of length equal to the first dimension
              of A.
	 */
	private double b[];
	public double[] getX() {
		return x;
	}
	public void setX(double[] x) {
		this.x = x;
	}
	public double[] getB() {
		return b;
	}
	public void setB(double[] b) {
		this.b = b;
	}
	
}
