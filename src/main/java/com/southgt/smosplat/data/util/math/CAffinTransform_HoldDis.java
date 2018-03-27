package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName CAffinTransform_HoldDis
 * @Description 保距变换，点的相对位置保持不变           
 * 
 * @version v1.0.0 
 * @author 姚家俊
 * 
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年6月14日     姚家俊       v1.0.0        create</p>
 *
 *
 */

public class CAffinTransform_HoldDis
{
	public double[] DesignPoints;
	public double[] SourcePoints;
	public int iNum;
	public double[] CoordTransMatrix = new double[16];
	public double[] Invert_CoordTransMatrix = new double[16];
	private Math_ReturnParam mathReturnParam;

	public CAffinTransform_HoldDis()
	{
		this.mathReturnParam=new Math_ReturnParam();
	}


	public CAffinTransform_HoldDis(double[] p1, double[] p2, int N)
	{
		DesignPoints = p2;
		SourcePoints = p1;
		iNum = N;

		double eps = 1e-16;
		//检查所给的点是不是在同一条直线上
		int i, j, iFlag1 = 0, iFlag2 = 0;
		double[] k1 = new double[3];
		double[] k2 = new double[3];
		k1[0] = DesignPoints[3] - DesignPoints[0];
		k1[1] = DesignPoints[4] - DesignPoints[1];
		k1[2] = DesignPoints[5] - DesignPoints[2];

		k2[0] = SourcePoints[3] - SourcePoints[0];
		k2[1] = SourcePoints[4] - SourcePoints[1];
		k2[2] = SourcePoints[5] - SourcePoints[2];

		double[] tmp1 = new double[3];
		double[] tmp2 = new double[3];
		for (i = 2; i < iNum; i++)
		{
			tmp1[0] = DesignPoints[3 * i] - DesignPoints[0];
			tmp1[1] = DesignPoints[3 * i + 1] - DesignPoints[1];
			tmp1[2] = DesignPoints[3 * i + 2] - DesignPoints[2];

			tmp2[0] = SourcePoints[3 * i] - SourcePoints[0];
			tmp2[1] = SourcePoints[3 * i + 1] - SourcePoints[1];
			tmp2[2] = SourcePoints[3 * i + 2] - SourcePoints[2];

			if (Math.abs(tmp1[0] * k1[1] - tmp1[1] * k1[0]) < eps && Math.abs(tmp1[0] * k1[2] - tmp1[2] * k1[0]) < eps && Math.abs(tmp1[1] * k1[2] - tmp1[2] * k1[1]) < eps)
			{
				iFlag1 += 1;
			}
			if (Math.abs(tmp2[0] * k2[1] - tmp2[1] * k2[0]) < eps && Math.abs(tmp2[0] * k2[2] - tmp2[2] * k2[0]) < eps && Math.abs(tmp2[1] * k2[2] - tmp2[2] * k2[1]) < eps)
			{
				iFlag2 += 1;
			}
		}

		if ((iFlag1 == iNum - 2) || (iFlag2 == iNum - 2))
		{
			return; //所有的设计点或者(和)所有的实测点共线
		}

		double[] ps = new double[4];
		double[] pd = new double[4];
		if (iNum == 3)
		{
			//由不共线的三点计算平面的方程的参数
			double[] v1 = new double[3];
			double[] v2 = new double[3];

			//计算测量点的平面参数
			v1[0] = SourcePoints[3] - SourcePoints[0];
			v1[1] = SourcePoints[4] - SourcePoints[1];
			v1[2] = SourcePoints[5] - SourcePoints[2];

			v2[0] = SourcePoints[6] - SourcePoints[0];
			v2[1] = SourcePoints[7] - SourcePoints[1];
			v2[2] = SourcePoints[8] - SourcePoints[2];

			ps[0] = v1[1] * v2[2] - v1[2] * v2[1];
			ps[1] = v1[2] * v2[0] - v1[0] * v2[2];
			ps[2] = v1[0] * v2[1] - v1[1] * v2[0];

			ps[3] = -(ps[0] * SourcePoints[0] + ps[1] * SourcePoints[1] + ps[2] * SourcePoints[2]);

			//计算设计点的平面参数
			v1[0] = DesignPoints[3] - DesignPoints[0];
			v1[1] = DesignPoints[4] - DesignPoints[1];
			v1[2] = DesignPoints[5] - DesignPoints[2];

			v2[0] = DesignPoints[6] - DesignPoints[0];
			v2[1] = DesignPoints[7] - DesignPoints[1];
			v2[2] = DesignPoints[8] - DesignPoints[2];

			pd[0] = v1[1] * v2[2] - v1[2] * v2[1];
			pd[1] = v1[2] * v2[0] - v1[0] * v2[2];
			pd[2] = v1[0] * v2[1] - v1[1] * v2[0];

			pd[3] = -(pd[0] * DesignPoints[0] + pd[1] * DesignPoints[1] + pd[2] * DesignPoints[2]);
		}
		else if (iNum > 3)
		{
			planefit(SourcePoints, iNum);
			ps = this.mathReturnParam.getPlaneparam();
			planefit(DesignPoints, iNum);
			pd = this.mathReturnParam.getPlaneparam();
		}

		double aas, bbs, ccs, dds, phis, aad, bbd, ccd, ddd, phid;
		dds = ps[3];
		phis = plane_xoy_angle(ps);
		aas = ps[0];
		bbs = ps[1];
		ccs = ps[2];

		ddd = pd[3];
		phid = plane_xoy_angle(pd);
		aad = pd[0];
		bbd = pd[1];
		ccd = pd[2];

		double[] ms = new double[16];
		double[] invert_ms = new double[16];
		if (Math.abs(aas) < eps && Math.abs(bbs) < eps && !(Math.abs(ccs) < eps))
		{
			for (i = 0; i < 4; i++)
			{
				for (j = 0; j < 4; j++)
				{
					if (i == j)
					{
						ms[4 * i + j] = 1;
						invert_ms[4 * i + j] = 1;
					}
					else
					{
						ms[4 * i + j] = 0;
						invert_ms[4 * i + j] = 0;
					}
				}
			}
		}
		else if (Math.abs(bbs) < eps)
		{
			double[] ms1 = new double[16];
			double[] ms2 = new double[16];
			parallel_move(0, dds / aas);
			ms1 = this.mathReturnParam.getParallelMoveMatrix();
			rotate_around_axis(1, -phis);
			ms2 = this.mathReturnParam.getRotatedMatrix();
			matrix_multi_matrix(ms2, ms1, 4, 4, 4);
			ms = this.mathReturnParam.getMr();

			rotate_around_axis(1, phis);
			ms2 = this.mathReturnParam.getRotatedMatrix();
			parallel_move(0, -dds / aas);
			ms1 = this.mathReturnParam.getParallelMoveMatrix();
			matrix_multi_matrix(ms1, ms2, 4, 4, 4);
			invert_ms = this.mathReturnParam.getMr();
		}
		else
		{
			double[] ms1 = new double[16];
			double[] ms2 = new double[16];
			double[] ms3 = new double[16];
			double[] ms_tmp = new double[16];
			double alphas = Math.atan(-aas / bbs);

			parallel_move(0, dds / aas);
			ms1 = this.mathReturnParam.getParallelMoveMatrix();
			rotate_around_axis(2, -alphas);
			ms2 = this.mathReturnParam.getRotatedMatrix();
			rotate_around_axis(0, -phis);
			ms3 = this.mathReturnParam.getRotatedMatrix();
			matrix_multi_matrix(ms2, ms1, 4, 4, 4);
			ms_tmp = this.mathReturnParam.getMr();
			matrix_multi_matrix(ms3, ms_tmp, 4, 4, 4);
			ms =this.mathReturnParam.getMr();

			rotate_around_axis(0, phis);
			ms3 = this.mathReturnParam.getRotatedMatrix();
			rotate_around_axis(2, alphas);
			ms2 = this.mathReturnParam.getRotatedMatrix();
			parallel_move(0, -dds / aas);
			ms1 = this.mathReturnParam.getParallelMoveMatrix();
			matrix_multi_matrix(ms2, ms3, 4, 4, 4);
			ms_tmp = this.mathReturnParam.getMr();
			matrix_multi_matrix(ms1, ms_tmp, 4, 4, 4);
			invert_ms = this.mathReturnParam.getMr();
		}

		double[] md = new double[16];
		double[] invert_md = new double[16];
		if (Math.atan(aad) < eps && Math.atan(bbd) < eps && !(Math.abs(ccd) < eps))
		{
			for (i = 0; i < 4; i++)
			{
				for (j = 0; j < 4; j++)
				{
					if (i == j)
					{
						md[4 * i + j] = 1;
						invert_md[4 * i + j] = 1;
					}
					else
					{
						md[4 * i + j] = 0;
						invert_md[4 * i + j] = 0;
					}
				}
			}
		}
		else if (Math.abs(bbd) < eps)
		{
			double[] md1 = new double[16];
			double[] md2 = new double[16];
			parallel_move(0, ddd / aad);
			md1 = this.mathReturnParam.getParallelMoveMatrix();
			rotate_around_axis(1, -phid);
			md2 = this.mathReturnParam.getRotatedMatrix();
			matrix_multi_matrix(md2, md1, 4, 4, 4);
			md = this.mathReturnParam.getMr();

			rotate_around_axis(1, phid);
			md2 = this.mathReturnParam.getRotatedMatrix();
			parallel_move(0, -ddd / aad);
			md1 = this.mathReturnParam.getParallelMoveMatrix();
			matrix_multi_matrix(md1, md2, 4, 4, 4);
			invert_md = this.mathReturnParam.getMr();
		}
		else
		{
			double[] md1 = new double[16];
			double[] md2 = new double[16];
			double[] md3 = new double[16];
			double[] md_tmp = new double[16];
			double alphad = Math.atan(-aad / bbd);

			parallel_move(0, ddd / aad);
			md1 = this.mathReturnParam.getParallelMoveMatrix();
			rotate_around_axis(2, -alphad);
			md2 = this.mathReturnParam.getRotatedMatrix();
			rotate_around_axis(0, -phid);
			md3 = this.mathReturnParam.getRotatedMatrix();
			matrix_multi_matrix(md2, md1, 4, 4, 4);
			md_tmp = this.mathReturnParam.getMr();
			matrix_multi_matrix(md3, md_tmp, 4, 4, 4);
			md = this.mathReturnParam.getMr();

			rotate_around_axis(0, phid);
			md3 = this.mathReturnParam.getRotatedMatrix();
			rotate_around_axis(2, alphad);
			md2 = this.mathReturnParam.getRotatedMatrix();
			parallel_move(0, -ddd / aad);
			md1 = this.mathReturnParam.getParallelMoveMatrix();
			matrix_multi_matrix(md2, md3, 4, 4, 4);
			md_tmp = this.mathReturnParam.getMr();
			matrix_multi_matrix(md1, md_tmp, 4, 4, 4);
			invert_md = this.mathReturnParam.getMr();
		}

		double[] tmp_SourcePoints = new double[3 * iNum];
		double[] tmp_DesignPoints = new double[3 * iNum];
		for (i = 0; i < iNum; i++)
		{
			tmp_SourcePoints[3 * i] = ms[0] * SourcePoints[3 * i] + ms[1] * SourcePoints[3 * i + 1] + ms[2] * SourcePoints[3 * i + 2] + ms[3];
			tmp_SourcePoints[3 * i + 1] = ms[4] * SourcePoints[3 * i] + ms[5] * SourcePoints[3 * i + 1] + ms[6] * SourcePoints[3 * i + 2] + ms[7];
			tmp_SourcePoints[3 * i + 2] = ms[8] * SourcePoints[3 * i] + ms[9] * SourcePoints[3 * i + 1] + ms[10] * SourcePoints[3 * i + 2] + ms[11];


			tmp_DesignPoints[3 * i] = md[0] * DesignPoints[3 * i] + md[1] * DesignPoints[3 * i + 1] + md[2] * DesignPoints[3 * i + 2] + md[3];
			tmp_DesignPoints[3 * i + 1] = md[4] * DesignPoints[3 * i] + md[5] * DesignPoints[3 * i + 1] + md[6] * DesignPoints[3 * i + 2] + md[7];
			tmp_DesignPoints[3 * i + 2] = md[8] * DesignPoints[3 * i] + md[9] * DesignPoints[3 * i + 1] + md[10] * DesignPoints[3 * i + 2] + md[11];
		}

		double[] xoy_matrix = new double[16];
		double[] invert_xoy_matrix = new double[16];
		planePoints_xyMoveRotate(tmp_DesignPoints, tmp_SourcePoints, iNum);
		xoy_matrix = this.mathReturnParam.getTransMatrix();
		invert_xoy_matrix = this.mathReturnParam.getInvertTransMatrix();

		double[] tmp_matrix = new double[16];
		matrix_multi_matrix(xoy_matrix, ms, 4, 4, 4);
		tmp_matrix = this.mathReturnParam.getMr();
		matrix_multi_matrix(invert_md, tmp_matrix, 4, 4, 4);
		CoordTransMatrix = this.mathReturnParam.getMr();

		matrix_multi_matrix(invert_ms, invert_xoy_matrix, 4, 4, 4);
		tmp_matrix = this.mathReturnParam.getMr();
		matrix_multi_matrix(tmp_matrix, md, 4, 4, 4);
		Invert_CoordTransMatrix = this.mathReturnParam.getMr();

	}

	//**********************************************************************
//    这个函数计算一元三次方程a*x^3 + b*x^2 + c*x + d = 0的实数根，采用盛金
//      公式来求解。当有虚根的时候，实数根的个数iNumReal=1，储存方程根的数组
//      RootsArray的第一个元素存放实数根，第二个和第三个元素分别存放共轭虚数根
//      的实部和虚部。									                    
	//**********************************************************************
	private void realRootsOfCubicEquations(double a, double b, double c, double d)
	{
		double eps = 1e-16;

		double A = b * b - 3 * a * c;
		double B = b * c - 9 * a * d;
		double C = c * c - 3 * b * d;
		double Delta = B * B - 4 * A * C;
		double[] rootsArray =new double[3];
		if (Math.abs(A) < eps && Math.abs(B) < eps)
		{
			this.mathReturnParam.setiNumReal(3);
			rootsArray[0] = rootsArray[1] = rootsArray[2] = -c / b;
			return;
		}

		if (Delta > 0.0 && Math.abs(Delta) >= eps)
		{
			this.mathReturnParam.setiNumReal(1);
			double Y1, Y2;
			Y1 = A * b + 3 * a * (-B + Math.sqrt(Delta)) / 2;
			Y2 = A * b + 3 * a * (-B - Math.sqrt(Delta)) / 2;

			rootsArray[0] = (-b - Math.pow(Y1, 1.0 / 3.0) - Math.pow(Y2, 1.0 / 3.0)) / (3 * a);
			rootsArray[1] = (-2 * b + Math.pow(Y1, 1.0 / 3.0) + Math.pow(Y2, 1.0 / 3.0)) / (6 * a);
			rootsArray[2] = Math.sqrt(3.0) * (Math.pow(Y1, 1.0 / 3.0) - Math.pow(Y2, 1.0 / 3.0)) / (6 * a);

			return;
		}

		if (Math.abs(Delta) < eps)
		{
			this.mathReturnParam.setiNumReal(3);
			rootsArray[0] = -b / a + B / A;
			rootsArray[1] = rootsArray[2] = -0.5 * B / A;

			return;
		}

		if (Delta < 0.0 && Math.abs(Delta) >= eps)
		{
			this.mathReturnParam.setiNumReal(3);

			double T = 0.5 * (2 * A * b - 3 * a * B) / Math.pow(A, 3.0 / 2.0);
			double theta = Math.acos(T);

			rootsArray[0] = (-b - 2 * Math.sqrt(A) * Math.cos(theta / 3)) / (3.0 * a);
			rootsArray[1] = (-b + Math.sqrt(A) * (Math.cos(theta / 3) + Math.sqrt(3) * Math.sin(theta / 3.0))) / (3.0 * a);
			rootsArray[2] = (-b + Math.sqrt(A) * (Math.cos(theta / 3) - Math.sqrt(3) * Math.sin(theta / 3.0))) / (3.0 * a);
			this.mathReturnParam.setRootsArray(rootsArray);
			return;
		}
	}
//
	//**********************************************************************
//    这个函数对于给定的点列拟合一个平面，点列的坐标存储在数组dArr中，点的个数
//      为iLength，第i个点的x，y和z坐标分别存储在数组dArr的第3×i，3×i+1，
//      3×i + 2的位置上. 输出参数为平面的参数数组planeparam，其中平面的方程为
//      planeparam[0]*x + planeparam[1]*y + planeparam[2]*z + planeparam[4] = 0
//                                                                           
	//**********************************************************************
	private void planefit(double[] dArr, int iLength)
	{
		double eps = 1e-16;

		int i;
		double sum1, sum2, sum3;
		double[] planeparam=new double[4];

		sum1 = sum2 = sum3 = 0.0;
		for (i = 0; i < iLength; i++)
		{
			sum1 += dArr[3 * i];
			sum2 += dArr[3 * i + 1];
			sum3 += dArr[3 * i + 2];
		}
		double x_mean = sum1 / iLength;
		double y_mean = sum2 / iLength;
		double z_mean = sum3 / iLength;

		double a11, a12, a13, a21, a22, a23, a31, a32, a33;
		a11 = a12 = a13 = a22 = a23 = a33 = 0.0;

		double delta_x, delta_y, delta_z;
		for (i = 0; i < iLength; i++)
		{
			delta_x = dArr[3 * i] - x_mean;
			delta_y = dArr[3 * i + 1] - y_mean;
			delta_z = dArr[3 * i + 2] - z_mean;

			a11 += delta_x * delta_x;
			a12 += delta_x * delta_y;
			a13 += delta_x * delta_z;
			a22 += delta_y * delta_y;
			a23 += delta_y * delta_z;
			a33 += delta_z * delta_z;
		}

		a21 = a12;
		a31 = a13;
		a32 = a23;

		double a, b, c, d;
		a = -1;
		b = a11 + a22 + a33;
		c = a12 * a21 - a11 * a22 + a13 * a31 + a23 * a32 - a11 * a33 - a22 * a33;
		d = -a13 * a22 * a31 + a12 * a23 * a31 + a13 * a21 * a32 - a11 * a23 * a32 - a12 * a21 * a33 + a11 * a22 * a33;

		int iNumReal = 0; //初始化
		double[] RootsArray = new double[3];
		realRootsOfCubicEquations(a, b, c, d);

		double chi = 0; //初始化
		if (iNumReal == 1)
		{
			if ((RootsArray[0] > 0.0 && Math.abs(RootsArray[0]) > eps) || Math.abs(RootsArray[0]) < eps)
			{
				chi = RootsArray[0];
			}
			else
			{
				return;
			}
		}
		else if (iNumReal == 3)
		{
			double tmp = 0.0;
			for (i = 0; i < 3; i++)
			{
				if ((RootsArray[i] > 0.0 && Math.abs(RootsArray[i]) > eps) || Math.abs(RootsArray[i]) < eps)
				{
					tmp = tmp < RootsArray[i] ? tmp : RootsArray[i];
				}
			}

			chi = tmp;
		}

		double p, q;
		p = (a13 * (a22 - chi) - a12 * a23) / (a12 * a12 - (a11 - chi) * (a22 - chi));
		q = (a23 * (a11 - chi) - a12 * a13) / (a12 * a12 - (a11 - chi) * (a22 - chi));

		double t = 1.0 / Math.sqrt(p * p + q * q + 1.0);
		double sign = p > 0 ? 1.0 : (-1.0);
		planeparam[0] = sign * p * t;
		planeparam[1] = sign * q * t;
		planeparam[2] = sign * t;
		planeparam[3] = -sign * (p * x_mean + q * y_mean + z_mean) * t;
		this.mathReturnParam.setPlaneparam(planeparam);

	}

	//**********************************************************************
	// 计算矩阵和向量的乘积，即rv = ma×v                                   
	//**********************************************************************
	private void matrxi_multi_vector(double[] ma, double[] v, int m, int n)
	{
		double sum;
		double[] rv=new double[m*n];
		for (int i = 0; i < m; i++)
		{
			sum = 0.0;
			for (int j = 0; j < n; j++)
			{
				sum += ma[i * n + j] * v[j];
			}
			rv[i] = sum;
		}
		this.mathReturnParam.setRv(rv);
	}

	//**********************************************************************
	// 计算矩阵m1和m2的乘积，即mr = m1×m2								    
	//**********************************************************************
	private void matrix_multi_matrix(double[] m1, double[] m2, int m, int p, int n)
	{
		int i, j, k;
		double tmp = 0.0;
		double[] mr=new double[m*n];

		for (i = 0; i < m; i++)
		{
			for (j = 0; j < n; j++)
			{
				for (k = 0; k < p; k++)
				{
					tmp += m1[p * i + k] * m2[n * k + j];
				}
				mr[n * i + j] = tmp;
				tmp = 0.0;
			}
		}
		this.mathReturnParam.setMr(mr);
	}

	//**********************************************************************
	// 计算平面与XOY平面的夹角                                              
	//**********************************************************************
	private double plane_xoy_angle(double[] p)
	{
		double alpha, eps = 1e-16;
		if (Math.abs(p[1]) < eps)
		{
			alpha = Math.acos(0.0);
		}
		else
		{
			alpha = Math.atan(-p[0] / p[1]);
		}

		if (Math.abs(p[2]) < eps)
		{
			return Math.acos(0.0);
		}
		else
		{
			double tmp = (p[0] * Math.sin(alpha) - p[1] * Math.cos(alpha)) / p[2];
			return Math.atan(tmp);
		}

	}

	//**********************************************************************
//    计算绕x轴或者y轴或者z轴旋转一个角度angle的坐标变换矩阵。
//   iFlag标识出是绕那根坐标轴旋转，0表示绕x轴，1表示绕y轴，2表示绕z轴   
	//**********************************************************************
	private void rotate_around_axis(int iFlag, double angle)
	{
		double[] matrix=new double[16];
		if (iFlag == 0)
		{
			matrix[0] = matrix[15] = 1;
			matrix[5] = matrix[10] = Math.cos(angle);
			matrix[6] = -Math.sin(angle);
			matrix[9] = Math.sin(angle);
			matrix[1] = matrix[2] = matrix[3] = matrix[4] = 0.0;
			matrix[7] = matrix[8] = matrix[11] = matrix[12] = 0.0;
			matrix[13] = matrix[14] = 0.0;
		}
		else if (iFlag == 1)
		{
			matrix[5] = matrix[15] = 1;
			matrix[0] = matrix[10] = Math.cos(angle);
			matrix[2] = Math.sin(angle);
			matrix[8] = -Math.sin(angle);
			matrix[1] = matrix[3] = matrix[4] = matrix[6] = 0.0;
			matrix[7] = matrix[9] = matrix[11] = matrix[12] = 0.0;
			matrix[13] = matrix[14] = 0.0;
		}
		else if (iFlag == 2)
		{
			matrix[10] = matrix[15] = 1;
			matrix[0] = matrix[5] = Math.cos(angle);
			matrix[1] = -Math.sin(angle);
			matrix[4] = Math.sin(angle);
			matrix[2] = matrix[3] = matrix[6] = matrix[7] = 0.0;
			matrix[8] = matrix[9] = matrix[11] = matrix[12] = 0.0;
			matrix[13] = matrix[14] = 0.0;
		}
		this.mathReturnParam.setRotatedMatrix(matrix);
	}

	//**********************************************************************
//    做平移变换的坐标变换距阵，iFlag=0，表示沿x轴平移，1表示沿y轴平移，2
//   表示沿z轴平移                                                        
	//**********************************************************************
	private void parallel_move(int iFlag, double move)
	{
		double[] matrix=new double[16];
		matrix[0] = 1;
		matrix[1 * 4 + 1] = 1;
		matrix[2 * 4 + 2] = 1;
		matrix[3 * 4 + 3] = 1;
		matrix[1] = matrix[2] = matrix[3] = matrix[4] = 0.0;
		matrix[6] = matrix[7] = matrix[8] = matrix[9] = 0.0;
		matrix[11] = matrix[12] = matrix[13] = matrix[14] = 0.0;

		if (iFlag == 0)
		{
			matrix[3] = move;
		}
		else if (iFlag == 1)
		{
			matrix[7] = move;
		}
		else if (iFlag == 2)
		{
			matrix[11] = move;
		}
		this.mathReturnParam.setParallelMoveMatrix(matrix);
	}

	//**********************************************************************
//    寻找变换，使得实测点在经过坐标变换之后到水平面上，经过水平面内的平移和
//      旋转以后，与对应的设计点列的距离的平方之和最小。点列的个数为iLength，
//      设计平面上的点的x和y坐标存储在数组DesignPoints中，经过变换的实测点列的
//      坐标x，y和z的值存放在数组SourcePoints中.所有存放坐标信息的数组：3×i的
//      位置存放第i个点的x坐标的值，3×i+1的位置存放第i个点的y坐标的值，3×i+2
//      的位置存放第i个点的z坐标的值。输出数组TransMatrix为坐标变换的4×4阶的
//      系数矩阵。					   							            
	//**********************************************************************
	private void xyMoveAndRotate(double[] DesignPoints, double[] tmp_SourcePoints, int iLength)
	{
		int i;
		double tmp, sum1, sum2, sum3;
		double[] transMatrix=new double[16];
		double[] invert_TransMatrix=new double[16];

		sum1 = sum2 = sum3 = 0.0;
		for (i = 0; i < iLength; i++)
		{
			sum1 += tmp_SourcePoints[3 * i];
			sum2 += tmp_SourcePoints[3 * i + 1];
			sum3 += tmp_SourcePoints[3 * i + 2];
		}
		double meanxs = sum1 / iLength;
		double meanys = sum2 / iLength;
		double meanzs = sum3 / iLength;

		sum1 = sum2 = sum3 = 0.0;
		for (i = 0; i < iLength; i++)
		{
			sum1 += DesignPoints[3 * i];
			sum2 += DesignPoints[3 * i + 1];
			sum3 += DesignPoints[3 * i + 2];
		}
		double meanxd = sum1 / iLength;
		double meanyd = sum2 / iLength;
		double meanzd = sum3 / iLength;

		double deltaxd, deltaxs, deltayd, deltays;

		tmp = 0.0;
		for (i = 0; i < iLength; i++)
		{
			deltaxd = DesignPoints[3 * i] - meanxd;
			deltaxs = tmp_SourcePoints[3 * i] - meanxs;
			deltayd = DesignPoints[3 * i + 1] - meanyd;
			deltays = tmp_SourcePoints[3 * i + 1] - meanys;

			tmp += deltaxd * deltaxs + deltayd * deltays;
		}
		double a = tmp;

		tmp = 0.0;
		for (i = 0; i < iLength; i++)
		{
			deltaxd = DesignPoints[3 * i] - meanxd;
			deltaxs = tmp_SourcePoints[3 * i] - meanxs;
			deltayd = DesignPoints[3 * i + 1] - meanyd;
			deltays = tmp_SourcePoints[3 * i + 1] - meanys;

			tmp += deltaxs * deltayd - deltays * deltaxd;
		}
		double b = tmp;

		if (Math.abs(a) < 1e-16 && Math.abs(b) < 1e-16)
		{
			this.mathReturnParam.setMindis(Double.MAX_VALUE);
			return;
		}

		double m = a / Math.sqrt(a * a + b * b);
		double n = b / Math.sqrt(a * a + b * b);

		double r, tmp1 = 0, tmp2 = 0;

		double[] ResultPoints = new double[2 * iLength];
		for (i = 0; i < iLength; i++)
		{
			deltaxs = tmp_SourcePoints[3 * i] - meanxs;
			deltays = tmp_SourcePoints[3 * i + 1] - meanys;

			ResultPoints[2 * i] = m * deltaxs - n * deltays + meanxd;
			ResultPoints[2 * i + 1] = n * deltaxs + m * deltays + meanyd;

			r = (ResultPoints[2 * i] - DesignPoints[3 * i]) * (ResultPoints[2 * i] - DesignPoints[3 * i]) + (ResultPoints[2 * i + 1] - DesignPoints[3 * i + 1]) * (ResultPoints[2 * i + 1] - DesignPoints[3 * i + 1]);

			tmp1 += r;
		}


		m = -m;
		n = -n;
		for (i = 0; i < iLength; i++)
		{
			deltaxs = tmp_SourcePoints[3 * i] - meanxs;
			deltays = tmp_SourcePoints[3 * i + 1] - meanys;

			ResultPoints[2 * i] = m * deltaxs - n * deltays + meanxd;
			ResultPoints[2 * i + 1] = n * deltaxs + m * deltays + meanyd;

			r = (ResultPoints[2 * i] - DesignPoints[3 * i]) * (ResultPoints[2 * i] - DesignPoints[3 * i]) + (ResultPoints[2 * i + 1] - DesignPoints[3 * i + 1]) * (ResultPoints[2 * i + 1] - DesignPoints[3 * i + 1]);

			tmp2 += r;
		}

		if (tmp1 > tmp2)
		{
			this.mathReturnParam.setMindis(Math.sqrt(tmp2));
			m = -a / Math.sqrt(a * a + b * b);
			n = -b / Math.sqrt(a * a + b * b);
		}
		else
		{
			this.mathReturnParam.setMindis(Math.sqrt(tmp1));
			m = a / Math.sqrt(a * a + b * b);
			n = b / Math.sqrt(a * a + b * b);
		}

		double dx = meanxd - m * meanxs + n * meanys;
		double dy = meanyd - n * meanxs - m * meanys;

		transMatrix[2] = transMatrix[6] = transMatrix[8] = transMatrix[9] = 0.0;
		transMatrix[12] = transMatrix[13] = transMatrix[14] = 0.0;
		transMatrix[0] = transMatrix[5] = m;
		transMatrix[1] = -n;
		transMatrix[4] = n;
		transMatrix[3] = dx;
		transMatrix[7] = dy;
		transMatrix[11] = meanzd - meanzs;
		transMatrix[10] = transMatrix[15] = 1.0;

		invert_TransMatrix[2] = invert_TransMatrix[6] = invert_TransMatrix[8] = invert_TransMatrix[9] = 0.0;
		invert_TransMatrix[12] = invert_TransMatrix[13] = invert_TransMatrix[14] = 0.0;
		invert_TransMatrix[0] = invert_TransMatrix[5] = m / (m * m + n * n);
		invert_TransMatrix[1] = n / (m * m + n * n);
		invert_TransMatrix[4] = -n / (m * m + n * n);
		invert_TransMatrix[3] = (-m * dx - n * dy) / (m * m + n * n);
		invert_TransMatrix[7] = (n * dx - m * dy) / (m * m + n * n);
		invert_TransMatrix[11] = meanzs - meanzd;
		invert_TransMatrix[10] = invert_TransMatrix[15] = 1.0;
		this.mathReturnParam.setInvertTransMatrix(invert_TransMatrix);
		this.mathReturnParam.setTransMatrix(transMatrix);
	}

	//**********************************************************************
	// 考虑还有镜面反射的情况                                               
	//**********************************************************************
	private void planePoints_xyMoveRotate(double[] DesignPoints, double[] tmp_SourcePoints, int iLength)
	{
		double[] transMatrix=new double[16];
		double[] invert_TransMatrix=new double[16];
		double mindis1, mindis2;
		mindis1 = mindis2 = 0; //初始化
		double[] m1 = new double[16];
		double[] invert_m1 = new double[16];
		double[] m2 = new double[16];
		double[] invert_m2 = new double[16];
		xyMoveAndRotate(DesignPoints, tmp_SourcePoints, iLength);
		mindis1 = this.mathReturnParam.getMindis();
		m1=this.mathReturnParam.getTransMatrix();
		invert_m1=this.mathReturnParam.getInvertTransMatrix();

		//考虑镜面反射的情况，只考虑沿YOZ平面的反射（因为位于水平面内的镜面反射，
		//如果是沿XOZ平面的镜面反射，则可以通过沿YOZ平面的反射再加上一个绕Z轴的
		//的旋转来实现。如果是沿任何一个垂直于XOY平面的反射的话，也可以类似通过
		//一个沿YOZ平面的反射再加上一个绕Z轴的旋转来实现）。
		double[] xtmp_SourcePoints = new double[3 * iLength];
		int i;
		for (i = 0; i < iLength; i++)
		{
			xtmp_SourcePoints[3 * i] = -tmp_SourcePoints[3 * i];
			xtmp_SourcePoints[3 * i + 1] = tmp_SourcePoints[3 * i + 1];
			xtmp_SourcePoints[3 * i + 2] = tmp_SourcePoints[3 * i + 2];
		}
		xyMoveAndRotate(DesignPoints, xtmp_SourcePoints, iLength);
		mindis2 = this.mathReturnParam.getMindis();
		m2=this.mathReturnParam.getTransMatrix();
		invert_m2=this.mathReturnParam.getInvertTransMatrix();

		if (mindis1 < mindis2)
		{
			for (i = 0; i < 16; i++)
			{
				transMatrix[i] = m1[i];
				invert_TransMatrix[i] = invert_m1[i];
			}
		}
		else
		{
			for (i = 0; i < 16; i++)
			{
				if (i % 4 == 0)
				{
					transMatrix[i] = -m2[i];
				}
				else
				{
					transMatrix[i] = m2[i];
				}

				if (i < 4)
				{
					invert_TransMatrix[i] = -invert_m2[i];
				}
				else
				{
					invert_TransMatrix[i] = invert_m2[i];
				}
			}
		}
		this.mathReturnParam.setTransMatrix(transMatrix);
		this.mathReturnParam.setInvertTransMatrix(invert_TransMatrix);

	}

	public final void coordtrans_old2new(double[] p_old)
	{
		double[] p_new=new double[3]; 
		p_new[0] = CoordTransMatrix[0] * p_old[0] + CoordTransMatrix[1] * p_old[1] + CoordTransMatrix[2] * p_old[2] + CoordTransMatrix[3];
		p_new[1] = CoordTransMatrix[4] * p_old[0] + CoordTransMatrix[5] * p_old[1] + CoordTransMatrix[6] * p_old[2] + CoordTransMatrix[7];
		p_new[2] = CoordTransMatrix[8] * p_old[0] + CoordTransMatrix[9] * p_old[1] + CoordTransMatrix[10] * p_old[2] + CoordTransMatrix[11];
		this.mathReturnParam.setP_new(p_new);
	}

	public final void coordtrans_new2old(double[] p_new)
	{
		double[] p_old=new double[3];
		p_old[0] = Invert_CoordTransMatrix[0] * p_new[0] + Invert_CoordTransMatrix[1] * p_new[1] + Invert_CoordTransMatrix[2] * p_new[2] + Invert_CoordTransMatrix[3];
		p_old[1] = Invert_CoordTransMatrix[4] * p_new[0] + Invert_CoordTransMatrix[5] * p_new[1] + Invert_CoordTransMatrix[6] * p_new[2] + Invert_CoordTransMatrix[7];
		p_old[2] = Invert_CoordTransMatrix[8] * p_new[0] + Invert_CoordTransMatrix[9] * p_new[1] + Invert_CoordTransMatrix[10] * p_new[2] + Invert_CoordTransMatrix[11];
		this.mathReturnParam.setP_old(p_old);
	}
}

