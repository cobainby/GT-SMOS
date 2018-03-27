package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName cc 
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
public class CoordTrans7Param
{
//	public double[][] values = new double[7][1];
//	//{{dx},{dy},{dz},{rx},{ry},{rz},{k}}; 
//	//public double dx,dy,dz,rx,ry,rz,k; 
//	public final void Set4Param(double dx, double dy, double dz, double k)
//	{
//		this.setDX(dx);
//		this.setDY(dy);
//		this.setDZ(dz);
//		this.setK(k);
//		this.setRZ(0);
//		this.setRY(this.getRZ());
//		this.setRX(this.getRY());
//	}
//	public final void SetRotationParamRad(double rx, double ry, double rz)
//	{
//		this.setRX(rx);
//		this.setRY(ry);
//		this.setRZ(rz);
//	}
//	public final void SetRotationParamMM(double rx, double ry, double rz)
//	{
//		SetRotationParamRad(rx * Math.PI / 648000, ry * Math.PI / 648000, rz * Math.PI / 648000);
//	}
//	private double[][] GetMx()
//	{
//		double[][] Mx = new double[][] {{1,0,0}, {0,Math.cos(getRX()),Math.sin(getRX())}, {0,-Math.sin(getRX()),Math.cos(getRX())}};
//		return Mx;
//	}
//	private double[][] GetMy()
//	{
//		double[][] My = new double[][] {{Math.cos(getRY()),0,-Math.sin(getRY())}, {0,1,0}, {Math.sin(getRY()),0,Math.cos(getRY())}};
//		return My;
//	}
//	private double[][] GetMz()
//	{
//		double[][] Mz = new double[][] {{Math.cos(getRZ()),Math.sin(getRZ()),0}, {-Math.sin(getRZ()),Math.cos(getRZ()),0}, {0,0,1}};
//		return Mz;
//	}
//
//	private double[][] GetM() //M=Mx*My*Mz? or M=Mz*My*Mx?
//	{
//		double[][] M = new double[3][3];
//
//		RefObject<Double> tempRef_M = new RefObject<Double>(M);
//		GT.CORE.Matrix.MatrixTool.Multi(GetMz(), GetMy(), tempRef_M);
//		M = tempRef_M.argvalue;
//		RefObject<Double> tempRef_M2 = new RefObject<Double>(M);
//		GT.CORE.Matrix.MatrixTool.Multi(M, GetMx(), tempRef_M2);
//		M = tempRef_M2.argvalue;
//		return M;
//	}
//	private double[][] GetMdx()
//	{
//		double[][] mt = {{ 0, 0, 0 }, { 0, -Math.sin(getRX()), Math.cos(getRX()) }, { 0, -Math.cos(getRX()), -Math.sin(getRX()) }};
//
//		double[][] m = new double[3][3];
//
//		RefObject<Double> tempRef_m = new RefObject<Double>(m);
//		GT.CORE.Matrix.MatrixTool.Multi(GetMz(), GetMy(), tempRef_m);
//		m = tempRef_m.argvalue;
//		RefObject<Double> tempRef_m2 = new RefObject<Double>(m);
//		GT.CORE.Matrix.MatrixTool.Multi(m, mt, tempRef_m2);
//		m = tempRef_m2.argvalue;
//		return m;
//	}
//	private double[][] GetMdy()
//	{
//		double[][] mt = {{ -Math.sin(getRY()), 0, -Math.cos(getRY()) }, { 0, 0, 0 }, { Math.cos(getRY()), 0, -Math.sin(getRY()) }};
//
//		double[][] m = new double[3][3];
//
//		RefObject<Double> tempRef_m = new RefObject<Double>(m);
//		GT.CORE.Matrix.MatrixTool.Multi(GetMz(), mt, tempRef_m);
//		m = tempRef_m.argvalue;
//		RefObject<Double> tempRef_m2 = new RefObject<Double>(m);
//		GT.CORE.Matrix.MatrixTool.Multi(m, GetMx(), tempRef_m2);
//		m = tempRef_m2.argvalue;
//		return m;
//	}
//
//	private double[][] GetMdz()
//	{
//		double[][] mt = {{ -Math.sin(getRZ()), Math.cos(getRZ()), 0 }, { -Math.cos(getRZ()), -Math.sin(getRZ()), 0 }, { 0, 0, 0 }};
//
//		double[][] m = new double[3][3];
//
//		RefObject<Double> tempRef_m = new RefObject<Double>(m);
//		GT.CORE.Matrix.MatrixTool.Multi(mt, GetMy(), tempRef_m);
//		m = tempRef_m.argvalue;
//		RefObject<Double> tempRef_m2 = new RefObject<Double>(m);
//		GT.CORE.Matrix.MatrixTool.Multi(m, GetMx(), tempRef_m2);
//		m = tempRef_m2.argvalue;
//		return m;
//	}
//	private double[][] specialMulti(double[][] m, double[][] X)
//	{
//		int rowNumM = m.length;
//		int colNumM = m[0].length;
//		int rowNumX = X.length;
//		int colNumX = X[0].length;
//		int lines = rowNumX / colNumM;
//		double[][] mt = GT.CORE.Matrix.MatrixTool.Init(rowNumM, colNumX);
//		double[][] subX = GT.CORE.Matrix.MatrixTool.Init(colNumM, colNumX);
//		double[][] res = GT.CORE.Matrix.MatrixTool.Init(rowNumM * lines, colNumX);
//
//		for (int i = 0; i < lines; i++)
//		{
//			RefObject<Double> tempRef_subX = new RefObject<Double>(subX);
//			GT.CORE.Matrix.MatrixTool.CopySub(X, i * colNumM, 0, colNumM, colNumX, tempRef_subX, 0, 0);
//			subX = tempRef_subX.argvalue;
//			RefObject<Double> tempRef_mt = new RefObject<Double>(mt);
//			GT.CORE.Matrix.MatrixTool.Multi(m, subX, tempRef_mt);
//			mt = tempRef_mt.argvalue;
//			RefObject<Double> tempRef_res = new RefObject<Double>(res);
//			GT.CORE.Matrix.MatrixTool.CopySub(mt, 0, 0, rowNumM, colNumX, tempRef_res, i * rowNumM, 0);
//			res = tempRef_res.argvalue;
//		}
//		return res;
//	}
//	private double[][] specialSub(double[][] m, double[][] X)
//	{
//		int rowNumM = m.length;
//		int colNumM = m[0].length;
//		int rowNumX = X.length;
//		int colNumX = X[0].length;
//		int lines = rowNumX / rowNumM;
//		double[][] subX = GT.CORE.Matrix.MatrixTool.Init(rowNumM, colNumX);
//		double[][] res = GT.CORE.Matrix.MatrixTool.Init(rowNumX, colNumX);
//
//		for (int i = 0; i < rowNumX; i += rowNumM)
//		{
//			RefObject<Double> tempRef_subX = new RefObject<Double>(subX);
//			GT.CORE.Matrix.MatrixTool.CopySub(X, i, 0, rowNumM, colNumX, tempRef_subX, 0, 0);
//			subX = tempRef_subX.argvalue;
//			RefObject<Double> tempRef_subX2 = new RefObject<Double>(subX);
//			GT.CORE.Matrix.MatrixTool.Sub(m, subX, tempRef_subX2);
//			subX = tempRef_subX2.argvalue;
//			RefObject<Double> tempRef_res = new RefObject<Double>(res);
//			GT.CORE.Matrix.MatrixTool.CopySub(subX, 0, 0, rowNumM, colNumX, tempRef_res, i, 0);
//			res = tempRef_res.argvalue;
//		}
//		return res;
//	}
//
//	private double[][] GetF(double[][] X, double[][] Y)
//	{
//		double[][] f0;
//		double[][] qx = GT.CORE.Matrix.MatrixTool.Init(X.length, 1);
//		double[][] K1 = { { -getDX() }, { -getDY() }, { -getDZ() } };
//		double[][] S = { { 1 + getK() } };
//
//		RefObject<Double> tempRef_qx = new RefObject<Double>(qx);
//		GT.CORE.Matrix.MatrixTool.Multi(X, S, tempRef_qx);
//		qx = tempRef_qx.argvalue;
//		double[][] M = GetM();
//		qx = specialMulti(M, qx);
//		RefObject<Double> tempRef_qx2 = new RefObject<Double>(qx);
//		GT.CORE.Matrix.MatrixTool.Sub(qx, Y, tempRef_qx2);
//		qx = tempRef_qx2.argvalue;
//		f0 = specialSub(K1, qx);
//		return f0;
//	}
//	private double[][] GetB(double[][] X)
//	{
//		int rowNum = X.length;
//		double[][] B = GT.CORE.Matrix.MatrixTool.Init(rowNum, 7);
//		double[][] M = GetM();
//		double[][] Mdx = GetMdx();
//		double[][] Mdy = GetMdy();
//		double[][] Mdz = GetMdz();
//		double[][] mi = GT.CORE.Matrix.MatrixTool.Ident(3);
//		double[][] MX, MY, MZ, MK;
//
//		MK = specialMulti(M, X);
//		MX = specialMulti(Mdx, X);
//		MY = specialMulti(Mdy, X);
//		MZ = specialMulti(Mdz, X);
//
//		for (int i = 0; i < rowNum; i += 3)
//		{
//			RefObject<Double> tempRef_B = new RefObject<Double>(B);
//			GT.CORE.Matrix.MatrixTool.CopySub(mi, 0, 0, 3, 3, tempRef_B, i, 0);
//			B = tempRef_B.argvalue;
//		}
//		RefObject<Double> tempRef_B2 = new RefObject<Double>(B);
//		GT.CORE.Matrix.MatrixTool.CopySub(MX, 0, 0, rowNum, 1, tempRef_B2, 0, 3);
//		B = tempRef_B2.argvalue;
//		RefObject<Double> tempRef_B3 = new RefObject<Double>(B);
//		GT.CORE.Matrix.MatrixTool.CopySub(MY, 0, 0, rowNum, 1, tempRef_B3, 0, 4);
//		B = tempRef_B3.argvalue;
//		RefObject<Double> tempRef_B4 = new RefObject<Double>(B);
//		GT.CORE.Matrix.MatrixTool.CopySub(MZ, 0, 0, rowNum, 1, tempRef_B4, 0, 5);
//		B = tempRef_B4.argvalue;
//		RefObject<Double> tempRef_B5 = new RefObject<Double>(B);
//		GT.CORE.Matrix.MatrixTool.CopySub(MK, 0, 0, rowNum, 1, tempRef_B5, 0, 6);
//		B = tempRef_B5.argvalue;
//		return B;
//	}
//	private double[][] GetA()
//	{
//		double[][] M = GetM();
//		double[][] I2 = GT.CORE.Matrix.MatrixTool.Ident(3);
//		double[][] A = GT.CORE.Matrix.MatrixTool.Init(3, 6);
//
//		RefObject<Double> tempRef_I2 = new RefObject<Double>(I2);
//		GT.CORE.Matrix.MatrixTool.MutliConst(tempRef_I2, -1);
//		I2 = tempRef_I2.argvalue;
//		RefObject<Double> tempRef_M = new RefObject<Double>(M);
//		GT.CORE.Matrix.MatrixTool.MutliConst(tempRef_M, (1 + getK()));
//		M = tempRef_M.argvalue;
//		RefObject<Double> tempRef_A = new RefObject<Double>(A);
//		GT.CORE.Matrix.MatrixTool.CopySub(M, 0, 0, 3, 3, tempRef_A, 0, 0);
//		A = tempRef_A.argvalue;
//		RefObject<Double> tempRef_A2 = new RefObject<Double>(A);
//		GT.CORE.Matrix.MatrixTool.CopySub(I2, 0, 0, 3, 3, tempRef_A2, 0, 3);
//		A = tempRef_A2.argvalue;
//		return A;
//	}
//	private double[][] GetV(double[][] X, double[][] Y, CoordTrans7Param dpp)
//	{
//		int rowNum = X.length;
//
//		double[][] B, F, A, B2, B3, F2, V;
//		double[][] AT = GT.CORE.Matrix.MatrixTool.Init(6, 3);
//
//		A = GetA();
//		RefObject<Double> tempRef_AT = new RefObject<Double>(AT);
//		GT.CORE.Matrix.MatrixTool.AT(A, tempRef_AT);
//		AT = tempRef_AT.argvalue;
//		RefObject<Double> tempRef_AT2 = new RefObject<Double>(AT);
//		GT.CORE.Matrix.MatrixTool.MutliConst(tempRef_AT2, 1 / (1 + (1 + getK()) * (1 + getK())));
//		AT = tempRef_AT2.argvalue;
//
//		F = GetF(X, Y);
//		B = GetB(X);
//		B2 = GT.CORE.Matrix.MatrixTool.Init(3, 7);
//		B3 = GT.CORE.Matrix.MatrixTool.Init(3, 1);
//		F2 = GT.CORE.Matrix.MatrixTool.Init(rowNum, 1);
//		for (int i = 0; i < rowNum / 3; i++)
//		{
//			RefObject<Double> tempRef_B2 = new RefObject<Double>(B2);
//			GT.CORE.Matrix.MatrixTool.CopySub(B, i * 3, 0, 3, 7, tempRef_B2, 0, 0);
//			B2 = tempRef_B2.argvalue;
//			RefObject<Double> tempRef_B3 = new RefObject<Double>(B3);
//			GT.CORE.Matrix.MatrixTool.Multi(B2, dpp.values, tempRef_B3);
//			B3 = tempRef_B3.argvalue;
//			RefObject<Double> tempRef_F2 = new RefObject<Double>(F2);
//			GT.CORE.Matrix.MatrixTool.CopySub(B3, 0, 0, 3, 1, tempRef_F2, i * 3, 0);
//			F2 = tempRef_F2.argvalue;
//		}
//		RefObject<Double> tempRef_F22 = new RefObject<Double>(F2);
//		GT.CORE.Matrix.MatrixTool.Sub(F, F2, tempRef_F22);
//		F2 = tempRef_F22.argvalue;
//		V = specialMulti(AT, F2);
//		return V;
//	}
//
//	/** 
//	 根据样本参数和参数对数利用最小二乘法计算7参数
//	 
//	 @param inputNum
//	 @param varNum
//	*/
//	public final void CalculateTrans7Param(double[][] inputNum, double varNum)
//	{
//		double[] hangLie = new double[8];
//		double[] aim = new double[7];
//		double[][] numberVar = new double[7][7];
//		double[][] rowCol = new double[10][10];
//
//		for (int i = 0; i < 7; i++)
//		{
//			for (int j = 0; j < 7; j++)
//			{
//				rowCol[i][j] = 0;
//			}
//		}
//
//		for (int i = 0; i < 3; i++)
//		{
//			rowCol[i][i] = varNum;
//
//		}
//
//		for (int j = 0; j < varNum; j++)
//		{
//			rowCol[0][3] = rowCol[0][3] + inputNum[j][0];
//			rowCol[0][5] = rowCol[0][5] - inputNum[j][2];
//			rowCol[0][6] = rowCol[0][6] + inputNum[j][1];
//			rowCol[1][3] = rowCol[1][3] + inputNum[j][1];
//			rowCol[1][4] = rowCol[1][4] + inputNum[j][2];
//			rowCol[1][6] = rowCol[1][6] - inputNum[j][0];
//			rowCol[2][3] = rowCol[2][3] + inputNum[j][2];
//			rowCol[2][4] = rowCol[2][4] - inputNum[j][1];
//			rowCol[2][5] = rowCol[2][5] + inputNum[j][0];
//			rowCol[3][0] = rowCol[3][0] + inputNum[j][0];
//			rowCol[3][1] = rowCol[3][1] + inputNum[j][1];
//			rowCol[3][2] = rowCol[3][2] + inputNum[j][2];
//			rowCol[3][3] = rowCol[3][3] + inputNum[j][0] * inputNum[j][0] + inputNum[j][1] * inputNum[j][1] + inputNum[j][2] * inputNum[j][2];
//			rowCol[4][1] = rowCol[4][1] + inputNum[j][2];
//			rowCol[4][2] = rowCol[4][2] - inputNum[j][1];
//			// rowCol[4, 4] = rowCol[4, 1] + inputNum[j, 2];
//			// rowCol[4, 2] = rowCol[4, 2] - inputNum[j, 1];
//			rowCol[4][4] = rowCol[4][4] + inputNum[j][2] * inputNum[j][2] + inputNum[j][1] * inputNum[j][1];
//			rowCol[4][5] = rowCol[4][5] - inputNum[j][0] * inputNum[j][1];
//			rowCol[4][6] = rowCol[4][6] - inputNum[j][0] * inputNum[j][2];
//			rowCol[5][0] = rowCol[5][0] - inputNum[j][2];
//			rowCol[5][2] = rowCol[5][2] + inputNum[j][0];
//			rowCol[5][4] = rowCol[5][4] - inputNum[j][1] * inputNum[j][0];
//			rowCol[5][5] = rowCol[5][5] + inputNum[j][2] * inputNum[j][2] + inputNum[j][0] * inputNum[j][0];
//			rowCol[5][6] = rowCol[5][6] - inputNum[j][1] * inputNum[j][2];
//			rowCol[6][0] = rowCol[6][0] + inputNum[j][1];
//			rowCol[6][1] = rowCol[6][1] - inputNum[j][0];
//			rowCol[6][4] = rowCol[6][4] - inputNum[j][0] * inputNum[j][2];
//			rowCol[6][5] = rowCol[6][5] - inputNum[j][1] * inputNum[j][2];
//			rowCol[6][6] = rowCol[6][6] + inputNum[j][0] * inputNum[j][0] + inputNum[j][1] * inputNum[j][1];
//			aim[0] = aim[0] + inputNum[j][3] - inputNum[j][0];
//			aim[1] = aim[1] + inputNum[j][4] - inputNum[j][1];
//			aim[2] = aim[2] + inputNum[j][5] - inputNum[j][2];
//			aim[3] = aim[3] + inputNum[j][0] * (inputNum[j][3] - inputNum[j][0]) + inputNum[j][1] * (inputNum[j][4] - inputNum[j][1]) + inputNum[j][2] * (inputNum[j][5] - inputNum[j][2]);
//			aim[4] = aim[4] + inputNum[j][2] * (inputNum[j][4] - inputNum[j][1]) - inputNum[j][1] * (inputNum[j][5] - inputNum[j][2]);
//			aim[5] = aim[5] - inputNum[j][2] * (inputNum[j][3] - inputNum[j][0]) + inputNum[j][0] * (inputNum[j][5] - inputNum[j][2]);
//			aim[6] = aim[6] + inputNum[j][1] * (inputNum[j][3] - inputNum[j][0]) - inputNum[j][0] * (inputNum[j][4] - inputNum[j][1]);
//
//		}
//
//		for (int i = 0; i < 7; i++)
//		{
//			for (int j = 0; j < 7; j++)
//			{
//				numberVar[i][j] = rowCol[i][j];
//			}
//		}
//
//		//计算n阶行列式值
//		hangLie[0] = ColRow(7, rowCol);
//
//		if (hangLie[0] == 0)
//		{
//			return;
//		}
//		else
//		{
//			for (int k = 1; k <= 7; k++)
//			{
//				for (int i = 0; i <= 6; i++)
//				{
//					for (int j = 0; j <= 6; j++)
//					{
//						rowCol[i][j] = numberVar[i][j];
//					}
//				}
//				for (int i = 0; i <= 6; i++)
//				{
//					rowCol[i][k - 1] = aim[i];
//				}
//				hangLie[k] = ColRow(7, rowCol);
//			}
//
//			this.setDX(hangLie[1] / hangLie[0]);
//			this.setDY(hangLie[2] / hangLie[0]);
//			this.setDZ(hangLie[3] / hangLie[0]);
//			this.setK(hangLie[4] / hangLie[0]);
//			this.setRX(hangLie[5] / hangLie[0]);
//			this.setRY(hangLie[6] / hangLie[0]);
//			this.setRZ(hangLie[7] / hangLie[0]);
//		}
//	}
//
//	private double ColRow(int step, double[][] rowCol)
//	{
//		double temp;
//		double result;
//
//		for (int k = 0; k < step - 1; k++)
//		{
//			for (int i = k + 1; i < step; i++)
//			{
//				temp = rowCol[i][k];
//				for (int j = k; j < step; j++)
//				{
//					if (rowCol[k][k] != 0)
//					{
//						rowCol[i][j] = rowCol[i][j] - temp * rowCol[k][j] / rowCol[k][k];
//					}
//					else
//					{
//						for (int l = k + 1; l < step; l++) //寻找第k个元素不是0的行
//						{
//							if (rowCol[l][k] != 0)
//							{
//								for (int m = k; m < step; m++) //将找到的行与第k行互换
//								{
//									temp = rowCol[k][m];
//									rowCol[k][m] = -rowCol[l][m];
//									rowCol[l][m] = temp;
//								}
//								break;
//							}
//							if (l == step + 1)
//							{
//								result = 0;
//								return result;
//							}
//						}
//					}
//				}
//			}
//
//		}
//		temp = 1;
//		for (int i = 0; i < step; i++)
//		{
//			temp = temp * rowCol[i][i];
//
//		}
//		result = temp;
//		return result;
//	}
//
//
//	private boolean isSmall()
//	{
//		double s = 0;
//		for (int i = 0; i < 7; i++)
//		{
//			s += Math.abs(values[i][0]);
//		}
//		if (s < 0.0000001)
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
//
//	/** 
//	 根据7参数进行坐标转换
//	 
//	 @param x1
//	 @param y1
//	 @param z1
//	 @param x2
//	 @param y2
//	 @param z2
//	*/
//	public final void TransCoord(double x1, double y1, double z1, RefObject<Double> x2, RefObject<Double> y2, RefObject<Double> z2)
//	{
//
//		double[][] Xi = { { x1 }, { y1 }, { z1 } };
//		double[][] DX1 = { { getDX() }, { getDY() }, { getDZ() } };
//		double[][] tY = new double[3][1];
//		double[][] K1 = { { 1 + getK() } };
//
//		double[][] M = GetM();
//		RefObject<Double> tempRef_tY = new RefObject<Double>(tY);
//		GT.CORE.Matrix.MatrixTool.Multi(Xi, K1, tempRef_tY);
//		tY = tempRef_tY.argvalue;
//		RefObject<Double> tempRef_tY2 = new RefObject<Double>(tY);
//		GT.CORE.Matrix.MatrixTool.Multi(M, tY, tempRef_tY2);
//		tY = tempRef_tY2.argvalue;
//		RefObject<Double> tempRef_tY3 = new RefObject<Double>(tY);
//		GT.CORE.Matrix.MatrixTool.Add(tY, DX1, tempRef_tY3);
//		tY = tempRef_tY3.argvalue;
//		x2.argvalue = tY[0][0];
//		y2.argvalue = tY[1][0];
//		z2.argvalue = tY[2][0];
//
//		//x2 = (1+K)*(x1)+(-z1*RY+y1*RZ)+DX;
//		//y2 = (1 + K) * (y1) + (z1 * RX + x1 * RZ) + DY;
//		//z2 = (1 + K) * (z1) + (-y1 * RX + x1 * RY) + DZ;
//
////       *
////        *   AimX:=(1+ChangeK)*(SourceX)+(-SourceZ*Wy+SourceY*Wz)+DX;
////            AimY:=(1+ChangeK)*(SourceY)+(SourceZ*Wx-SourceX*Wz)+DY;
////            AimZ:=(1+ChangeK)*(SourceZ)+(-SourceY*Wx+SourceX*Wy)+DZ;
////        * *
//	}
//
//	/** 
//	 X轴偏依移量
//	 
//	*/
//	public final double getDX()
//	{
//		return values[0][0];
//	}
//	public final void setDX(double value)
//	{
//		values[0][0] = value;
//	}
//
//	/** 
//	 Y轴偏依移量
//	 
//	*/
//	public final double getDY()
//	{
//		return values[1][0];
//	}
//	public final void setDY(double value)
//	{
//		values[1][0] = value;
//	}
//
//	/** 
//	 Z轴偏依移量
//	 
//	*/
//	public final double getDZ()
//	{
//		return values[2][0];
//	}
//	public final void setDZ(double value)
//	{
//		values[2][0] = value;
//	}
//
//	/** 
//	 X轴旋转角度
//	 
//	*/
//	public final double getRX()
//	{
//		return values[3][0];
//	}
//	public final void setRX(double value)
//	{
//		values[3][0] = value;
//	}
//
//	/** 
//	 Y轴旋转角度
//	 
//	*/
//	public final double getRY()
//	{
//		return values[4][0];
//	}
//	public final void setRY(double value)
//	{
//		values[4][0] = value;
//	}
//
//	/** 
//	 Z轴旋转角度
//	 
//	*/
//	public final double getRZ()
//	{
//		return values[5][0];
//	}
//	public final void setRZ(double value)
//	{
//		values[5][0] = value;
//	}
//
//	/** 
//	 尺度
//	 
//	*/
//	public final double getK()
//	{
//		return values[6][0];
//	}
//	public final void setK(double value)
//	{
//		values[6][0] = value;
//	}
}
