package com.southgt.smosplat.data.util.math;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.util.Arith;
import com.southgt.smosplat.data.entity.WYS_CoordData;

import Jama.Matrix;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName gtMath 
 * @Description 核心数学运算类
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
public class GtMath
	{
		/** 
		 圆周率
		*/
		public static final double PI = 3.1415926535897932384626433832795;
		
		/** 
		 返回错误值
		*/
		public static final double INVALID_DATA = -99999.0;
		
		/** 
		 double值错误
		*/
		public static final double INVALID_DOUBLE = 1e-6;
		
		/** 
		 最小正值
		*/
		private double dEpsilon = 1.0e-6;
		
		private Math_ReturnParam mathReturnParam;

		public Math_ReturnParam getMathReturnParam() {
			return mathReturnParam;
		}

		public void setMathReturnParam(Math_ReturnParam mathReturnParam) {
			this.mathReturnParam = mathReturnParam;
		}

		@Override
		public String toString() {
			return "GtMath [dEpsilon=" + dEpsilon + ", mathReturnParam="
					+ mathReturnParam + "]";
		}

		/** 
		* <p>Title: </p> 
		* <p>Description: </p>  
		*/
		public GtMath() {
			super();
			this.mathReturnParam=new Math_ReturnParam();
		}

		/**
		 * 
		 * 断面纵向位移
		 * @date  2016年12月30日 上午10:22:38
		 * @param @param mdInfoAdd_dN 实测北坐标
		 * @param @param mdInfoAdd_dE 实测东坐标
		 * @param @param sectInfo_dAzimuth 断面方位角
		 * @param @param spInfod_North 监测点北坐标
		 * @param @param spInfod_East 监测点东坐标
		 * @param @return
		 * @return double
		 * @throws null
		 * 
		 * @version v1.0
		 * @author  Administrator
		 * <p>Modification History:</p>
		 * <p>Date         Author  姚家俊    Version     Description</p>
		 * <p> -----------------------------------------------------------------</p>
		 * <p>2016年12月30日     Administrator      v1.0          create</p>
		 *
		 */
		public static double x_Displacement(double mdInfoAdd_dN, double mdInfoAdd_dE, double sectInfo_dAzimuth, double spInfod_North, double spInfod_East){
			double spInfooriginN = spInfod_North * Math.cos(sectInfo_dAzimuth) + spInfod_East * Math.sin(sectInfo_dAzimuth);
            return mdInfoAdd_dN * Math.cos(sectInfo_dAzimuth) + mdInfoAdd_dE * Math.sin(sectInfo_dAzimuth) - spInfooriginN;
		}
		/**
		 * 
		 * 断面横向位移
		 * @date  2016年12月30日 上午10:27:14
		 * 
		 * @param @param mdInfoAdd_dN 实测北坐标
		 * @param @param mdInfoAdd_dE 实测东坐标
		 * @param @param sectInfo_dAzimuth 断面方位角
		 * @param @param spInfod_North 监测点北坐标
		 * @param @param spInfod_East 监测点东坐标
		 * @param @return
		 * @return double
		 * @throws null
		 * 
		 * @version v1.0
		 * @author  Administrator
		 * <p>Modification History:</p>
		 * <p>Date         Author  姚家俊    Version     Description</p>
		 * <p> -----------------------------------------------------------------</p>
		 * <p>2016年12月30日     Administrator      v1.0          create</p>
		 *
		 */
		public static double y_Displacement(double mdInfoAdd_dN, double mdInfoAdd_dE, double sectInfo_dAzimuth, double spInfod_North, double spInfod_East)
        {
            double spInfooriginE = -1 * spInfod_North * Math.sin(sectInfo_dAzimuth) + spInfod_East * Math.cos(sectInfo_dAzimuth);
            return -1 * mdInfoAdd_dN * Math.sin(sectInfo_dAzimuth) + mdInfoAdd_dE * Math.cos(sectInfo_dAzimuth) - spInfooriginE;
        }
		/**
		 * 
		 * 计算方位角(返回弧度)
		 * @date  2017年7月3日 上午11:35:40
		 * 
		 * @param startPoint
		 * @param endPoint
		 * @return
		 * Double
		 * @throws null
		 * 
		 * @version v1.0
		 * @author  姚家俊
		 * <p>Modification History:</p>
		 * <p>Date         Author      Version     Description</p>
		 * <p> -----------------------------------------------------------------</p>
		 * <p>2017年7月3日     姚家俊      v1.0          create</p>
		 *
		 */
	public static Double calculateSectionAngle(WYS_CoordData startPoint, WYS_CoordData endPoint) {
		Double startE = startPoint.getCaculateE();
		Double startN = startPoint.getCaculateN();
		Double endE = endPoint.getCaculateE();
		Double endN = endPoint.getCaculateN();
		Double angleResult = 0.0;
		if (startE == null || startN == null || endE == null || endN == null) {
			System.out.println("坐标不能存在空值！");
			return null;
		}
		if (startE == endE && startN == endN) {
			System.out.println("起始点和终止点坐标不能相同！");
			return null;
		}
		if ((endE * 1 - startE * 1) != 0 && (endN * 1 - startN * 1) != 0) { // 非边界方位角
			Double resultAbs = Math.abs((endE * 1 - startE * 1) / (endN * 1 - startN * 1));
			angleResult = Math.atan(resultAbs) * 180 / Math.PI;
			if ((endN * 1 - startN * 1) > 0) {
				if ((endE * 1 - startE * 1) > 0) {
					//不变
				} else if ((endE * 1 - startE * 1) < 0) {
					angleResult = 360 - angleResult * 1;
				}
			} else if ((endN * 1 - startN * 1) < 0) {
				if ((endE * 1 - startE * 1) > 0) {
					angleResult = 180 - angleResult * 1;
				} else if ((endE * 1 - startE * 1) < 0) {
					angleResult = 180 + angleResult * 1;
				}
			}
		} else { // 方位角在边界上，东坐标方向
			if ((endN * 1 - startN * 1) == 0 && (endE * 1 - startE * 1) > 0) {
				angleResult = 0.0; // 东坐标正方向
			} else if ((endN * 1 - startN * 1) == 0 && (endE * 1 - startE * 1) < 0) {
				angleResult = 180.0; // 东坐标反方向
			} else if ((endN * 1 - startN * 1) < 0 && (endE * 1 - startE * 1) == 0) {
				angleResult = 90.0; // 北坐标正方向
			} else if ((endN * 1 - startN * 1) > 0 && (endE * 1 - startE * 1) == 0) {
				angleResult = 270.0; // 北坐标反方向
			}
			;
		}
		angleResult = exAngle(angleResult, (short) 3);
		return angleResult;
	}
		
		/** 
		 计算湿温
		 @param Humidity 相对湿度%
		 @param DryTemperature 干球温度℃
		*/
		public static double adjWetTemperature(double humidity, double dryTemperature)
		{
			double tw = 15; //湿温假设值（全站仪湿温范围5.2-20）
			boolean adjSucceed = false;
			int adjTime = 0;
			while (!adjSucceed)
			{
				adjTime++;
				double a = (18.678 - dryTemperature / 234.5) * tw / (tw + 257.14); //系数
				double pw = 611.2 * Math.pow(Math.E, a); //水蒸汽饱和压力
				double dw = 0.6219 * pw / (101326 - pw); //含湿量, kg/kg干空气
				double hw = 1.01 * tw + (2500 + 1.84 * tw) * dw; //湿空气焓值
				double b = (18.678 - dryTemperature / 234.5) * dryTemperature / (dryTemperature + 257.14);
				double p = 611.2 * Math.exp(b);
				double d = 0.6219 * 0.01 * humidity * p / (101326 - 0.01 * humidity * p);
				double h = 1.01 * dryTemperature + (2500 + 1.84 * dryTemperature) * d;

				if (Math.abs(hw - h) < 0.05)
				{
					break;
				}
				else if (adjTime > 5000)
				{
					break;
				}
				if (hw < h)
				{
					tw += 0.1;
				}
				else
				{
					tw -= 0.1;
				}
			}
			return tw;
		}
		
		/** 
		 十进制度为单位的角度转换成秒
		 @param dbDms 十进制的角度
		 @return 转换成秒后的角度
		*/
		public final double unTrunAngle(double dbDms)
		{
			double dD;
			dD = dbDms * 3600.0;

			while (dD >= 1296000.0)
			{
				dD -= 1296000.0;
			}
			if (dD < 0.0)
			{
				dD = 0.0;
			}

			return dD;
		}
		
		/** 
		 角度转换函数
		 @param angle 需要转换的角度
		 @param nFlag
		 0－度分秒转换为十进制度	
		 1－十进制度转换为度分秒
		 2－度分秒转换为弧度		
		 3－十进制度转换为弧度
		 @return 
		*/
	public static double exAngle(double angle, short nFlag) {
		double mant, temp, result;
		result = INVALID_DOUBLE;
		long d, f, m;
		temp = angle;
		if (angle >= 0.0) {
			mant = 0.000000001;
		} else {
			mant = -0.000000001;
		}
		if (nFlag == 0 || nFlag == 2) {
			d = (long) (temp + mant);// 度的整数部分
			temp = (temp - d) * 100.0;
			f = (long) (temp + mant);// 分的整数部分

			if (Math.abs(f) >= 60) {
				return INVALID_DOUBLE;
			}

			temp = (temp - f) * 100.0;
			m = (long) (temp + mant);// 秒的整数部分

			if (Math.abs(m) >= 60) {
				return INVALID_DOUBLE;
			}
			// //弧度合理化。转化成2PI以内的弧度。
			// if(temp>2*Math.PI){
			// temp%=2*Math.PI;
			// }

			temp = (temp - m) * 100.0;// 秒的小数部分
			result = d + f / 60.0 + m / 3600.0 + temp / 360000.0;
			if (nFlag == 2) {
				result = result * PI / 180.0;
			}
			return result;
		} else if (nFlag == 1) {
			d = (long) (temp + mant);
			temp = (temp - d) * 60.0;
			f = (long) (temp + mant);
			temp = (temp - f) * 60.0;
			result = d + (f * 1000000 + temp * 10000) / 100000000.0;

			if (Math.abs(result - 360.0) < 1.0e-10) {
				result = 0.0;
			}
		} else if (nFlag == 3) {
			result = angle * PI / 180.0;
		}
		return result;
	}
		
		/** 
		 弧度转换十进制度/度分秒
		 @param radian 需要转换的角度
		 @param nFlag
		0-弧度转换为十进制度，
		1-弧度转换为度分秒       
		 返回值  : 十进制度或度分秒，
		返回INVALID_DOUBLE错误。	
		 @return 
		*/
		public static double radianToDegree(double radian, int iMode)
		{
			double dDegree = INVALID_DOUBLE;
			if (iMode == 0)
			{
				dDegree = radian * 180.0 / PI;
			}
			else if (iMode == 1)
			{
				dDegree = exAngle(radian * 180.0 / PI, (short) 1);
			}
			return dDegree;
		}
		
		/**
		 * 
		 * TODO(弧度转化为秒) 
		 * @date  2016年8月15日 下午4:02:50
		 * 
		 * @param @param radian
		 * @param @return
		 * @return double
		 * @throws null
		 * 
		 * @version v1.0
		 * @author  Administrator
		 * <p>Modification History:</p>
		 * <p>Date         Author  姚家俊    Version     Description</p>
		 * <p> -----------------------------------------------------------------</p>
		 * <p>2016年8月15日     Administrator      v1.0          create</p>
		 *
		 */
		public static double radianToSecond(double radian)
		{
			double strAngle = 0.0;
			return strAngle = radian * 3600 / Math.PI * 180;
		}
		
		/** 
		 度分秒转换弧度
		 @param Angle 度分秒
		 @param iMode 转换方式   0- 度 转换弧度    1-度分秒转换弧度 
		 @return 弧度
		*/
		public final static double angleToRadian(double angle, int iMode)
		{
			double dRadian = INVALID_DOUBLE;
			if (iMode == 0)
			{
				dRadian = angle * Math.PI / 180;
			}
			else if (iMode == 1)
			{
				dRadian = exAngle(angle, (short) 0) * Math.PI / 180;
			}
			return dRadian;
		}
		
		/**
		 * 
		 * 度分秒转弧度(xx°xx′xx″)
		 * @date  2017年6月20日 上午9:48:17
		 * 
		 * @param angle
		 * @return
		 * double
		 * @throws null
		 * 
		 * @version v1.0
		 * @author  姚家俊
		 * <p>Modification History:</p>
		 * <p>Date         Author      Version     Description</p>
		 * <p> -----------------------------------------------------------------</p>
		 * <p>2017年6月20日     姚家俊      v1.0          create</p>
		 *
		 */
		public static double angleToRadian(String angle){
			int degIndex = angle.indexOf("°");
			int minIndex = angle.indexOf("'");
			int secIndex = angle.indexOf("\"");
			double deg = Double.parseDouble(angle.substring(0, degIndex));
			double min = Double.parseDouble(angle.substring(degIndex + 1, minIndex));
			double sec = Double.parseDouble(angle.substring(minIndex + 1, secIndex));
			double a = deg + min/100 + sec/10000;
			double a2Radian = GtMath.angleToRadian(a,1);
			return a2Radian;
		}
		
		/** 
		 弧度转换为度分秒(xx°xx′xx″)
		 @param radian 弧度
		 @return 角度(xx°xx′xx″)
		*/
		public static String radiantoAngle(double radian)
		{
			double absRadian = 0.0;
			if (radian < 0)
			{
				absRadian = Math.abs(radian);
			}
			else
			{
				absRadian = radian;
			}
			double dD = radianToDegree(absRadian, 1);
			double degree = (int)(dD);
			double second = (int)((dD - degree) * 100);
			double minute = (dD - degree - second / 100) * 10000;
			String strAngle = "";
			if (radian < 0)
			{
				strAngle = strAngle = String.format("-%1$s°%2$s′%3$s″", degree, pad(Double.toString(second),2,true), pad(Double.toString(minute).substring(0,Double.toString(minute).indexOf(".")+1 ),4,true));
			}
			else
			{
				strAngle = strAngle = String.format("%1$s°%2$s′%3$s″", degree, pad(Double.toString(second),2,true),  pad(Double.toString(minute).substring(0,Double.toString(minute).indexOf(".")+1 ),4,true));
			}
			return strAngle;
		}
		
		/**
		 * str:源字符串
		 * size:补齐0的个数
		 * isprefixed（
		 * true：0加在前面
		 * false:0加在后面）
		 */
	public static String pad(String str, int size, boolean isprefixed) {
		if (str == null)
			str = "";
		int str_size = str.length();
		int pad_len = size - str_size;
		StringBuffer retvalue = new StringBuffer();
		for (int i = 0; i < pad_len; i++) {
			retvalue.append("0");
		}
		if (isprefixed)
			return retvalue.append(str).toString();
		return retvalue.insert(0, str).toString(); } 
		
		/** 
		 弧度转换为度分秒,(xx°xx′xx″)后，只返回分
		 
		 @param radian 弧度
		 @return 秒形式的角度(xx″)
		*/
		public static String radiantoMinute(double radian)
		{
			double absRadian = 0.0;
			if (radian < 0)
			{
				absRadian = Math.abs(radian);
			}
			else
			{
				absRadian = radian;
			}
			double dD = radianToDegree(absRadian, 1);
			double degree = (int)(dD);
			double second = (int)((dD - degree) * 100);
			double minute = (dD - degree - second / 100) * 10000;
			String strAngle = "";
			if (radian < 0)
			{
				strAngle = strAngle = String.format("-%3$s'", degree, pad(Double.toString(second),2,true), Double.toString(minute).subSequence(0, Double.toString(minute).indexOf(".")+1));
			}
			else
			{
				strAngle = strAngle = String.format("%3$s'", degree, pad(Double.toString(second),2,true), Double.toString(minute).subSequence(0, Double.toString(minute).indexOf(".")+1));
			}
			return strAngle;
		}
		
		/** 
		  秒为单位的角度转换为度分秒
		 @param lAngle 秒为单位的角度
		 @return 度分秒形式的角度
		*/
		public static double turnAngle(long lAngle)
		{
			double dD;
			while (lAngle >= 1296000)
			{
				lAngle -= 1296000;
			}
			if (lAngle < 0)
			{
				lAngle = 0;
			}
			dD = lAngle / 3600.0;
			return exAngle(dD, (short) 1);
		}
		
		/** 
		 根据2点坐标计算方位角
		 @param startNorth 起始点北坐标
		 @param startEast 起始点东坐标
		 @param endNorth 终点北坐标
		 @param endEast 终点东坐标
		 @return 
		*/
		public static double getAngle(double startNorth, double startEast, double endNorth, double endEast)
		{
			double result, m_angle;
			double desN = endNorth - startNorth;
			double desE = endEast - startEast;
			if (Math.abs(desN) > 0)
			{
				m_angle = Math.atan(desE / desN);
			}
			else
			{
				if ((Math.abs(desE) == 0) && (Math.abs(desN) == 0))
				{
					return 0.0;
				}
				else
				{
					m_angle = Math.PI / 2;
				}
			}
			if (desN >= 0)
			{
				if (desN > 0)
				{
					if (desE > 0)
					{
						result = m_angle;
					}
					else
					{
						result = 2 * Math.PI + m_angle;
					}
				}
				else
				{
					if (desE > 0)
					{
						result = m_angle;
					}
					else
					{
						result = Math.PI + m_angle;
					}
				}
			}
			else
			{
				result = Math.PI + m_angle;
			}
			if (result == 2 * Math.PI)
			{
				return 0.0;
			}
			return result; //输出弧度值角度
		}
		
		/** 
		 求解坐标为(x,y)的点的方位角，
		 其中y轴正方向为0.0，沿顺时针方向增加，
		 范围为[0,2*PI).  当x=y=0时，返回-9999999.  
		 @param x
		 @param y
		 @return 
		*/
		public static double getAzimuth(double x, double y)
		{
			if (Math.abs(x) < 1e-15 && Math.abs(y) > 1e-15)
			{
				if (y > 0)
				{
					return 0.0;
				}
				else
				{
					return Math.PI;
				}
			}
			if (Math.abs(x) > 1e-15 && Math.abs(y) < 1e-15)
			{
				if (x > 0)
				{
					return Math.PI * 0.5;
				}
				else
				{
					return 1.5 * Math.PI;
				}
			}
			if (Math.abs(x) > 1e-15 && Math.abs(y) > 1e-15)
			{
				if (x > 0 && y > 0)
				{
					return Math.atan(x / y);
				}
				if (x > 0 && y < 0)
				{
					return Math.atan(x / y) + Math.PI;
				}
				if (x < 0 && y < 0)
				{
					return Math.atan(x / y) + Math.PI;
				}
				if (x < 0 && y > 0)
				{
					return Math.atan(x / y) + 2 * Math.PI;
				}
			}

			return INVALID_DATA;
		}
		
		/** 
		 根据2点坐标计算平面坐标距离
		 @param startNorth 起始点北坐标
		 @param startEast 起始点东坐标
		 @param endNorth 终点北坐标
		 @param endEast 终点东坐标
		 @return 平距
		*/
		public static double getDist2D(double startNorth, double startEast, double endNorth, double endEast)
		{
			return Math.sqrt((startNorth - endNorth) * (startNorth - endNorth) + (startEast - endEast) * (startEast - endEast));
		}
		
		/** 
		 利用点投影到断面的方法，求北位移and东位移、高程位移
		 @param MeaN 实测北坐标
		 @param MeaE 实测东坐标
		 @param MeaE 实测高程
		 @param SecAzimuth 断面方位角（弧度）
		 @param ReferenceN 参考点北坐标
		 @param ReferenceE 参考点东坐标
		 @return 位移
		*/
		public void calDisp(double meaN, double meaE, double meaH, double secAzimuth, double referenceN, double referenceE, double referenceH)
		{
			double e0 = -1 * referenceN * Math.sin(secAzimuth) + referenceE * Math.cos(secAzimuth); //东坐标偏移量
			double n0 = referenceN * Math.cos(secAzimuth) + referenceE * Math.sin(secAzimuth);       //北坐标偏移量
			this.mathReturnParam.getDisp().setDisp_E(-1 * meaN * Math.sin(secAzimuth) + meaE * Math.cos(secAzimuth) - e0);
			this.mathReturnParam.getDisp().setDisp_N(meaN * Math.cos(secAzimuth) + meaE * Math.sin(secAzimuth) - n0);
			this.mathReturnParam.getDisp().setDisp_H(meaH - referenceH);
		}
		
		/** 
		 根据2点坐标计算三维坐标距离
		 @param dStartNorth 起点北坐标
		 @param dStartEast 起点东坐标
		 @param dStartHeight 起点高
		 @param dEndNorth 终点北坐标
		 @param dEndEast 终点东坐标
		 @param dEndHeight 终点高
		 @return 距离
		*/
		public static double getDist3D(double dStartNorth, double dStartEast, double dStartHeight, double dEndNorth, double dEndEast, double dEndHeight)
		{
			return Math.sqrt((dEndNorth - dStartNorth) * (dEndNorth - dStartNorth) + (dEndEast - dStartEast) * (dEndEast - dStartEast) + (dEndHeight - dStartHeight) * (dEndHeight - dStartHeight));
		}
		/** 
		 求一个3X3矩阵的逆矩阵
		 
		 @param src 已知矩阵1
		 @param inverse 逆矩阵
		 @return 成功返回true 失败返回false
		*/
		public  boolean invert(double[][] src)
		{
			double a, b, c, d, e, f, g, h, i;
			a = src[0][0];
			b = src[0][1];
			c = src[0][2];
			d = src[1][0];
			e = src[1][1];
			f = src[1][2];
			g = src[2][0];
			h = src[2][1];
			i = src[2][2];

			double del = -c * e * g + b * f * g + c * d * h - a * f * h - b * d * i + a * e * i;

			if (Math.abs(del) < 1e-15)
			{
				return false;
			}

			double A, B, C, D, E, F, G, H, I;
			A = -f * h + e * i;
			B = c * h - b * i;
			C = -c * e + b * f;
			D = f * g - d * i;
			E = -c * g + a * i;
			F = c * d - a * f;
			G = -e * g + d * h;
			H = b * g - a * h;
			I = -b * d + a * e;

			this.mathReturnParam.setInverse(new double[][]{
					{A / del,B / del,C / del},
					{D / del,E / del,F / del},
					{G / del,H / del,I / del}});
			return true;
		}
		
		/** 
		 两个3X3的矩阵求点乘
		 @param src1 矩阵1
		 @param src2 矩阵2
		 @param des 相乘结果
		 @return 
		*/
		public  boolean matMutiMat(double[][] src1, double[][] src2)
		{
			double[][]des=new double[3][3];
			if (src1.length == 3 && src2.length == 3)
			{
				int i, j;
				for (i = 0; i < 3; i++)
				{
					for (j = 0; j < 3; j++)
					{
						des[i][j] = src1[i][0] * src2[0][j] + src1[i][1] * src2[1][j] + src1[i][2] * src2[2][j];
					}
				}
				this.mathReturnParam.setDes(des);
			}
			else
			{
				return false;
			}
			return true;
		}
		
		/** 
		 根据测站点和目标点数据计算方位角和竖直角
		 @param dTE 测站东
		 @param dTN 测站北
		 @param dTH 测站高程
		 @param dSE 目标东
		 @param dSN 目标北
		 @param dSH 目标高
		 @param dInsH 仪器高
		 @param dPrismH 棱镜高
		 @param AzimuthAngle 方位角
		 @param VerticalAngle 竖直角
		*/
		public void calculateAngle(double dTE, double dTN, double dTH, double dSE, double dSN, double dSH, double dInsH, double dPrismH)
		{
			double dDis2D, dAngle;
			double azi=getAngle(dTN, dTE, dSN, dSE);
			this.mathReturnParam.getAzimuthAndVertialAngle().setAzimuthAngle(azi);
			if (azi > 2 * Math.PI)
			{
				this.mathReturnParam.getAzimuthAndVertialAngle().setAzimuthAngle(azi-2*PI);
			}
			if (azi < 0.0)
			{
				this.mathReturnParam.getAzimuthAndVertialAngle().setAzimuthAngle( 2 * Math.PI + azi);
			}
			dDis2D = getDist2D(dTN, dTE, dSN, dSE);
			dAngle = Math.atan2((dSH + dPrismH) - (dTH + dInsH), dDis2D);
			this.mathReturnParam.getAzimuthAndVertialAngle().setVerticalAngle(Math.PI / 2.0 - dAngle);
		}
		
		/** 
		 根据水平角、天顶距、斜距计算坐标
		 @param dSE 测站东坐标
		 @param dSN 测站北坐标
		 @param dSZ 测站高程
		 @param dSInsH 测站仪器高
		 @param dHA 水平角
		 @param dVA 天顶距
		 @param dSD 斜距
		 @param dPrism 棱镜常数
		 @param dE 计算东坐标E
		 @param dN 计算北坐标N
		 @param dH 计算高程H
		 @return 
		*/
		public   boolean calulateCoord(double dSE, double dSN, double dSZ, double dSInsH, double dHA, double dVA, double dSD, double dPrism)
		{
			double dDis, dAngle, dBegAngle, dAzimuth;
			this.mathReturnParam.getCoord().setdN(dSN);
			this.mathReturnParam.getCoord().setdE(dSE);
			this.mathReturnParam.getCoord().setdH(dSZ);
			if (dHA == -10000.0 || dVA == -10000.0)
			{
				return false;
			}
			if (dSD <= 0.0)
			{
				return false;
			}
			dDis = dSD * Math.sin(dVA);
			dBegAngle = 0.0;
			dAngle = dHA - dBegAngle;
			if (dAngle < 0.0)
			{
				dAngle = dAngle + 2 * Math.PI;
			}
			dAzimuth = 0.0;
			dAngle += dAzimuth;
			if (dAngle >= 2 * Math.PI)
			{
				dAngle -= 2 * Math.PI;
			}
			this.mathReturnParam.getCoord().setdN(dSN + dDis * Math.cos(dAngle));
			this.mathReturnParam.getCoord().setdE(dSE + dDis * Math.sin(dAngle));
			this.mathReturnParam.getCoord().setdH(dSZ + dSD * Math.cos(dVA) + dSInsH - dPrism);
					
			return true;
		}
		
		/**
		 * 
		 * 计算从全站仪导出的数据坐标 
		 * @date  2017年4月26日 上午8:53:25
		 * 
		 * @param dSE
		 * @param dSN
		 * @param dSZ
		 * @param dSInsH
		 * @param dHA
		 * @param dVA
		 * @param dSD
		 * @param dPrism
		 * @return
		 * WYS_CoordData
		 * @throws null
		 * 
		 * @version v1.0
		 * @author  姚家俊
		 * <p>Modification History:</p>
		 * <p>Date         Author      Version     Description</p>
		 * <p> -----------------------------------------------------------------</p>
		 * <p>2017年4月26日     姚家俊      v1.0          create</p>
		 *
		 */
		public static  WYS_CoordData calulateManualCoord(Station station, WYS_CoordData coordData, double dPrism){
			//转为弧度
			double dHA = coordData.getCaculateHA();
			double dVA = coordData.getCaculateVA();
			double dSD = coordData.getCaculateSD();
			double dSN = station.getNorth();
			double dSE = station.getEast();
			double dSZ = station.getHeight();
			double dSInsH = station.getInstrumentHeight();
			dHA = GtMath.exAngle(dHA, (short) 3);
			dVA = GtMath.exAngle(dVA, (short) 3);
			double dDis, dAngle, dBegAngle, dAzimuth;
			if (dHA == -10000.0 || dVA == -10000.0) {
				return null;
			}
			if (dSD <= 0.0) {
				return null;
			}
			// 平距
			dDis = dSD * Math.sin(dVA);
			dBegAngle = 0.0;
			dAngle = dHA - dBegAngle;
			if (dAngle < 0.0) {
				dAngle = dAngle + 2 * Math.PI;
			}
			dAzimuth = 0.0;
			dAngle += dAzimuth;
			if (dAngle >= 2 * Math.PI) {
				dAngle -= 2 * Math.PI;
			}
			coordData.setCaculateN(dSN + dDis * Math.cos(dAngle));
			coordData.setCaculateE(dSE + dDis * Math.sin(dAngle));
			coordData.setCaculateH(dSZ + dSD * Math.cos(dVA) + dSInsH - dPrism);
			coordData.setCaculateHA(dHA);
			coordData.setCaculateVA(dVA);
			return coordData;
		}
		
		/** 
		 弧度合理化（转换成0到2*pi之间）
		 @param ZenithDis
		 @return 
		*/
		public static double radianAjust(double radian)
		{
			double Angle = 0.0;
			if (radian >= 0 && radian < Math.PI * 2)
			{
				Angle = radian;
				return Angle;
			}
			else if (radian < 0)
			{
				boolean Yes = false;
				Angle = radian;
				while (!Yes)
				{
					Angle = Angle + Math.PI * 2;
					if (Angle >= 0 && Angle < Math.PI * 2)
					{
						Yes = true;
					}
				}
				return Angle;
			}
			else
			{
				boolean Yes = false;
				Angle = radian;
				while (!Yes)
				{
					Angle = Angle - Math.PI * 2;
					if (Angle >= 0 && Angle < Math.PI * 2)
					{
						Yes = true;
					}
				}
				return Angle;
			}
		}
		
		/** 
		 归零差计算
		 @param Angle1 第一个角度(弧度)
		 @param Angle2 第二个角度(弧度)
		 @return 返回弧度为单位的归零差
		*/
		public static double diffZerocalCulate(double angle1, double angle2)
		{
			angle1=radianAjust(angle1);
			angle2=radianAjust(angle2);
			double DiffZeroValue = 0.0;
			//注意一个角在第一象限，而另外一个角在第四象限
			if (angle1 <= Math.PI / 2 && angle2 >= Math.PI * 1.5)
			{
				DiffZeroValue = (angle1 + Math.PI * 2 - angle2);
			}
			else if (angle2 <= Math.PI / 2 && angle1 >= Math.PI * 1.5)
			{
				DiffZeroValue = (angle1 - Math.PI * 2 - angle2);
			}
			else
			{
				DiffZeroValue = (angle1 - angle2);
			}
			return DiffZeroValue;
		}
		
		/** 
		 计算两个角度的平均值
		 @param Azimuth1
		 @param Azimuth2
		 @return 
		*/
		public static double calculateAzimuthAvg(double azimuth1, double azimuth2)
		{
			double azimuthAvgVal = 0.0;

			//判断是否一个角在第一象限,而另一个角在第四象限的情况
			if ((azimuth1 < Math.PI / 2.0) && (azimuth2 > Math.PI * 3.0 / 2.0))
			{
				azimuthAvgVal = (azimuth2 + (azimuth1 + 2 * Math.PI)) / 2;
			}
			else if ((azimuth2 < Math.PI / 2.0) && (azimuth1 > Math.PI * 3.0 / 2.0))
			{
				azimuthAvgVal = ((azimuth2 + 2 * Math.PI) + azimuth1) / 2;
			}
			else
			{
				azimuthAvgVal = (azimuth2 + azimuth1) / 2;
			}
			if (azimuthAvgVal < 0)
			{
				return azimuthAvgVal = azimuthAvgVal + 2 * Math.PI;
			}
			else if (azimuthAvgVal > 2 * Math.PI)
			{
				return azimuthAvgVal = azimuthAvgVal - 2 * Math.PI;
			}
			else
			{
				return azimuthAvgVal;
			}
		}
		
		/** 
		 计算弧度列表平均值
		 @param Azimuth
		 @return 
		*/
		public static double calculateAzimuthAvg(List<Double> azimuth)
		{
			double azimuthAvgVal = 0.0;
			if (azimuth.size() == 2)
			{
				azimuthAvgVal = calculateAzimuthAvg(azimuth.get(0), azimuth.get(1));
			}
			else if (azimuth.size() == 1)
			{
				azimuthAvgVal = azimuth.get(0);
			}
			else if (azimuth.isEmpty())
			{
				azimuthAvgVal = 0.0;
			}
			else
			{
				for (int i = 0; i < azimuth.size() - 2; i++)
				{
					if (i == 0)
					{
						azimuthAvgVal = calculateAzimuthAvg(azimuth.get(i), azimuth.get(i + 1));
					}
					else
					{
						azimuthAvgVal = calculateAzimuthAvg(azimuthAvgVal, azimuth.get(i + 1));
					}
				}
			}
			return azimuthAvgVal;
		}
		
		/** 
		 计算盘左和盘右的平均值
		 @param LAzimuth
		 @param RAzimuth
		 @return 
		*/
		public final double calculateLRAzimuthAvg(double lAzimuth, double rAzimuth)
		{
			double AzimuthAvgVal = 0.0;
			if (rAzimuth - Math.PI < 0)
			{
				rAzimuth += Math.PI;
			}
			else
			{
				rAzimuth = rAzimuth - Math.PI;
			}
			return AzimuthAvgVal = calculateAzimuthAvg(lAzimuth, rAzimuth);
		}
		
		/** 
		 1,四参数计算(该算法有问题)
		 计算平面坐标系中，从旧坐标系坐标变换到新坐标系坐标的四参数坐标变换。
		 x = x0 + x'*m*cos(alpha) - y'*m*sin(alpha);
		 y = y0 + x'*m*sin(alpha) + y'*m*cos(alpha);
		 @param nxcoord 新坐标系下的x坐标
		 @param nycoord 新坐标系下的y坐标
		 @param oxcoord 旧坐标系下的x坐标
		 @param oycoord 旧坐标系下的y坐标
		 @param x0 坐标变换的x轴平移量
		 @param y0 坐标变换的y轴平移量
		 @param m 坐标变换的尺度因子
		 @param alpha 坐标变换的旋转角
		 @param RMSE 平差的单位权中误差
		 @return 操作成功或失败
		*/
//		public final boolean fourParameterCalculation(double[] nxcoord, double[] nycoord, double[] oxcoord, double[] oycoord, RefObject<Double> x0, RefObject<Double> y0, RefObject<Double> m, RefObject<Double> alpha, RefObject<Double> RMSE, RefObject<String> errorString)
//		{
//			if (nxcoord == null || nycoord == null || oxcoord == null || oycoord == null)
//			{
//				errorString.argvalue = "输入数组为空！";
//				return false;
//			}
//			int nLength = nxcoord.length;
//			if (nLength != nycoord.length || nLength != oxcoord.length || nLength != oycoord.length || nycoord.length != oxcoord.length || nycoord.length != oycoord.length || oxcoord.length != oycoord.length)
//			{
//				errorString.argvalue = "坐标数据个数不一致，输入有误！";
//				return false;
//			}
//			if (nLength == 0)
//			{
//				errorString.argvalue = "输入数组长度为零！";
//				return false;
//			}
//			if (nLength < 3)
//			{
//				errorString.argvalue = "四参数坐标计算至少需要三个点，请输入不少于三个点的测量数据！";
//				return false;
//			}
//
//			Matrix matN = new Matrix(4, 4);
//			Matrix matC = new Matrix(4, 1);
//			Matrix CmptDeltaX = new Matrix(4, 1);
//
//			double x0tmp, y0tmp, mtmp, alphatmp;
//			x0tmp = y0tmp = mtmp = alphatmp = 0.0;
//
//			boolean bDone = false;
//			double dCondition = 10000.0;
//			while (!bDone)
//			{
//				for (int i = 0; i < matN.NoRows; i++)
//				{
//					for (int j = 0; j < matN.NoCols; j++)
//					{
//						matN[i][j] = 0.0;
//					}
//				}
//				matN[0][0] = matN[1][1] = (double)nLength;
//				for (int i = 0; i < matC.NoRows; i++)
//				{
//					matC[i][0] = 0.0;
//				}
//
//				double ds = Math.sin(alphatmp);
//				double dc = Math.cos(alphatmp);
//				for (int i = 0; i < nLength; i++)
//				{
//					double p = oxcoord[i] * dc - oycoord[i] * ds;
//					double q = oxcoord[i] * ds + oycoord[i] * dc;
//					double r = -mtmp * oxcoord[i] * ds - mtmp * oycoord[i] * dc;
//					double s = mtmp * oxcoord[i] * dc - mtmp * oycoord[i] * ds;
//					double lx = nxcoord[i] - x0tmp - mtmp * oxcoord[i] * dc + mtmp * oycoord[i] * ds;
//					double ly = nycoord[i] - y0tmp - mtmp * oxcoord[i] * ds - mtmp * oycoord[i] * dc;
//
//					matC[0][0] += lx;
//					matC[1][0] += ly;
//					matC[2][0] += p * lx + q * ly;
//					matC[3][0] += r * lx + s * ly;
//
//					matN[2][2] += p * p + q * q;
//					matN[3][3] += r * r + s * s;
//
//					matN[2][0] += p;
//					matN[2][1] += q;
//					matN[3][0] += r;
//					matN[3][1] += s;
//					matN[3][2] += p * r + q * s;
//				}
//				matN[0][2] = matN[2][0];
//				matN[1][2] = matN[2][1];
//				matN[0][3] = matN[3][0];
//				matN[1][3] = matN[3][1];
//				matN[2][3] = matN[3][2];
//
//				CmptDeltaX = UtilityClass.SolvingNormalEquation(matN, matC);
//
//				double dTemp = 0.0;
//				for (int i = 0; i < CmptDeltaX.NoRows; i++)
//				{
//					dTemp += CmptDeltaX[i][0] * CmptDeltaX[i][0];
//				}
//				dTemp = Math.sqrt(dTemp);
//				if (Math.abs(dTemp - dCondition) <= dEpsilon || dTemp <= dEpsilon)
//				{
//					bDone = true;
//
//					//计算残差和拟合单位权中误差
//					double sumRE = 0.0;
//					double[] residualErrorArray;
//					residualErrorArray = new double[2 * nLength];
//					for (int i = 0; i < nLength; i++)
//					{
//						double p = oxcoord[i] * dc - oycoord[i] * ds;
//						double q = oxcoord[i] * ds + oycoord[i] * dc;
//						double r = -mtmp * oxcoord[i] * ds - mtmp * oycoord[i] * dc;
//						double s = mtmp * oxcoord[i] * dc - mtmp * oycoord[i] * ds;
//						double lx = nxcoord[i] - x0tmp - mtmp * oxcoord[i] * dc + mtmp * oycoord[i] * ds;
//						double ly = nycoord[i] - y0tmp - mtmp * oxcoord[i] * ds - mtmp * oycoord[i] * dc;
//
//						residualErrorArray[2 * i] = CmptDeltaX[0][0] + p * CmptDeltaX[2][0] + r * CmptDeltaX[3][0] - lx;
//						residualErrorArray[2 * i + 1] = CmptDeltaX[1][0] + q * CmptDeltaX[2][0] + s * CmptDeltaX[3][0] - ly;
//
//						sumRE += residualErrorArray[2 * i] * residualErrorArray[2 * i] + residualErrorArray[2 * i + 1] * residualErrorArray[2 * i + 1];
//					}
//
//					if (2 * nLength > 4)
//					{
//						RMSE.argvalue = Math.sqrt(sumRE / (2 * nLength - 4));
//					}
//					else
//					{
//						RMSE.argvalue = Math.sqrt(sumRE);
//					}
//
//					x0tmp += CmptDeltaX[0][0];
//					y0tmp += CmptDeltaX[1][0];
//					mtmp += CmptDeltaX[2][0];
//					alphatmp += CmptDeltaX[3][0];
//
//					break;
//				}
//				else
//				{
//					dCondition = dTemp;
//
//					x0tmp += CmptDeltaX[0][0];
//					y0tmp += CmptDeltaX[1][0];
//					mtmp += CmptDeltaX[2][0];
//					alphatmp += CmptDeltaX[3][0];
//				}
//			}
//
//			x0.argvalue = x0tmp;
//			y0.argvalue = y0tmp;
//			m.argvalue = mtmp;
//			alpha.argvalue = alphatmp;
//
//			return true;
//		}

		public final boolean fourParameterCalculation2(double[][] nNorthEast, double[][] oNorthEast)
		{
		   int M = nNorthEast.length;
		   int N = nNorthEast[0].length;

		   double[][] B = new double[M][4];
		   return true;
		}


//		/** 
//		 2, 七参数坐标变换(该算法有问题)
//		 从旧坐标系到新坐标系的坐标变化公式为：
//		 x = Tx + m*r11*x' + m*r12*y' + m*r13*h'
//		 y = Ty + m*r21*x' + m*r22*y' + m*r23*h'
//		 h = Th + m*r31*x' + m*r32*y' + m*r33*h'
//		 其中旋转矩阵TransMat = r = r(alpha)*r(beta)*r(gama);
//		 
//		 @param nxcoord 新坐标系下的x坐标
//		 @param nycoord 新坐标系下的y坐标
//		 @param nhcoord 新坐标系下的h坐标
//		 @param oxcoord 旧坐标系下的x坐标
//		 @param oycoord 旧坐标系下的y坐标
//		 @param ohcoord 旧坐标系下的h坐标
//		 @param Tx 坐标变换沿x轴的平移量
//		 @param Ty 坐标变换沿y轴的平移量
//		 @param Th 坐标变换沿h轴的平移量
//		 @param m 坐标变换的尺度因子
//		 @param TransMat 坐标变换的旋转矩阵
//		 @param alpha 坐标变换中绕x轴旋转的角度
//		 @param beta 坐标变换中绕y轴旋转的角度
//		 @param gama 坐标变换中绕z轴旋转的角度
//		 @param RMSE 平差的单位权中误差
//		 @param errorString 错误代码
//		 @return 操作成功或者失败
//		*/
//		public final boolean SevenParameterCalculation(double[] nxcoord, double[] nycoord, double[] nhcoord, double[] oxcoord, double[] oycoord, double[] ohcoord, RefObject<Double> Tx, RefObject<Double> Ty, RefObject<Double> Th, RefObject<Double> m, RefObject<Double[]> TransMat, RefObject<Double> alpha, RefObject<Double> beta, RefObject<Double> gama, RefObject<Double> RMSE, RefObject<String> errorString)
//		{
//			if (nxcoord == null || nycoord == null || nhcoord == null || oxcoord == null || oycoord == null || ohcoord == null)
//			{
//				errorString.argvalue = "输入数组为空！";
//				return false;
//			}
//			int nLength = nxcoord.length;
//			if (nLength != nycoord.length || nLength != nhcoord.length || nLength != oxcoord.length || nLength != oycoord.length || nLength != ohcoord.length || nycoord.length != nhcoord.length || nycoord.length != oxcoord.length || nycoord.length != oycoord.length || nycoord.length != ohcoord.length || nhcoord.length != oxcoord.length || nhcoord.length != oycoord.length || nhcoord.length != ohcoord.length || oxcoord.length != oycoord.length || oxcoord.length != ohcoord.length || oycoord.length != ohcoord.length)
//			{
//				errorString.argvalue = "坐标数据个数不一致，输入有误！";
//				return false;
//			}
//			if (nLength == 0)
//			{
//				errorString.argvalue = "输入数组长度为零！";
//				return false;
//			}
//			if (nLength < 4)
//			{
//				errorString.argvalue = "七参数坐标计算至少需要四个点，请输入不少于四个点的测量数据！";
//				return false;
//			}
//
//			Matrix matN = new Matrix(7, 7);
//			Matrix matC = new Matrix(7, 1);
//			Matrix CmptDeltaX = new Matrix(7, 1);
//
//			double x0tmp, y0tmp, h0tmp, mtmp, alphatmp, betatmp, gamatmp;
//			x0tmp = y0tmp = h0tmp = mtmp = alphatmp = betatmp = gamatmp = 0.0;
//
//			boolean bDone = false;
//			double dCondition = 10000.0;
//			while (!bDone)
//			{
//				for (int i = 0; i < matN.NoRows; i++)
//				{
//					for (int j = 0; j < matN.NoCols; j++)
//					{
//						matN[i][j] = 0.0;
//					}
//				}
//				matN[0][0] = matN[1][1] = matN[2][2] = (double)nLength;
//				for (int i = 0; i < matC.NoRows; i++)
//				{
//					matC[i][0] = 0.0;
//				}
//
//				double als = Math.sin(alphatmp);
//				double alc = Math.cos(alphatmp);
//				double bes = Math.sin(betatmp);
//				double bec = Math.cos(betatmp);
//				double gas = Math.sin(gamatmp);
//				double gac = Math.cos(gamatmp);
//				Matrix mR = new Matrix(3, 3);
//				mR[0][0] = bec * gac;
//				mR[0][1] = -bec * gas;
//				mR[0][2] = bes;
//				mR[1][0] = als * bes * gac + alc * gas;
//				mR[1][1] = -als * bes * gas + alc * gac;
//				mR[1][2] = -als * bec;
//				mR[2][0] = -alc * bes * gac + als * gas;
//				mR[2][1] = alc * bes * gas + als * gac;
//				mR[2][2] = alc * bec;
//				Matrix mRDiffa = new Matrix(3, 3);
//				mRDiffa[0][0] = 0.0;
//				mRDiffa[0][1] = 0.0;
//				mRDiffa[0][2] = 0.0;
//				mRDiffa[1][0] = alc * bes * gac - als * gac;
//				mRDiffa[1][1] = -alc * bes * gas - als * gac;
//				mRDiffa[1][2] = -alc * bec;
//				mRDiffa[2][0] = als * bes * gac + alc * gas;
//				mRDiffa[2][1] = -als * bes * gas + alc * gac;
//				mRDiffa[2][2] = -als * bec;
//				Matrix mRDiffb = new Matrix(3, 3);
//				mRDiffb[0][0] = -bes * gac;
//				mRDiffb[0][1] = bes * gas;
//				mRDiffb[0][2] = bec;
//				mRDiffb[1][0] = als * bec * gac;
//				mRDiffb[1][1] = -als * bec * gas;
//				mRDiffb[1][2] = als * bes;
//				mRDiffb[2][0] = -alc * bec * gac;
//				mRDiffb[2][1] = alc * bec * gas;
//				mRDiffb[2][2] = -alc * bes;
//				Matrix mRDiffg = new Matrix(3, 3);
//				mRDiffg[0][0] = -bec * gas;
//				mRDiffg[0][1] = -bec * gac;
//				mRDiffg[0][2] = 0.0;
//				mRDiffg[1][0] = -als * bes * gas + alc * gac;
//				mRDiffg[1][1] = -als * bes * gac - alc * gas;
//				mRDiffg[1][2] = 0.0;
//				mRDiffg[2][0] = alc * bes * gas + als * gac;
//				mRDiffg[2][1] = alc * bes * gac - als * gas;
//				mRDiffg[2][2] = 0.0;
//
//				for (int i = 0; i < nLength; i++)
//				{
//					double px = mR[0][0] * oxcoord[i] + mR[0][1] * oycoord[i] + mR[0][2] * ohcoord[i];
//					double py = mR[1][0] * oxcoord[i] + mR[1][1] * oycoord[i] + mR[1][2] * ohcoord[i];
//					double ph = mR[2][0] * oxcoord[i] + mR[2][1] * oycoord[i] + mR[2][2] * ohcoord[i];
//					double qx = mtmp * mRDiffa[0][0] * oxcoord[i] + mtmp * mRDiffa[0][1] * oycoord[i] + mtmp * mRDiffa[0][2] * ohcoord[i];
//					double qy = mtmp * mRDiffa[1][0] * oxcoord[i] + mtmp * mRDiffa[1][1] * oycoord[i] + mtmp * mRDiffa[1][2] * ohcoord[i];
//					double qh = mtmp * mRDiffa[2][0] * oxcoord[i] + mtmp * mRDiffa[2][1] * oycoord[i] + mtmp * mRDiffa[2][2] * ohcoord[i];
//					double rx = mtmp * mRDiffb[0][0] * oxcoord[i] + mtmp * mRDiffb[0][1] * oycoord[i] + mtmp * mRDiffb[0][2] * ohcoord[i];
//					double ry = mtmp * mRDiffb[1][0] * oxcoord[i] + mtmp * mRDiffb[1][1] * oycoord[i] + mtmp * mRDiffb[1][2] * ohcoord[i];
//					double rh = mtmp * mRDiffb[2][0] * oxcoord[i] + mtmp * mRDiffb[2][1] * oycoord[i] + mtmp * mRDiffb[2][2] * ohcoord[i];
//					double sx = mtmp * mRDiffg[0][0] * oxcoord[i] + mtmp * mRDiffg[0][1] * oycoord[i] + mtmp * mRDiffg[0][2] * ohcoord[i];
//					double sy = mtmp * mRDiffg[1][0] * oxcoord[i] + mtmp * mRDiffg[1][1] * oycoord[i] + mtmp * mRDiffg[1][2] * ohcoord[i];
//					double sh = mtmp * mRDiffg[2][0] * oxcoord[i] + mtmp * mRDiffg[2][1] * oycoord[i] + mtmp * mRDiffg[2][2] * ohcoord[i];
//
//					double lx = nxcoord[i] - x0tmp - px;
//					double ly = nycoord[i] - y0tmp - py;
//					double lh = nhcoord[i] - h0tmp - ph;
//
//					matC[0][0] += lx;
//					matC[1][0] += ly;
//					matC[2][0] += lh;
//					matC[3][0] += px * lx + py * ly + ph * lh;
//					matC[4][0] += qx * lx + qy * ly + qh * lh;
//					matC[5][0] += rx * lx + ry * ly + rh * lh;
//					matC[6][0] += sx * lx + sy * ly + sh * lh;
//
//					matN[3][3] += px * px + py * py + ph * ph;
//					matN[4][4] += qx * qx + qy * qy + qh * qh;
//					matN[5][5] += rx * rx + ry * ry + rh * rh;
//					matN[6][6] += sx * sx + sy * sy + sh * sh;
//
//					matN[3][0] += px;
//					matN[3][1] += py;
//					matN[3][2] += py;
//					matN[4][0] += qx;
//					matN[4][1] += qy;
//					matN[4][2] += qh;
//					matN[4][3] += px * qx + py * qy + ph * qh;
//					matN[5][0] += rx;
//					matN[5][1] += ry;
//					matN[5][2] += rh;
//					matN[5][3] += px * rx + py * ry + ph * rh;
//					matN[5][4] += qx * rx + qy * ry + qh * rh;
//					matN[6][0] += sx;
//					matN[6][1] += sy;
//					matN[6][2] += sh;
//					matN[6][3] += px * sx + py * sy + ph * sh;
//					matN[6][4] += qx * sx + qy * sy + qh * sh;
//					matN[6][5] += rx * sx + ry * sy + rh * sh;
//				}
//				matN[0][3] = matN[3][0];
//				matN[1][3] = matN[3][1];
//				matN[2][3] = matN[3][2];
//				matN[0][4] = matN[4][0];
//				matN[1][4] = matN[4][1];
//				matN[2][4] = matN[4][2];
//				matN[3][4] = matN[4][3];
//				matN[0][5] = matN[5][0];
//				matN[1][5] = matN[5][1];
//				matN[2][5] = matN[5][2];
//				matN[3][5] = matN[5][3];
//				matN[4][5] = matN[5][4];
//				matN[0][6] = matN[6][0];
//				matN[1][6] = matN[6][1];
//				matN[2][6] = matN[6][2];
//				matN[3][6] = matN[6][3];
//				matN[4][6] = matN[6][4];
//				matN[5][6] = matN[6][5];
//
//				CmptDeltaX = UtilityClass.SolvingNormalEquation(matN, matC);
//
//				double dTemp = 0.0;
//				for (int i = 0; i < CmptDeltaX.NoRows; i++)
//				{
//					dTemp += CmptDeltaX[i][0] * CmptDeltaX[i][0];
//				}
//				dTemp = Math.sqrt(dTemp);
//				if (Math.abs(dTemp - dCondition) <= dEpsilon || dTemp <= dEpsilon)
//				{
//					bDone = true;
//
//					//计算残差和拟合单位权中误差
//					double sumRE = 0.0;
//					double[] residualErrorArray;
//					residualErrorArray = new double[3 * nLength];
//					for (int i = 0; i < nLength; i++)
//					{
//						double px = mR[0][0] * oxcoord[i] + mR[0][1] * oycoord[i] + mR[0][2] * ohcoord[i];
//						double py = mR[1][0] * oxcoord[i] + mR[1][1] * oycoord[i] + mR[1][2] * ohcoord[i];
//						double ph = mR[2][0] * oxcoord[i] + mR[2][1] * oycoord[i] + mR[2][2] * ohcoord[i];
//						double qx = mtmp * mRDiffa[0][0] * oxcoord[i] + mtmp * mRDiffa[0][1] * oycoord[i] + mtmp * mRDiffa[0][2] * ohcoord[i];
//						double qy = mtmp * mRDiffa[1][0] * oxcoord[i] + mtmp * mRDiffa[1][1] * oycoord[i] + mtmp * mRDiffa[1][2] * ohcoord[i];
//						double qh = mtmp * mRDiffa[2][0] * oxcoord[i] + mtmp * mRDiffa[2][1] * oycoord[i] + mtmp * mRDiffa[2][2] * ohcoord[i];
//						double rx = mtmp * mRDiffb[0][0] * oxcoord[i] + mtmp * mRDiffb[0][1] * oycoord[i] + mtmp * mRDiffb[0][2] * ohcoord[i];
//						double ry = mtmp * mRDiffb[1][0] * oxcoord[i] + mtmp * mRDiffb[1][1] * oycoord[i] + mtmp * mRDiffb[1][2] * ohcoord[i];
//						double rh = mtmp * mRDiffb[2][0] * oxcoord[i] + mtmp * mRDiffb[2][1] * oycoord[i] + mtmp * mRDiffb[2][2] * ohcoord[i];
//						double sx = mtmp * mRDiffg[0][0] * oxcoord[i] + mtmp * mRDiffg[0][1] * oycoord[i] + mtmp * mRDiffg[0][2] * ohcoord[i];
//						double sy = mtmp * mRDiffg[1][0] * oxcoord[i] + mtmp * mRDiffg[1][1] * oycoord[i] + mtmp * mRDiffg[1][2] * ohcoord[i];
//						double sh = mtmp * mRDiffg[2][0] * oxcoord[i] + mtmp * mRDiffg[2][1] * oycoord[i] + mtmp * mRDiffg[2][2] * ohcoord[i];
//
//						double lx = nxcoord[i] - x0tmp - px;
//						double ly = nycoord[i] - y0tmp - py;
//						double lh = nhcoord[i] - h0tmp - ph;
//
//						residualErrorArray[3 * i] = CmptDeltaX[0][0] + px * CmptDeltaX[3][0] + qx * CmptDeltaX[4][0] + rx * CmptDeltaX[5][0] + sx * CmptDeltaX[6][0] - lx;
//						residualErrorArray[3 * i + 1] = CmptDeltaX[1][0] + py * CmptDeltaX[3][0] + qy * CmptDeltaX[4][0] + ry * CmptDeltaX[5][0] + sy * CmptDeltaX[6][0] - ly;
//						residualErrorArray[3 * i + 2] = CmptDeltaX[2][0] + ph * CmptDeltaX[3][0] + qh * CmptDeltaX[4][0] + rh * CmptDeltaX[5][0] + sh * CmptDeltaX[6][0] - lh;
//
//						sumRE += residualErrorArray[3 * i] * residualErrorArray[3 * i] + residualErrorArray[3 * i + 1] * residualErrorArray[3 * i + 1] + residualErrorArray[3 * i + 2] * residualErrorArray[3 * i + 2];
//					}
//
//					if (3 * nLength > 7)
//					{
//						RMSE.argvalue = Math.sqrt(sumRE / (3 * nLength - 7));
//					}
//					else
//					{
//						RMSE.argvalue = Math.sqrt(sumRE);
//					}
//
//					x0tmp += CmptDeltaX[0][0];
//					y0tmp += CmptDeltaX[1][0];
//					h0tmp += CmptDeltaX[2][0];
//					mtmp += CmptDeltaX[3][0];
//					alphatmp += CmptDeltaX[4][0];
//					betatmp += CmptDeltaX[5][0];
//					gamatmp += CmptDeltaX[6][0];
//
//					break;
//				}
//				else
//				{
//					dCondition = dTemp;
//
//					x0tmp += CmptDeltaX[0][0];
//					y0tmp += CmptDeltaX[1][0];
//					h0tmp += CmptDeltaX[2][0];
//					mtmp += CmptDeltaX[3][0];
//					alphatmp += CmptDeltaX[4][0];
//					betatmp += CmptDeltaX[5][0];
//					gamatmp += CmptDeltaX[6][0];
//				}
//			}
//
//			Tx.argvalue = x0tmp;
//			Ty.argvalue = y0tmp;
//			Th.argvalue = h0tmp;
//			m.argvalue = mtmp;
//			alpha.argvalue = alphatmp;
//			beta.argvalue = betatmp;
//			gama.argvalue = gamatmp;
//
//			TransMat.argvalue[0] = Math.cos(beta.argvalue) * Math.cos(gama.argvalue);
//			TransMat.argvalue[1] = -Math.cos(beta.argvalue) * Math.sin(gama.argvalue);
//			TransMat.argvalue[2] = Math.sin(beta.argvalue);
//			TransMat.argvalue[3] = Math.sin(alpha.argvalue) * Math.sin(beta.argvalue) * Math.cos(gama.argvalue) + Math.cos(alpha.argvalue) * Math.sin(gama.argvalue);
//			TransMat.argvalue[4] = -Math.sin(alpha.argvalue) * Math.sin(beta.argvalue) * Math.sin(gama.argvalue) + Math.cos(alpha.argvalue) * Math.cos(gama.argvalue);
//			TransMat.argvalue[5] = -Math.sin(alpha.argvalue) * Math.cos(beta.argvalue);
//			TransMat.argvalue[6] = -Math.cos(alpha.argvalue) * Math.sin(beta.argvalue) * Math.cos(gama.argvalue) + Math.sin(alpha.argvalue) * Math.sin(gama.argvalue);
//			TransMat.argvalue[7] = Math.cos(alpha.argvalue) * Math.sin(beta.argvalue) * Math.sin(gama.argvalue) + Math.sin(alpha.argvalue) * Math.cos(gama.argvalue);
//			TransMat.argvalue[8] = Math.cos(alpha.argvalue) * Math.cos(beta.argvalue);
//
//			return true;
//		}
		
		/** 
		 返回值是函数的系数
		 
		 @param Guass
		 @param n
		 @return 
		*/
		public static double[] computGauss(double[][] Guass, int n)
		{
			int i, j;
			int k, m;
			double temp;
			double max;
			double s;
			double[] x = new double[n];
			for (i = 0; i < n; i++) //初始化
			{
				x[i] = 0.0;
			}

			for (j = 0; j < n; j++)
			{
				max = 0;
				k = j;
				for (i = j; i < n; i++)
				{
					if (Math.abs(Guass[i][j]) > max)
					{
						max = Guass[i][j];
						k = i;
					}
				}


				if (k != j)
				{
					for (m = j; m < n + 1; m++)
					{
						temp = Guass[j][m];
						Guass[j][m] = Guass[k][m];
						Guass[k][m] = temp;
					}
				}
				if (0 == max)
				{
					// "此线性方程为奇异线性方程" 
					return x;
				}

				for (i = j + 1; i < n; i++)
				{
					s = Guass[i][j];
					for (m = j; m < n + 1; m++)
					{
						Guass[i][m] = Guass[i][m] - Guass[j][m] * s / (Guass[j][j]);
					}
				}

			} //结束for (j=0;j<n;j++)

			for (i = n - 1; i >= 0; i--)
			{
				s = 0;
				for (j = i + 1; j < n; j++)
				{
					s = s + Guass[i][j] * x[j];
				}
				x[i] = (Guass[i][n] - s) / Guass[i][i];
			}
			return x;
		}
		
		/** 
		用最小二乘法拟合二元多次曲线
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
		*/
		public  boolean multiLine(double[] arrX, double[] arrY, int dimension)
		{
			double[]refarrYOffset = new double[arrY.length];
			double[]refarrY = new double[arrY.length];
			if (arrX.length < 2 || arrY.length < 2)
			{
				this.mathReturnParam.getMutiLineParam().setErrorString("坐标集合个数不能小于2个！");
				return false;
			}
			if (arrX.length != arrY.length)
			{
				this.mathReturnParam.getMutiLineParam().setErrorString("x坐标集合与y坐标集合个数不一致！");
				return false;
			}
			int n = dimension + 1; //dimension次方程需要求 dimension+1个系数
			int length = arrX.length; //已知点的个数
			double[][] guass = new double[n][n + 1]; //高斯矩阵 例如：y=a0+a1*x+a2*x*x
			for (int i = 0; i < n; i++)
			{
				int j;
				for (j = 0; j < n; j++)
				{
					guass[i][j] = sumArr(arrX, j + i, length);
				}
				guass[i][j] = sumArr(arrX, i, arrY, 1, length);
			}
			this.mathReturnParam.getMutiLineParam().setParameter(computGauss(guass, n));
			for (int i = 0; i < arrY.length; i++)
			{
				double data = this.mathReturnParam.getMutiLineParam().getParameter()[0];
				for (int j = 1; j <= dimension; j++)
				{
					data += this.mathReturnParam.getMutiLineParam().getParameter()[j] * Math.pow(arrX[i], dimension);
					;
				}
				refarrYOffset[i] = arrY[i] - data;
				refarrY[i] = data;
			}
			this.mathReturnParam.getMutiLineParam().setRefarrYOffset(refarrYOffset);
			this.mathReturnParam.getMutiLineParam().setRefarrY(refarrY);
			return true;
		}
		
		/** 
		 求数组的元素的n次方的和
		 @param arr
		 @param n
		 @param length
		 @return 
		*/
		public static double sumArr(double[] arr, int n, int length)
		{
			double s = 0;
			for (int i = 0; i < length; i++)
			{
				if (arr[i] != 0 || n != 0)
				{
					s = s + Math.pow(arr[i], n);
				}
				else
				{
					s = s + 1;
				}
			}
			return s;
		}
		public static double sumArr(double[] arr1, int n1, double[] arr2, int n2, int length)
		{
			double s = 0;
			for (int i = 0; i < length; i++)
			{
				if ((arr1[i] != 0 || n1 != 0) && (arr2[i] != 0 || n2 != 0))
				{
					s = s + Math.pow(arr1[i], n1) * Math.pow(arr2[i], n2);
				}
				else
				{
					s = s + 1;
				}
			}
			return s;

		}
		
		/**
		 * 返回一个double数组的平均值
		 */
		private double average(double[]data){
			double sum=0.0;
			for(int i=0;i<data.length;i++){
				sum+=data[i];
			}
			return sum/(data.length);
		}

		/** 
		 平面计算
		 X = x0 + k * Math.Cos(alpha) * (x - OldXAvg) + k * Math.Sin(alpha) * (y - OldYAvg);
		 Y = y0 + k * Math.Cos(alpha) * (y - OldYAvg) - k * Math.Sin(alpha) * (x - OldXAvg);
		 或
		 X = x0 + a * (x - OldXAvg) + b * (Y - OldYAvg);
		 Y = y0 + a * (Y - OldYAvg) - b * (X - OldXAvg);
		 @param nxcoord
		 @param nycoord
		 @param oxcoord
		 @param oycoord
		 @param x0
		 @param y0
		 @param a
		 @param b
		 @param k
		 @param alpha
		 @param xoffset
		 @param yoffset
		 @param errorString
		 @return 
		*/
		public  boolean planeFitting(double[] nxcoord, double[] nycoord, double[] oxcoord, double[] oycoord)
		{
			double[] xoffset=new double[nxcoord.length];
			double[] yoffset=new double[nxcoord.length];
			if (nxcoord == null || nycoord == null || oxcoord == null || oycoord == null)
			{
				this.mathReturnParam.getPlaneFitting().setErrorString("输入数组为空！");
				return false;
			}
			int nLength = nxcoord.length;
			if (nLength != nycoord.length || nLength != oxcoord.length || nLength != oycoord.length || nycoord.length != oxcoord.length || nycoord.length != oycoord.length || oxcoord.length != oycoord.length)
			{
				this.mathReturnParam.getPlaneFitting().setErrorString("坐标数据个数不一致，输入有误！");
				return false;
			}
			if (nLength == 0)
			{
				this.mathReturnParam.getPlaneFitting().setErrorString("输入数组长度为零！");
				return false;
			}
			if (nLength < 2)
			{
				this.mathReturnParam.getPlaneFitting().setErrorString("坐标计算至少需要2个点，请输入不少于2个点的测量数据！");
				return false;
			}
			this.mathReturnParam.getPlaneFitting().setX0(average(nxcoord));//新北坐标平均值
			this.mathReturnParam.getPlaneFitting().setY0(average(nycoord));//新东坐标平均值
			this.mathReturnParam.getPlaneFitting().setOldAAvg(average(oxcoord));
			this.mathReturnParam.getPlaneFitting().setOldYAvg(average(oycoord));
			double[] Coxcoord = new double[oxcoord.length]; //Δx
			double[] Coycoord = new double[oxcoord.length]; //Δy
			double D = 0.0;
			double sum1 = 0.0;
			double sum2 = 0.0;
			for (int i = 0; i < oxcoord.length; i++)
			{
				Coxcoord[i] = oxcoord[i] - this.mathReturnParam.getPlaneFitting().getOldYAvg();
				Coycoord[i] = oycoord[i] - this.mathReturnParam.getPlaneFitting().getOldYAvg();
				D += Coxcoord[i] * Coxcoord[i] + Coycoord[i] * Coycoord[i];
				sum1 += nxcoord[i] * Coxcoord[i] + nycoord[i] * Coycoord[i];
				sum2 += nxcoord[i] * Coycoord[i] - nycoord[i] * Coxcoord[i];
			}
			double a = sum1 / D;
			double b = sum2 / D;
			this.mathReturnParam.getPlaneFitting().setK(Math.sqrt(a * a + b * b));
			this.mathReturnParam.getPlaneFitting().setAlpha(Math.atan(b / a));
			double VVx = 0.0;
			double VVy = 0.0;
			for (int i = 0; i < nxcoord.length; i++)
			{
				xoffset[i] = nxcoord[i] - (this.mathReturnParam.getPlaneFitting().getX0() + (a * Coxcoord[i] + b * Coycoord[i]));
				yoffset[i] = nycoord[i] - (this.mathReturnParam.getPlaneFitting().getY0() + (a * Coycoord[i] - b * Coxcoord[i]));
				VVx += xoffset[i] * xoffset[i];
				VVy += yoffset[i] * yoffset[i];
			}
			double RMSE_x = Math.sqrt(VVx / xoffset.length); //X转换中误差
			double RMSE_y = Math.sqrt(VVy / yoffset.length); //Y转换中误差
			this.mathReturnParam.getPlaneFitting().setrMSE(Math.sqrt(RMSE_x * RMSE_x + RMSE_y * RMSE_y));//点位中误差
			this.mathReturnParam.getPlaneFitting().setXoffset(xoffset);
			this.mathReturnParam.getPlaneFitting().setYoffset(yoffset);
			return true;
		}
		
		/** 
		 后方交会计算测站高程
		 @param Height
		 @param Va
		 @param SD
		 @param InsHeight
		 @return 
		*/
		public  boolean resectionHeight(double[] Height, double[] Va, double[] SD)
		{
			
			this.mathReturnParam.setInsHeight(0.0);
			if (Height.length != Va.length || Height.length != SD.length || Va.length == 0)
			{
				return false;
			}
			int M = Va.length;
			double[] DiffH = new double[M];
			for(int i=0;i< M;i++)
			{
				DiffH[i] = Math.cos(Va[i]) * SD[i];
			}
			double[][] B = new double[M][1];
			for (int i = 0; i < M;i++)
			{
				B[i][1] = -1;
			}
			double[][] Bt =new Matrix(B).transpose().getArray();
			double[][] l = new double[M][1];
			double[][] Q = new double[M][M];
			for (int i = 0; i < M; i++)
			{
				Q[i][i] = SD[i];
			}
			double[][] P=new Matrix(Q).inverse().getArray();
			double[][] BtP=(new Matrix(Bt).times(new Matrix(P))).getArray();
			double[][] Nbb=(new Matrix(BtP).times(new Matrix(P))).getArray();
			double[][] U=(new Matrix(Bt).times(new Matrix(l))).getArray();
			double[][] H=(new Matrix(Nbb).times(new Matrix(U))).getArray();
			this.mathReturnParam.setInsHeight(H[0][0]);
			return true;
		}
		
		/** 
		 高程计算
		 H = h0 + k * Math.Cos(alpha) * (h - OldHAvg);
		 或
		 H = h0 + a * (h - OldHAvg);
		 @param nhcoord
		 @param nycoord
		 @param ohcoord
		 @param h0
		 @param y0
		 @param k
		 @param alpha
		 @param OldHAvg
		 @param OldYAvg
		 @param hoffset
		 @param RMSE
		 @param errorString
		 @return 
		*/
		public final boolean heightFitting(double[] nhcoord, double[] ohcoord)
		{
			double[] hoffset=new double[nhcoord.length];
			if (nhcoord == null || ohcoord == null)
			{
				this.mathReturnParam.getHeightFitting().setErrorString("输入数组为空！");
				return false;
			}
			int nLength = nhcoord.length;
			if (nLength != ohcoord.length)
			{
				this.mathReturnParam.getHeightFitting().setErrorString("坐标数据个数不一致，输入有误！");
				return false;
			}
			if (nLength == 0)
			{
				this.mathReturnParam.getHeightFitting().setErrorString("输入数组长度为零！");
				return false;
			}
			if (nLength < 2)
			{
				this.mathReturnParam.getHeightFitting().setErrorString("高程计算至少需要2个点，请输入不少于2个点的测量数据！");
				return false;
			}
			this.mathReturnParam.getHeightFitting().setH0(average(nhcoord));//新高程平均值
			this.mathReturnParam.getHeightFitting().setOldHAvg(average(ohcoord));
			double[] Cohcoord = new double[ohcoord.length]; //Δh
			double D = 0.0;
			double sum1 = 0.0;
			double sum2 = 0.0;
			for (int i = 0; i < ohcoord.length; i++)
			{
				Cohcoord[i] = ohcoord[i] - this.mathReturnParam.getHeightFitting().getOldHAvg();
				D += Cohcoord[i] * Cohcoord[i];
				sum1 += nhcoord[i] * Cohcoord[i];
			}
			double a = sum1 / D;
			double b = sum2 / D;
			this.mathReturnParam.getHeightFitting().setK(Math.sqrt(a * a + b * b)); 
			this.mathReturnParam.getHeightFitting().setAlpha(Math.atan(b / a));
			double VVh = 0.0;
			for (int i = 0; i < nhcoord.length; i++)
			{
				hoffset[i] = nhcoord[i] - (this.mathReturnParam.getHeightFitting().getH0() + a * Cohcoord[i]);
				VVh += hoffset[i] * hoffset[i];
			}
			this.mathReturnParam.getHeightFitting().setrMSE(Math.sqrt(VVh / hoffset.length)); //H转换中误差
			this.mathReturnParam.getHeightFitting().setHoffset(hoffset);
			return true;
		}
		
		/** 
		 无定向导线平差
		 @param E
		 @param N
		 @param H
		 @param MeaHa
		 @param MeaVa
		 @param MeaSD
		 @param InsH
		 @param PrismH
		 @return 
		*/
		public final boolean astaticTraverseAdjustment(double[] e, double[] n, double[] h, double[] meaHa, double[] meaVa, double[] meaHD,double[] controlPointsE,double[] controlPointsN)
		{
			java.util.ArrayList<Double> hD =new java.util.ArrayList<Double>();
			int AngleCount = (meaHa.length - 2) / 2 + 2; //角度个数
			java.util.ArrayList<Double> ha = new java.util.ArrayList<Double>();
			java.util.ArrayList<Double> eIncrementCoordinate = new java.util.ArrayList<Double>(); //东坐标增量列表
			java.util.ArrayList<Double> nIncrementCoordinate = new java.util.ArrayList<Double>(); //北坐标增量列表

			meaHa[0] = radianAjust(meaHa[0]+Math.PI);
			for (int i = 2; i < meaHa.length - 1; i++)
			{ //不同测站的角度转到统一坐标系
				if (!isOdd(i))
				{ //偶数进来
					meaHa[i + 1] = (meaHa[i + 1] + (meaHa[i - 1] - radianAjust(radianAjust(radianAjust(meaHa[i]+Math.PI)))));
					meaHD[i - 1] = (meaHD[i - 1] + meaHD[i]) / 2;
				}
			}
			ha.add(meaHa[0]);
			hD.add(meaHD[0]);
			for (int i = 0; i < meaHa.length; i++)
			{
				if (isOdd(i))
				{ //奇数进来
					ha.add(meaHa[i]);
					hD.add(meaHD[i]);
				}
			}
			for (int i = 0; i < ha.size();i++)
			{
				nIncrementCoordinate.set(i, hD.get(i) * Math.cos(ha.get(i)));
				eIncrementCoordinate.set(i, hD.get(i) * Math.sin(ha.get(i)));
			}
			double sumNIncCoord = 0.0;
			double sumEIncCoord = 0.0;
			double sumMeaHD = 0.0;
			for (int i = 0; i < eIncrementCoordinate.size();)
			{
				sumNIncCoord += nIncrementCoordinate.get(i);
				sumEIncCoord += eIncrementCoordinate.get(i);
				sumMeaHD += hD.get(i);
			}
			double JSATan = Math.atan2(sumNIncCoord, sumEIncCoord);
			double SJATan = Math.atan2(n[1] - n[0], e[1] - e[0]);
			double fx = sumNIncCoord - (n[1] - n[0]);
			double fy = sumEIncCoord - (e[1] - e[0]);
			for (int i = 0; i < ha.size(); i++)
			{
				ha.set(i, ha.get(i) - (JSATan - SJATan));
			}
			double k = Math.sqrt((n[0] - n[1]) * (n[0] - n[1]) + (e[0] - e[1]) * (e[0] - e[1])) / Math.sqrt(sumNIncCoord * sumNIncCoord + sumEIncCoord * sumEIncCoord);
			for (int i = 0; i < hD.size();i++)
			{
				hD.set(i, hD.get(i) * k);
			}


			for (int i = 0; i < ha.size(); i++)
			{
				nIncrementCoordinate.set(i, hD.get(i) * Math.cos(ha.get(i)));
				eIncrementCoordinate.set(i, hD.get(i) * Math.sin(ha.get(i)));
			}
			for (int i = 0; i < eIncrementCoordinate.size();)
			{
				sumNIncCoord += nIncrementCoordinate.get(i);
				sumEIncCoord += eIncrementCoordinate.get(i);
				sumMeaHD += hD.get(i);
			}
			this.mathReturnParam.getTraverseAdjustPlane().sethD(hD);
			//如果算的Fx和Fy都非常接近零，证明算法没问题。
			fx = sumNIncCoord - (n[1] - n[0]);
			fy = sumEIncCoord - (e[1] - e[0]);
			controlPointsE[1]=e[0];
			controlPointsN[1]=n[0];
			for (int i = 1; i < controlPointsE.length;i++)
			{
				controlPointsE[i]=controlPointsE[i] + eIncrementCoordinate.get(i - 1);
				controlPointsN[i]= controlPointsN[i] + nIncrementCoordinate.get(i - 1);
			}
			this.mathReturnParam.getTraverseAdjustPlane().setControlPointE(controlPointsE);
			this.mathReturnParam.getTraverseAdjustPlane().setControlPointN(controlPointsN);
			this.mathReturnParam.getTraverseAdjustPlane().sethD(hD);
			return true;
		}
		
		/**
		 * double数组求和
		 */
		private double sumList(ArrayList<Double> data){
			double sum=0.0;
			for(double i:data){
				sum+=i;
			}
			return sum;
		}
		
		/** 
		 无定向导线计算高程
		 @param H
		 @param MeaVa
		 @param MeaSD
		 @param InsH 仪器高（前面加一个为零的仪器高）
		 @param PrismH
		 @param ControlPointsH
		 @return 
		*/
		public final boolean astaticTraverseAdjustment(double[] H, double[] meaVa, double[] meaSD, double[] InsH, double[] prismH, double[] controlPointsH)
		{
			double[] meaDisH = new double[meaVa.length];
			int j = 0;
			meaDisH[0] = -(Math.cos(meaVa[0]) * meaSD[0] + InsH[1] - prismH[0]);
			for (int i = 1; i < meaVa.length;i++)
			{
				if (isOdd(i))
				{ //奇数项
					meaDisH[i] = Math.cos(meaVa[i]) * meaSD[i] + InsH[i] - prismH[i + 1];
				}
				else
				{
					j++;
					meaDisH[i] = -(Math.cos(meaVa[i]) * meaSD[i] + InsH[j] - prismH[i - 1]);
				}
			}
			for (int i = 0; i < meaDisH.length; i++)
			{
				if (!isOdd(i))
				{
					meaDisH[i - 1] = (meaDisH[i - 1] + meaDisH[i]) / 2;
				}
			}
			java.util.ArrayList<Double> dHigh = new java.util.ArrayList<Double>();
			for (int i = 0; i < meaDisH.length; i++)
			{
				if(isOdd(i))
				{
					dHigh.add(meaDisH[i]); //奇数项
				}
			}
			double sumDHigh = sumList(dHigh);
			double highMisclosure = H[1] - H[0] - sumDHigh; //高程闭合差
			java.util.ArrayList<Double> sumSDList = new java.util.ArrayList<Double>();
			for (int i = 0; i < meaSD.length; i++)
			{
				if (isOdd(i))
				{
					sumSDList.add(meaSD[i]); //奇数项
				}
			}
			for (int i = 0; i < meaDisH.length; i++)
			{
				dHigh.set(i, dHigh.get(i) * (1 + sumSDList.get(i)/sumList(sumSDList)));
			}
			sumDHigh = sumList(dHigh);
			highMisclosure = H[1] - H[0] - sumDHigh; //如果计算无误，该值非常接近零
			controlPointsH[0] = H[0];
			for (int i = 1; i < controlPointsH.length;i++)
			{
				controlPointsH[i] = controlPointsH[i - 1] + dHigh.get(i - 1);
			}
			this.mathReturnParam.getTraverseAdjustHeight().setControlPointH(controlPointsH);
			return true;
		}
		
		/** 
		 判断奇数
		 @param n
		 @return 返回true奇数
		*/
		public static boolean isOdd(int n)
		{
			if(n%2!=0){
				return true;
			}
			else{
				return false;
			}
		}
		/**
		 * 
		 * 得出时间毫秒值
		 * @return
		 */
		public static long fromDateStringToLong(String inVal) { //此方法计算时间毫秒
			Date date = null; // 定义时间类型
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = inputFormat.parse(inVal); // 将字符型转换成日期型
			} catch (Exception e) {
				e.printStackTrace();
			}
			return date.getTime(); // 返回毫秒数
		}
		
		/**
	     * date2比date1多的天数
	     * @param date1    
	     * @param date2
	     * @return    
	     */
	    public static int differentDays(Date date1,Date date2)
	    {
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	       int day1= cal1.get(Calendar.DAY_OF_YEAR);
	        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
	        
	        int year1 = cal1.get(Calendar.YEAR);
	        int year2 = cal2.get(Calendar.YEAR);
	        if(year1 != year2)   //同一年
	        {
	            int timeDistance = 0 ;
	            for(int i = year1 ; i < year2 ; i ++)
	            {
	                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
	                {
	                    timeDistance += 366;
	                }
	                else    //不是闰年
	                {
	                    timeDistance += 365;
	                }
	            }
	            
	            return timeDistance + (day2-day1) ;
	        }
	        else    //不同年
	        {
	            System.out.println("判断day2 - day1 : " + (day2-day1));
	            return day2-day1;
	        }
	    }
	    
	    /**
	     * 
	     * 比较两天年月日是否相等
	     * @date  2017年12月12日 下午2:25:28
	     * 
	     * @param d1
	     * @param d2
	     * @return
	     * boolean
	     * @throws null
	     * 
	     * @version v1.0
	     * @author  姚家俊
	     * <p>Modification History:</p>
	     * <p>Date         Author      Version     Description</p>
	     * <p> -----------------------------------------------------------------</p>
	     * <p>2017年12月12日     姚家俊      v1.0          create</p>
	     *
	     */
	    public static boolean sameDate(Date d1, Date d2) {  
	        LocalDate localDate1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();  
	        LocalDate localDate2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();  
	        return localDate1.isEqual(localDate2);  
	    } 

	}






