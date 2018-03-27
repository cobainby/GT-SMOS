package com.southgt.smosplat.data.util.math;

/** 
计算扩展方法

*/
public final class AngleConvet
{
	private GtMath gtMath;
	
	public AngleConvet() {
		super();
		this.gtMath=new GtMath();
	}
	/** 
	 将弧度转换成度分秒
	 @param radian 弧度
	 @return 度分秒
	*/
	public static double radianToDegree(double radian)
	{
		return GtMath.radianToDegree(radian, 1);
	}
	
	/** 
	 将弧度转换成十进制度
	 @param radian 弧度
	 @return 十进制度
	*/
	public static double RadianToDecimalDegree(double radian)
	{
		return GtMath.radianToDegree(radian, 0);
	}
	
	/** 
	 十进制度分秒转换成度分秒
	 
	 @param decDegree 十进制度
	 @return 度
	*/
	public static double DecimalDegreeToDegree(double decDegree)
	{
		return GtMath.exAngle(decDegree, (short) 1);
	}
	
	/** 
	 度分秒转换成十进制度
	 @param degree 度分秒
	 @return 
	*/
	public static double DegreeToDecimalDegree(double degree)
	{
		return GtMath.exAngle(degree, (short) 0);
	}
	
	/** 
	 度分秒转成弧度
	 @param degree 度分秒
	 @return 弧度
	*/
	public static double DegreeToRadian(double degree)
	{
		return GtMath.exAngle(degree, (short) 2);
	}
	
	/** 
	 十进制度转换为弧度
	 @param decDegree 十进制度转
	 @return 弧度
	*/
	public static double DecimalDegreeToRadian(double decDegree)
	{
		return GtMath.exAngle(decDegree, (short) 3);
	}
	
	/** 
	 弧度转换为度分秒,(xx°xx′xx″)
	 @param radian 弧度
	 @return 度分秒形式的角度(xx°xx′xx″)
	*/
	public static String RadianToDegreeString(double radian)
	{
		double AbsRadian = 0.0;
		if (radian < 0)
		{
			AbsRadian = Math.abs(radian);
		}
		else
		{
			AbsRadian = radian;
		}
		double dD = GtMath.radianToDegree(AbsRadian, 1);
		double degree = (int)(dD);
		double second = (int)((dD - degree) * 100);
		double minute = (dD - degree - second / 100) * 10000;
		String strAngle = "";
		if (radian < 0)
		{
			strAngle = String.format("-%1$s°%2$s′%3$s″", degree, second, String.format("%.2f", minute));
		}
		else
		{
			strAngle = String.format("%1$s°%2$s′%3$s″", degree, second, String.format("%.2f", minute));
		}
		return strAngle;
	}
	
	/** 
	 弧度转换为度分秒,(xx°xx′xx″)后，只返回秒
	 @param radian 弧度
	 @return 秒形式的角度(xx″)
	*/
	public static String RadianToSecondString(double radian)
	{
		double AbsRadian = 0.0;
		if (radian < 0)
		{
			AbsRadian = Math.abs(radian);
		}
		else
		{
			AbsRadian = radian;
		}
		double dD = GtMath.radianToDegree(AbsRadian, 1);
		double degree = (int)(dD);
		double second = (int)((dD - degree) * 100);
		double minute = (dD - degree - second / 100) * 10000;
		String strAngle = "";
		if (radian < 0)
		{
			strAngle = String.format("-%3$s″", degree, second, String.format("%.2f", minute));
		}
		else
		{
			strAngle = String.format("%3$s″", degree, second, String.format("%.2f", minute));
		}
		return strAngle;
	}
	
	/** 
	 秒转成弧度
	 @param Second 秒
	 @return 弧度
	*/
	public static double SecondToRadian(double Second)
	{
		double strAngle = 0.0;
		return strAngle = Second / 3600 * Math.PI / 180;
	}
	
	/** 
	 弧度转换成秒
	 @param Radian
	 @return 
	*/
	public static double RadianToSecond(double Radian)
	{
		double strAngle = 0.0;
		return strAngle = Radian * 3600 / Math.PI * 180;
	}
	
	/** 
	 天顶距转换成垂直角
	 @param ZenithDis
	 @return 
	*/
	public static double ZdToVa(double ZenithDis)
	{
		double Angle = 0.0;
		if (ZenithDis <= Math.PI)
		{
			return Angle = Math.PI / 2 - ZenithDis;
		}
		else
		{
			return Angle = ZenithDis - Math.PI * 1.5;
		}
	}
}
