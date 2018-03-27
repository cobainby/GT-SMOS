package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName DatasAdjustment 
 * @Description 数据平差
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
public class DatasAdjustment
{
	private static final double PI = 3.1415926535897932384626433832795;
	public Station station1, station2;
	public MeasureData[] datas1;
	private MeasureData[] datas2;
	private Math_ReturnParam mathReturnParam;

	public DatasAdjustment(MeasureData[] datas1, MeasureData[] datas2)
	{
		this.datas1 = datas1;
		this.datas2 = datas2;
		this.mathReturnParam=new Math_ReturnParam();
	}

	private void coordCoun(double x0, double y0, double length, double bear)
	{
		this.mathReturnParam.getCoord().setdN(x0 + length * Math.cos(bear));
		this.mathReturnParam.getCoord().setdE(y0 + length * Math.sin(bear));

		return;
	}

	//计算i点到j点的方位角，并返回弧度值
	private double azimuth(double xi, double yi, double xj, double yj)
	{
		double t = Math.atan2(yj - yi, xj - xi);
		if (t < 0.0)
		{
			t = t + 2.0 * PI;
		}
		return t;
	}

	//计算点到点的平距，返回平距值
	private double distance2D(double xi, double yi, double xj, double yj)
	{
		return Math.sqrt((xj - xi) * (xj - xi) + (yj - yi) * (yj - yi));
	}

	//矩阵相乘(a[m][n]存放矩阵A的元素,b[n][k]存放矩阵B的元素,m:矩阵A与乘积矩阵C的行数,n:矩阵A的列数,矩阵B的行数,
	//k:矩阵B与乘积矩阵C的列数,c[m][k]返回乘积矩阵C=AB的元素
	private void trmul(double[] a, double[] b, int m, int n, int k, double[] c)
	{
		int i, j, l, u;
		for (i = 0; i <= m - 1; i++)
		{
			for (j = 0; j <= k - 1; j++)
			{
				u = i * k + j;
				c[u] = 0.0;
				for (l = 0; l <= n - 1; l++)
				{
					c[u] = c[u] + a[i * n + l] * b[l * k + j];
				}
			}
		}
		return;
	}

	private boolean rinv(double[] a, int n)
	{
		int[] Is;
		int[] Js;
		int i, j, k, l, u, v;
		double d, p;
		Is = new int[n];
		Js = new int[n];
		for (k = 0; k <= n - 1; k++)
		{
			d = 0.0;
			for (i = k; i <= n - 1; i++)
			{
				for (j = k; j <= n - 1; j++)
				{
					l = i * n + j;
					p = Math.abs(a[l]);
					if (p > d)
					{
						d = p;
						Is[k] = i;
						Js[k] = j;
					}
				}
			}
			if (d + 1.0 == 1.0)
			{
				return false;
			}
			if (Is[k] != k)
			{
				for (j = 0; j <= n - 1; j++)
				{
					u = k * n + j;
					v = Is[k] * n + j;
					p = a[u];
					a[u] = a[v];
					a[v] = p;
				}
			}
			if (Is[k] != k)
			{
				for (i = 0; i <= n - 1; i++)
				{
					u = i * n + k;
					v = i * n + Js[k];
					p = a[u];
					a[u] = a[v];
					a[v] = p;
				}
			}
			l = k * n + k;
			a[l] = 1.0 / a[l];
			for (j = 0; j <= n - 1; j++)
			{
				if (j != k)
				{
					u = k * n + j;
					a[u] = a[u] * a[l];
				}
			}
			for (i = 0; i <= n - 1; i++)
			{
				if (i != k)
				{
					for (j = 0; j <= n - 1; j++)
					{
						if (j != k)
						{
							u = i * n + j;
							a[u] = a[u] - a[i * n + k] * a[k * n + j];
						}
					}
				}
			}
			for (i = 0; i <= n - 1; i++)
			{
				if (i != k)
				{
					u = i * n + k;
					a[u] = -a[u] * a[l];
				}
			}
		}
		for (k = n - 1; k >= 0; k--)
		{
			if (Js[k] != k)
			{
				for (j = 0; j <= n - 1; j++)
				{
					u = k * n + j;
					v = Js[k] * n + j;
					p = a[u];
					a[u] = a[v];
					a[v] = p;
				}
			}
			if (Is[k] != k)
			{
				for (i = 0; i <= n - 1; i++)
				{
					u = i * n + k;
					v = i * n + Is[k];
					p = a[u];
					a[u] = a[v];
					a[v] = p;
				}
			}
		}
		return true;
	}

	private void getHouJiaoCoord(double dAngle11, double dAngle12, double dDis1, double dAngle21, double dAngle22, double dDis2, double dEast1, double dNorth1, double dHeight1, double dEast2, double dNorth2, double dHeight2)
	{
		double a1, a2, aa1, aa2, dDir;
		double dDis2D1, dDis2D2, dDis2D;
		double dTemp;
		boolean bSmallAngle;

		if (dAngle11 < Math.PI)
		{
			if (dAngle21 > dAngle11 && dAngle21 < (dAngle11 + Math.PI))
			{
				a1 = dAngle21 - dAngle11;
				bSmallAngle = true;
			}
			else
			{
				a1 = 2 * PI - dAngle21 + dAngle11;
				bSmallAngle = false;
			}
		}
		else
		{
			if (dAngle21 > dAngle11 || dAngle21 < (dAngle11 - PI))
			{
				if (dAngle21 > Math.PI)
				{
					a1 = dAngle21 - dAngle11;
				}
				else
				{
					a1 = 2 * PI - dAngle11 + dAngle21;
				}
				bSmallAngle = true;
			}
			else
			{
				a1 = dAngle11 - dAngle21;
				bSmallAngle = false;
			}
		}

		dDis2D1 = dDis1 * Math.abs(Math.sin(dAngle12));
		dDis2D2 = dDis2 * Math.abs(Math.sin(dAngle22));
		dDis2D = Math.sqrt((dEast1 - dEast2) * (dEast1 - dEast2) + (dNorth1 - dNorth2) * (dNorth1 - dNorth2));

		dTemp = (dDis2D1 * dDis2D1 + dDis2D * dDis2D - dDis2D2 * dDis2D2) / (2 * dDis2D1 * dDis2D);
		a2 = Math.acos(dTemp);
		dDir = azimuth(dNorth1, dEast1, dNorth2, dEast2);
		//第一种情况
		aa1 = dDir + a2;
		if (aa1 > 2 * Math.PI)
		{
			aa1 -= 2 * Math.PI;
		}
		aa1 += Math.PI;
		if (aa1 > 2 * Math.PI)
		{
			aa1 -= 2 * Math.PI;
		}

		aa2 = dDir + Math.PI;
		if (aa2 > 2 * Math.PI)
		{
			aa2 -= 2 * Math.PI;
		}
		aa2 -= Math.PI - a1 - a2;
		if (aa2 < 0.0)
		{
			aa2 += 2 * PI;
		}
		aa2 += Math.PI;
		if (aa2 > 2 * Math.PI)
		{
			aa2 -= 2 * Math.PI;
		}
		//判断
		if (bSmallAngle)
		{
			if (aa1 >= Math.PI)
			{
				if (aa2 < aa1 && aa2 > (aa1 - Math.PI))
				{
					aa1 = dDir - a2;
					if (aa1 < 0.0)
					{
						aa1 += 2 * Math.PI;
					}
				}
				else
				{
					aa1 = dDir + a2;
					if (aa1 > 2 * Math.PI)
					{
						aa1 -= 2 * Math.PI;
					}
				}
			}
			else
			{
				if (aa2 < aa1 || aa2 > (aa1 + Math.PI))
				{
					aa1 = dDir - a2;
					if (aa1 < 0.0)
					{
						aa1 += 2 * Math.PI;
					}
				}
				else
				{
					aa1 = dDir + a2;
					if (aa1 > 2 * Math.PI)
					{
						aa1 -= 2 * Math.PI;
					}
				}
			}
		}
		else
		{
			if (aa1 <= Math.PI)
			{
				if (aa2 < aa1 || aa2 > (aa1 + Math.PI))
				{
					aa1 = dDir + a2;
					if (aa1 > 2 * Math.PI)
					{
						aa1 -= 2 * Math.PI;
					}
				}
				else
				{
					aa1 = dDir - a2;
					if (aa1 < 0.0)
					{
						aa1 += 2 * Math.PI;
					}
				}
			}
			else
			{
				if (aa2 < aa1 && aa2 > (aa1 - Math.PI))
				{
					aa1 = dDir + a2;
					if (aa1 > 2 * Math.PI)
					{
						aa1 -= 2 * Math.PI;
					}
				}
				else
				{
					aa1 = dDir - a2;
					if (aa1 < 0.0)
					{
						aa1 += 2 * Math.PI;
					}
				}
			}
		}

		coordCoun(dNorth1, dEast1, dDis2D1, aa1);

		a1 = dDis1 * Math.cos(dAngle12);
		if (dAngle12 > PI / 2.0)
		{
			a1 *= (-1.0);
		}
		a2 = dDis2 * Math.cos(dAngle22);
		if (dAngle22 > PI / 2.0)
		{
			a2 *= (-1.0);
		}

		this.mathReturnParam.getCoord().setdH((dHeight1 - a1 + dHeight2 - a2) / 2.0);

		return;
	}

	//注意：原始数据里面的每一个测站起始观测的水平角要是0度0分0秒
	public final boolean calculateData(double dAngleM1, double dDistanceM1, double dAngleM2, double dDistanceM2)
	{
		int index, count, count2, row, col, index2;
		;
		double a, a2, d, d2, z10, sumA1, sumB1, sumA2, sumB2, sumL1, sumL2, z20, m0, dStation1M, dStation2M; //单位权中误差
		MeasureData point1, point2;
		double[] pdP;
		double[] pdB;
		double[] pdBt;
		double[] pdNbb;
		double[] pdTemp;
		double[] pdW;
		double[] pdParaL;
		double[] pdParaV;

		final double p = 3600.0 * 180.0 / Math.PI;

		if (datas1.length != datas2.length || datas1.length < 3)
		{
			return false;
		}
		if (datas1[0].type != 1 || datas1[1].type != 1 || datas2[0].type != 1 || datas2[1].type != 1)
		{
			return false;
		}
		//求两个测站概算坐标
		point1 = datas1[0].clone();
		point2 = datas1[1].clone();
		getHouJiaoCoord(point1.angleH, point1.angleV, point1.dis, point2.angleH, point2.angleV, point2.dis, point1.east, point1.north, point1.height, point2.east, point2.north, point2.height);
		station1.setEast(this.mathReturnParam.getCoord().getdE());
		station1.setNorth(this.mathReturnParam.getCoord().getdN());
		station1.setHeight(this.mathReturnParam.getCoord().getdH());
		point1 = datas2[0].clone();
		point2 = datas2[1].clone();
		getHouJiaoCoord(point1.angleH, point1.angleV, point1.dis, point2.angleH, point2.angleV, point2.dis, point1.east, point1.north, point1.height, point2.east, point2.north, point2.height);
		station2.setEast(this.mathReturnParam.getCoord().getdE());
		station2.setNorth(this.mathReturnParam.getCoord().getdN());
		station2.setHeight(this.mathReturnParam.getCoord().getdH());
		//坐标概算
		a = azimuth(station1.getNorth(), station1.getEast(), datas1[0].north, datas1[0].east);
		for (index = 2; index < datas1.length; index++)
		{
			point1 = datas1[index].clone();
			a2 = a + point1.angleH;
			if (a2 > (2.0 * Math.PI))
			{
				a2 -= 2.0 * Math.PI;
			}
			d = point1.dis * Math.sin(point1.angleV);
			coordCoun(station1.getNorth(), station1.getEast(), d, a2);
			datas1[index].east=this.mathReturnParam.getCoord().getdE();
			datas1[index].north=this.mathReturnParam.getCoord().getdN();
			d = point1.dis * Math.sin(Math.PI / 2.0 - point1.angleV);
			datas1[index].height = station1.getHeight() + d;
			for (index2 = 2; index2 < datas2.length; index2++)
			{
				if (datas2[index2].name.equals(point1.name))
				{
					datas2[index2].north = datas1[index].north;
					datas2[index2].east = datas1[index].east;
					datas2[index2].height = datas1[index].height;
					break;
				}
			}
		}
		//求设站点到各观测点的概算方位角及概算距离
		for (index = 0; index < datas1.length; index++)
		{
			datas1[index].azimuth = azimuth(station1.getNorth(), station1.getEast(), datas1[index].north, datas1[index].east);
			datas1[index].s = distance2D(station1.getNorth(), station1.getEast(), datas1[index].north, datas1[index].east);
			datas2[index].azimuth = azimuth(station2.getNorth(), station2.getEast(), datas2[index].north, datas2[index].east);
			datas2[index].s = distance2D(station2.getNorth(), station2.getEast(), datas2[index].north, datas2[index].east);
		}
		//求Z0值
		for (index = 0; index < datas1.length; index++)
		{
			datas1[index].angleH += datas1[0].azimuth + 0.000005;
			if (datas1[index].angleH >= 2.0 * Math.PI)
			{
				datas1[index].angleH -= 2.0 * Math.PI;
			}
			datas2[index].angleH += datas2[0].azimuth + 0.000005;
			if (datas2[index].angleH >= 2.0 * Math.PI)
			{
				datas2[index].angleH -= 2.0 * Math.PI;
			}
		}
		z10 = 0.0;
		for (index = 0; index < datas1.length; index++)
		{
			a = datas1[index].azimuth - datas1[index].angleH;
			z10 += a;
		}
		z10 /= datas1.length;
		z20 = 0.0;
		for (index = 0; index < datas1.length; index++)
		{
			a = datas2[index].azimuth - datas2[index].angleH;
			z20 += a;
		}
		z20 /= datas1.length;
		//根据点名排序
		for (index = 2; index < datas1.length - 1; index++)
		{
			for (index2 = 2; index2 < datas2.length; index2++)
			{
				if ((datas1[index].name.equals(datas2[index2].name)))
				{
					if (index == index2)
					{
						break;
					}
					else
					{
						point1 = datas2[index2].clone();
						datas2[index2] = datas2[index].clone();
						datas2[index] = point1.clone();
						break;
					}
				}
			}
			if (index2 == datas2.length)
			{
				return false;
			}
		}
		sumA1 = sumB1 = sumA2 = sumB2 = sumL1 = sumL2 = 0.0;
		pdParaL = new double[4 * datas1.length + 2];
		//求各系数
		for (index = 0; index < datas1.length; index++)
		{
			point1 = datas1[index].clone();
			a = point1.angleH - (datas1[index].azimuth - z10);
			if (a > Math.PI)
			{
				a -= 2.0 * PI;
			}
			a *= p;
			datas1[index].l = a;
			datas1[index].a = (point1.east - station1.getEast()) * p / (datas1[index].s * datas1[index].s * 1000.0);
			sumA1 += datas1[index].a;
			datas1[index].b = (point1.north - station1.getNorth()) * p / (datas1[index].s * datas1[index].s * 1000.0);
			sumB1 += datas1[index].b;
			datas1[index].la = -(point1.north - station1.getNorth()) / datas1[index].s;
			datas1[index].lb = -(point1.east - station1.getEast()) / datas1[index].s;
			d = point1.dis * Math.sin(point1.angleV);
			d2 = distance2D(station1.getEast(), station1.getNorth(), point1.east, point1.north);
			datas1[index].ll = (d - d2) * 1000.0;
			pdParaL[index] = datas1[index].l;
			pdParaL[index + datas1.length + 1] = datas1[index].ll;
			sumL1 += datas1[index].l;
		}
		pdParaL[datas1.length] = sumL1;
		for (index = 0; index < datas2.length; index++)
		{
			point1 = datas2[index].clone();
			a = point1.angleH - (datas2[index].azimuth - z20);
			if (a > Math.PI)
			{
				a -= 2.0 * PI;
			}
			a *= p;
			datas2[index].l = a;
			datas2[index].a = (point1.east - station2.getEast()) * p / (datas2[index].s * datas2[index].s * 1000.0);
			sumA2 += datas2[index].a;
			datas2[index].b = (point1.north - station2.getNorth()) * p / (datas2[index].s * datas2[index].s * 1000.0);
			sumB2 += datas2[index].b;
			datas2[index].la = -(point1.north - station2.getNorth()) / datas2[index].s;
			datas2[index].lb = -(point1.east - station2.getEast()) / datas2[index].s;
			d = point1.dis * Math.sin(point1.angleV);
			d2 = distance2D(station2.getEast(), station2.getNorth(), point1.east, point1.north);
			datas2[index].ll = (d - d2) * 1000.0;
			pdParaL[index + datas1.length * 2 + 1] = datas2[index].l;
			pdParaL[index + datas1.length * 3 + 2] = datas2[index].ll;
			sumL2 += datas2[index].l;
		}
		pdParaL[datas1.length * 3 + 1] = sumL2;
		//求权阵矩阵
		m0 = dAngleM1;
		pdP = new double[(4 * datas1.length + 2) * (4 * datas1.length + 2)]; //[676]
		count = (4 * datas1.length + 2) * (4 * datas1.length + 2);
		for (index = 0; index < count; index++)
		{
			pdP[index] = 0.0;
		}
		count = (4 * datas1.length + 2); //26
		for (index = 0; index < count; index++)
		{
			if (index < datas1.length) //6
			{
				pdP[index * (4 * datas1.length + 2) + index] = m0 * m0 / (dAngleM1 * dAngleM1); //0,27,54,81,108,135
			}
			else if (index == datas1.length)
			{
				pdP[index * (4 * datas1.length + 2) + index] = m0 * m0 / (dAngleM1 * dAngleM1); //162
			}
			else if (index > datas1.length && index < (2 * datas1.length + 1))
			{
				pdP[index * (4 * datas1.length + 2) + index] = m0 * m0 / (dDistanceM1 * dDistanceM1); //189======324
			}
			else if (index >= (2 * datas1.length + 1) && index < (3 * datas1.length + 1))
			{
				pdP[index * (4 * datas1.length + 2) + index] = m0 * m0 / (dAngleM2 * dAngleM2);
			}
			else if (index == (3 * datas1.length + 1))
			{
				pdP[index * (4 * datas1.length + 2) + index] = m0 * m0 / (dAngleM2 * dAngleM2);
			}
			else
			{
				pdP[index * (4 * datas1.length + 2) + index] = m0 * m0 / (dDistanceM2 * dDistanceM2);
			}
		}
		//string strr=string.Empty;
		//for (index = 0; index < count; index++)
		//{
		//    for(index2=0;index2<count;index2++)
		//    {
		//        strr += pdP[index * count + index2].ToString();
		//        strr += ",";
		//    }
		//    strr += "\n";
		//}
		//求B矩阵
		count = (datas1.length * 2 + 1) * 2 * (4 + (datas1.length - 2) * 2);
		pdB = new double[count];
		for (index = 0; index < count; index++)
		{
			row = index / (4 + (datas1.length - 2) * 2);
			col = index - row * (4 + (datas1.length - 2) * 2);
			if (row < (2 * datas1.length + 1))
			{
				if (row < datas1.length)
				{
					if (col == 0)
					{
						pdB[index] = datas1[row].a;
					}
					else if (col == 1)
					{
						pdB[index] = datas1[row].b;
					}
					else if ((col - 4) == (row - 2) * 2 && col != 2 && col != 3)
					{
						pdB[index] = datas1[row].a * (-1.0);
					}
					else if ((col - 4) == ((row - 2) * 2 + 1) && col != 2 && col != 3)
					{
						pdB[index] = datas1[row].b * (-1.0);
					}
				}
				else if (row == datas1.length)
				{
					if (col == 0)
					{
						pdB[index] = sumA1;
					}
					else if (col == 1)
					{
						pdB[index] = sumB1;
					}
					else if (col > 3 && col < (4 + datas1.length * 2) && col != 2 && col != 3)
					{
						if ((col - 4) % 2 == 0)
						{
							pdB[index] = datas1[(col - 4) % 2 + 2].a * (-1.0);
						}
						else
						{
							pdB[index] = datas1[(col - 4) % 2 + 2].b * (-1.0);
						}
					}
				}
				else
				{
					if (col == 0)
					{
						pdB[index] = datas1[row - datas1.length - 1].la * (-1.0);
					}
					else if (col == 1)
					{
						pdB[index] = datas1[row - datas1.length - 1].lb * (-1.0);
					}
					else if ((col - 4) == (row - datas1.length - 1 - 2) * 2 && col != 2 & col != 3)
					{
						pdB[index] = datas1[row - datas1.length - 1].la;
					}
					else if ((col - 4) == ((row - datas1.length - 1 - 2) * 2 + 1) && col != 2 & col != 3)
					{
						pdB[index] = datas1[row - datas1.length - 1].lb;
					}
				}
			}
			else
			{
				if (row < datas1.length * 3 + 1)
				{
					if (col == 2)
					{
						pdB[index] = datas2[row - (datas1.length * 2 + 1)].a;
					}
					else if (col == 3)
					{
						pdB[index] = datas2[row - (datas1.length * 2 + 1)].b;
					}
					else if ((col - 4) == (row - (datas1.length * 2 + 1) - 2) * 2 && col != 0 && col != 1)
					{
						pdB[index] = datas2[row - (datas1.length * 2 + 1)].a * (-1.0);
					}
					else if ((col - 4) == ((row - (datas1.length * 2 + 1) - 2) * 2 + 1) && col != 0 && col != 1)
					{
						pdB[index] = datas2[row - (datas1.length * 2 + 1)].b * (-1.0);
					}
				}
				else if (row == datas1.length * 3 + 1)
				{
					if (col == 2)
					{
						pdB[index] = sumA2;
					}
					else if (col == 3)
					{
						pdB[index] = sumB2;
					}
					else if (col > 3 && col < (4 + datas1.length * 2))
					{
						if ((col - 4) % 2 == 0)
						{
							pdB[index] = datas2[(col - 4) % 2 + 2].a * (-1.0);
						}
						else
						{
							pdB[index] = datas2[(col - 4) % 2 + 2].b * (-1.0);
						}
					}
				}
				else
				{
					if (col == 2)
					{
						pdB[index] = datas2[row - (datas1.length * 2 + 1) - datas1.length - 1].la * (-1.0);
					}
					else if (col == 3)
					{
						pdB[index] = datas2[row - (datas1.length * 2 + 1) - datas1.length - 1].lb * (-1.0);
					}
					else if ((col - 4) == ((row - (datas1.length * 2 + 1) - datas1.length - 1 - 2) * 2) && col != 0 && col != 1)
					{
						pdB[index] = datas2[row - (datas1.length * 2 + 1) - datas1.length - 1].la;
					}
					else if ((col - 4) == ((row - (datas1.length * 2 + 1) - datas1.length - 1 - 2) * 2 + 1) && col != 0 && col != 1)
					{
						pdB[index] = datas2[row - (datas1.length * 2 + 1) - datas1.length - 1].lb;
					}
				}
			}
		}
		//
		//求Bt矩阵
		pdBt = new double[count];
		for (index = 0; index < count; index++)
		{
			row = index / (4 + (datas1.length - 2) * 2);
			col = index - row * (4 + (datas1.length - 2) * 2);
			count2 = col * (datas1.length * 2 + 1) * 2 + row;
			pdBt[count2] = pdB[index];
		}
		//求Nbb
		pdTemp = new double[(4 + (datas1.length - 2) * 2) * (4 * datas1.length + 2)];
		row = 4 + (datas1.length - 2) * 2;
		col = 4 * datas1.length + 2;
		trmul(pdBt, pdP, row, col, col, pdTemp);
		pdNbb = new double[(4 + (datas1.length - 2) * 2) * (4 + (datas1.length - 2) * 2)];
		trmul(pdTemp, pdB, row, col, row, pdNbb);
		if (!rinv(pdNbb, (4 + (datas1.length - 2) * 2)))
		{
			return false;
		}
		//求W
		pdW = new double[(4 + (datas1.length - 2) * 2)];
		trmul(pdTemp, pdParaL, (4 + (datas1.length - 2) * 2), (4 * datas1.length + 2), 1, pdW);
		//求坐标改正数
		pdTemp = new double[(4 + (datas1.length - 2) * 2)];
		trmul(pdNbb, pdW, (4 + (datas1.length - 2) * 2), (4 + (datas1.length - 2) * 2), 1, pdTemp);
		//求坐标平差值
		station1.setNorth(station1.getNorth() + pdTemp[0] / 1000.0);
		station1.setEast(station1.getEast() + pdTemp[1] / 1000.0);
		station2.setNorth(station2.getNorth() + pdTemp[2] / 1000.0);
		station2.setEast(station2.getEast() + pdTemp[3] / 1000.0);
		for (index = 2; index < datas1.length; index++)
		{
			datas1[index].north += pdTemp[index + 2] / 1000.0;
			index++;
			datas1[index].east += pdTemp[index + 2] / 1000.0;
		}
		//求V值
		count = 4 * datas1.length + 2;
		pdParaV = new double[count];
		for (index = 0; index < count / 2; index++)
		{
			if (index < 2)
			{
				pdParaV[index] = datas1[index].a * pdTemp[0] + datas1[index].b * pdTemp[1];
			}
			else if (index > 1 && index < datas1.length)
			{
				pdParaV[index] = datas1[index].a * pdTemp[0] + datas1[index].b * pdTemp[1] - datas1[index].a * pdTemp[index * 2] - datas1[index].b * pdTemp[index * 2 + 1];
			}
			else if (index == datas1.length)
			{
				pdParaV[index] = sumA1 * pdTemp[0] + sumB1 * pdTemp[1];
				for (index2 = 2; index2 < datas1.length; index2++)
				{
					pdParaV[index] -= (datas1[index2].a * pdTemp[index2 * 2] + datas1[index2].b * pdTemp[index2 * 2 + 1]);
				}

			}
			else if (index == datas1.length + 1)
			{
				pdParaV[index] = datas1[0].la * pdTemp[0] + datas1[0].lb * pdTemp[1];
			}
			else if (index == datas1.length + 2)
			{
				pdParaV[index] = datas1[1].la * pdTemp[0] + datas1[1].lb * pdTemp[1];
			}
			else if (index > datas1.length + 2 && index < (datas1.length * 2 + 1))
			{
				count2 = (index - datas1.length - 1);
				pdParaV[index] = datas1[count2].la * pdTemp[count2 * 2] + datas1[count2].lb * pdTemp[count2 * 2 + 1];
			}
		}
		index2 = count / 2;
		for (index = count / 2; index < count; index++)
		{
			index2 = index - count / 2;
			if (index2 < 2)
			{
				pdParaV[index] = datas2[index2].a * pdTemp[2] + datas2[index2].b * pdTemp[3];
			}
			else if (index2 > 1 && index2 < datas2.length)
			{
				pdParaV[index] = datas2[index2].a * pdTemp[2] + datas2[index2].b * pdTemp[3] - datas2[index2].a * pdTemp[index2 * 2] - datas2[index2].b * pdTemp[index2 * 2 + 1];
			}
			else if (index2 == datas2.length)
			{
				int index3;
				pdParaV[index] = sumA2 * pdTemp[2] + sumB2 * pdTemp[3];
				for (index3 = 2; index3 < datas2.length; index3++)
				{
					pdParaV[index] -= (datas2[index3].a * pdTemp[index3 * 2] + datas1[index3].b * pdTemp[index3 * 2 + 1]);
				}
			}
			else if (index2 == datas2.length + 1)
			{
				pdParaV[index] = datas2[0].la * pdTemp[2] + datas2[0].lb * pdTemp[3];
			}
			else if (index2 == datas2.length + 2)
			{
				pdParaV[index] = datas2[1].la * pdTemp[2] + datas2[1].lb * pdTemp[3];
			}
			else if (index2 > datas2.length + 2 && index2 < (datas2.length * 2 + 1))
			{
				count2 = (index2 - datas2.length - 1);
				pdParaV[index] = datas2[count2].la * pdTemp[count2 * 2] + datas1[count2].lb * pdTemp[count2 * 2 + 1];
			}
		}
		//计算单位权中误差
		count = 4 * datas1.length + 2;
		pdTemp = new double[count];
		trmul(pdParaV, pdP, 1, count, count, pdTemp);
		pdW = new double[1];
		trmul(pdTemp, pdParaV, 1, count, 1, pdW);
		m0 = Math.sqrt(pdW[0] / (4 * datas1.length + 2 - datas1.length * 2));
		//计算中误差
		a = m0 * Math.sqrt(pdNbb[0]);
		a2 = m0 * Math.sqrt(pdNbb[4 + (datas1.length - 2) * 2 + 1]);
		dStation1M = Math.sqrt(a * a + a2 * a2);
		a = m0 * Math.sqrt(pdNbb[(4 + (datas1.length - 2) * 2) * 2]);
		a2 = m0 * Math.sqrt(pdNbb[(4 + (datas1.length - 2) * 2) * 2 + 1]);
		dStation2M = Math.sqrt(a * a + a2 * a2);
//		station1.m = dStation1M;
//		station2.m = dStation2M;
		return true;
	}
}
