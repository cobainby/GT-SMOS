package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName Math_ReturnParam 
 * @Description 返回参数类
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
public class Math_ReturnParam {
	/**
	 * 东北高位移
	 */
	private Disp disp;
	
	/**
	 * 3X3逆矩阵
	 */
	private double[][] inverse=new double[3][3];
	
	/**
	 * 两个3X3的矩阵求点乘
	 */
	private double[][] des=new double[3][3];
	
	/**
	 * 方位角和竖直角
	 */
	private AzimuthAndVerticalAngle azimuthAndVertialAngle;
	
	/**
	 * 计算坐标
	 */
	private Coordinate coord;
	
	/**
	 * 拟合二元多次曲线参数
	 */
	private MultiLineParam mutiLineParam;
	
	/**
	 * 平面计算
	 */
	private PlaneFitting planeFitting;
	
	/**
	 * 测站高程
	 */
	private double insHeight;
	
	/**
	 * 高程计算
	 */
	private PlaneFitting heightFitting;
	
	/**
	 *  无定向导线平差（平面）
	 */
	private TraverseAdjust traverseAdjustPlane;
	
	/**
	 * 无低昂想导线平差（高程）
	 */
	private TraverseAdjust traverseAdjustHeight;
	
	/**
	 * 坐标转矩阵
	 */
	private double[] coordTransMatrix;
	
	/**
	 * 多维矩阵的转置
	 */
	private double[] permute;
	
	/**
	 * 下三角因子
	 */
	private double[] lowerTriangularFactor;
	
	/**
	 * 上三角因子
	 */
	private double[] upperTriangularFactor;
	
	/**
	 * 主元，每行的第一个非零元素
	 */
	private int[] pivot;
	
	/**
	 * 向量
	 */
	private Vetor vetor;
	
	/**
	 * 转换后的坐标
	 */
	private double[] p_new;
	
	/**
	 * 转换前的坐标
	 */
	private double[] p_old;
	
	/**
	 * 平面参数
	 */
	private double[] planeparam;
	
	/**
	 * 实数根的个数
	 */
	private int iNumReal;
	
	/**
	 * 方程组的根
	 */
	private double[] rootsArray;
	
	/**
	 * 矩阵和向量的乘积
	 */
	private double[] rv;
	
	/**
	 * 矩阵m1和m2的乘积
	 */
	private double[] mr;
	
	/**
	 * 绕x轴或者y轴或者z轴旋转一个角度angle的坐标变换矩阵
	 */
	private double[] rotatedMatrix;
	
	/**
	 * 平移变换的坐标变换距阵
	 */
	private double[] parallelMoveMatrix;
	
	/**
	 * mindis
	 */
	private double mindis;
	
	/**
	 * 坐标变换的4×4阶的
      	系数矩阵
	 */
	private double[] transMatrix;
	
	/**
	 * 坐标变换的4×4阶的
      	系数矩阵的转置
	 */
	private double[] invertTransMatrix;

	/**
	 * 四参数类
	 */
	private FOURPARA fourParam;
	public Disp getDisp() {
		return disp;
	}

	public void setDisp(Disp disp) {
		this.disp = disp;
	}

	public double[][] getInverse() {
		return inverse;
	}

	public void setInverse(double[][] inverse) {
		this.inverse = inverse;
	}

	public double[][] getDes() {
		return des;
	}

	public void setDes(double[][] des) {
		this.des = des;
	}

	public AzimuthAndVerticalAngle getAzimuthAndVertialAngle() {
		return azimuthAndVertialAngle;
	}

	public void setAzimuthAndVertialAngle(
			AzimuthAndVerticalAngle azimuthAndVertialAngle) {
		this.azimuthAndVertialAngle = azimuthAndVertialAngle;
	}

	public Coordinate getCoord() {
		return coord;
	}

	public void setCoord(Coordinate coord) {
		this.coord = coord;
	}

	public MultiLineParam getMutiLineParam() {
		return mutiLineParam;
	}

	public void setMutiLineParam(MultiLineParam mutiLineParam) {
		this.mutiLineParam = mutiLineParam;
	}

	public PlaneFitting getPlaneFitting() {
		return planeFitting;
	}

	public void setPlaneFitting(PlaneFitting planeFitting) {
		this.planeFitting = planeFitting;
	}

	public double getInsHeight() {
		return insHeight;
	}

	public void setInsHeight(double insHeight) {
		this.insHeight = insHeight;
	}

	public PlaneFitting getHeightFitting() {
		return heightFitting;
	}

	public void setHeightFitting(PlaneFitting heightFitting) {
		this.heightFitting = heightFitting;
	}
	public TraverseAdjust getTraverseAdjustPlane() {
		return traverseAdjustPlane;
	}

	public void setTraverseAdjustPlane(TraverseAdjust traverseAdjustPlane) {
		this.traverseAdjustPlane = traverseAdjustPlane;
	}

	public TraverseAdjust getTraverseAdjustHeight() {
		return traverseAdjustHeight;
	}

	public void setTraverseAdjustHeight(TraverseAdjust traverseAdjustHeight) {
		this.traverseAdjustHeight = traverseAdjustHeight;
	}

	public double[] getCoordTransMatrix() {
		return coordTransMatrix;
	}

	public void setCoordTransMatrix(double[] coordTransMatrix) {
		this.coordTransMatrix = coordTransMatrix;
	}

	public double[] getPermute() {
		return permute;
	}

	public void setPermute(double[] permute) {
		this.permute = permute;
	}

	public double[] getLowerTriangularFactor() {
		return lowerTriangularFactor;
	}

	public void setLowerTriangularFactor(double[] lowerTriangularFactor) {
		this.lowerTriangularFactor = lowerTriangularFactor;
	}

	public double[] getUpperTriangularFactor() {
		return upperTriangularFactor;
	}

	public void setUpperTriangularFactor(double[] upperTriangularFactor) {
		this.upperTriangularFactor = upperTriangularFactor;
	}


	public int[] getPivot() {
		return pivot;
	}

	public void setPivot(int[] pivot) {
		this.pivot = pivot;
	}

	public Vetor getVetor() {
		return vetor;
	}

	public void setVetor(Vetor vetor) {
		this.vetor = vetor;
	}

	public double[] getP_new() {
		return p_new;
	}

	public void setP_new(double[] p_new) {
		this.p_new = p_new;
	}

	public double[] getP_old() {
		return p_old;
	}

	public void setP_old(double[] p_old) {
		this.p_old = p_old;
	}

	public double[] getPlaneparam() {
		return planeparam;
	}

	public void setPlaneparam(double[] planeparam) {
		this.planeparam = planeparam;
	}

	public int getiNumReal() {
		return iNumReal;
	}

	public void setiNumReal(int iNumReal) {
		this.iNumReal = iNumReal;
	}

	public double[] getRootsArray() {
		return rootsArray;
	}

	public void setRootsArray(double[] rootsArray) {
		this.rootsArray = rootsArray;
	}

	public double[] getRv() {
		return rv;
	}

	public void setRv(double[] rv) {
		this.rv = rv;
	}

	public double[] getMr() {
		return mr;
	}

	public void setMr(double[] mr) {
		this.mr = mr;
	}

	public double[] getRotatedMatrix() {
		return rotatedMatrix;
	}

	public void setRotatedMatrix(double[] rotatedMatrix) {
		this.rotatedMatrix = rotatedMatrix;
	}

	public double[] getParallelMoveMatrix() {
		return parallelMoveMatrix;
	}

	public void setParallelMoveMatrix(double[] parallelMoveMatrix) {
		this.parallelMoveMatrix = parallelMoveMatrix;
	}

	public double getMindis() {
		return mindis;
	}

	public void setMindis(double mindis) {
		this.mindis = mindis;
	}

	public double[] getTransMatrix() {
		return transMatrix;
	}

	public void setTransMatrix(double[] transMatrix) {
		this.transMatrix = transMatrix;
	}

	public double[] getInvertTransMatrix() {
		return invertTransMatrix;
	}

	public void setInvertTransMatrix(double[] invertTransMatrix) {
		this.invertTransMatrix = invertTransMatrix;
	}
	
	public FOURPARA getFourParam() {
		return fourParam;
	}

	public void setFourParam(FOURPARA fourParam) {
		this.fourParam = fourParam;
	}

	/** 
	* <p>Title: </p> 
	* <p>Description: </p>  
	*/
	public Math_ReturnParam() {
		super();
		this.disp=new Disp();
		this.azimuthAndVertialAngle=new AzimuthAndVerticalAngle();
		this.coord=new Coordinate();
	}
	
}
