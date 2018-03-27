package com.southgt.smosplat.data.util.math;

/**
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 *
 * @ClassName cc 
 * @Description 仿射变换，距离可能会发生缩放    
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
	public class CAffinTransform_withScalar
	{
		private Math_ReturnParam mathReturnParam;
		public double[] designPoints;
		public double[] sourcePoints;
		public int iNum;
		public double[] CoordTransMatrix = new double[12];

		public CAffinTransform_withScalar()
		{
			this.mathReturnParam=new Math_ReturnParam();
		}

		private CAffinTransform_withScalar(double[] p1, double[] p2, int ilength)
		{
			designPoints = p1;
			sourcePoints = p2;
			iNum = ilength;
			if (ilength < 4) //点的个数太少，无法完全确定变换系数
			{
				return;
			}

			double[] tmp = new double[12];
			threeDimensionsCoordTrans(iNum, designPoints, sourcePoints, tmp);
			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 4; j++)
				{
					CoordTransMatrix[i * 4 + j] = tmp[j * 3 + i];
				}
			}
		}

		//**********************************************************************
//         计算三维空间多个点的仿射变换参数。CTM =  CoordTransMatrix
//             | x |	| CTM[0][0] CTM[1][0] CTM[2][0] | | X |	  | CTM[3][0] |
//             | y | = | CTM[0][1] CTM[1][1] CTM[2][1] |*| Y | + | CTM[3][1] |
//             | z |   | CTM[0][2] CTM[1][2] CTM[2][2] | | Z |	  | CTM[3][2] |
//                                                                                 
		//**********************************************************************
		public final void threeDimensionsCoordTrans(int iNum, double[] SourcePoints, double[] DesignPoints,double[]CoordTransMatrix)
		{
			double[] A, b;
			int i;

			A = new double[4 * 4];
			b = new double[4 * 3];

			for (i = 0; i < 4 * 4; i++)
			{
				A[i] = 0.0;
			}
			for (i = 0; i < 4 * 3; i++)
			{
				b[i] = 0.0;
			}

			for (i = 0; i < iNum; i++)
			{
				A[0 * 4 + 0] += SourcePoints[3 * i] * SourcePoints[3 * i];
				A[0 * 4 + 1] += SourcePoints[3 * i] * SourcePoints[3 * i + 1];
				A[0 * 4 + 2] += SourcePoints[3 * i] * SourcePoints[3 * i + 2];
				A[0 * 4 + 3] += SourcePoints[3 * i];

				A[1 * 4 + 1] += SourcePoints[3 * i + 1] * SourcePoints[3 * i + 1];
				A[1 * 4 + 2] += SourcePoints[3 * i + 1] * SourcePoints[3 * i + 2];
				A[1 * 4 + 3] += SourcePoints[3 * i + 1];

				A[2 * 4 + 2] += SourcePoints[3 * i + 2] * SourcePoints[3 * i + 2];
				A[2 * 4 + 3] += SourcePoints[3 * i + 2];


				b[0 * 3 + 0] += DesignPoints[3 * i] * SourcePoints[3 * i];
				b[1 * 3 + 0] += DesignPoints[3 * i] * SourcePoints[3 * i + 1];
				b[2 * 3 + 0] += DesignPoints[3 * i] * SourcePoints[3 * i + 2];
				b[3 * 3 + 0] += DesignPoints[3 * i];

				b[0 * 3 + 1] += DesignPoints[3 * i + 1] * SourcePoints[3 * i];
				b[1 * 3 + 1] += DesignPoints[3 * i + 1] * SourcePoints[3 * i + 1];
				b[2 * 3 + 1] += DesignPoints[3 * i + 1] * SourcePoints[3 * i + 2];
				b[3 * 3 + 1] += DesignPoints[3 * i + 1];

				b[0 * 3 + 2] += DesignPoints[3 * i + 2] * SourcePoints[3 * i];
				b[1 * 3 + 2] += DesignPoints[3 * i + 2] * SourcePoints[3 * i + 1];
				b[2 * 3 + 2] += DesignPoints[3 * i + 2] * SourcePoints[3 * i + 2];
				b[3 * 3 + 2] += DesignPoints[3 * i + 2];

			}

			A[1 * 4 + 0] = A[0 * 4 + 1];
			A[2 * 4 + 0] = A[0 * 4 + 2];
			A[2 * 4 + 1] = A[1 * 4 + 2];
			A[3 * 4 + 0] = A[0 * 4 + 3];
			A[3 * 4 + 1] = A[1 * 4 + 3];
			A[3 * 4 + 2] = A[2 * 4 + 3];
			A[3 * 4 + 3] = iNum;

			CLU_Decomposition LUDecomp = new CLU_Decomposition(A, 4, 4);
			if (LUDecomp.isNonsingular())
			{
				LUDecomp.solve(b, 3, CoordTransMatrix);
			}
			this.mathReturnParam.getVetor().setX(CoordTransMatrix);
			this.mathReturnParam.getVetor().setB(b);
		}


		//**********************************************************************
		// 给定一个点的坐标，计算坐标转换以后的坐标                             
		//**********************************************************************
		private void coordtransform(double[] p_old, double[] p_new)
		{
			p_new[0] = CoordTransMatrix[0] * p_old[0] + CoordTransMatrix[1] * p_old[1] + CoordTransMatrix[2] * p_old[2] + CoordTransMatrix[3];
			p_new[1] = CoordTransMatrix[4] * p_old[0] + CoordTransMatrix[5] * p_old[1] + CoordTransMatrix[6] * p_old[2] + CoordTransMatrix[7];
			p_new[2] = CoordTransMatrix[8] * p_old[0] + CoordTransMatrix[9] * p_old[1] + CoordTransMatrix[10] * p_old[2] + CoordTransMatrix[11];
			this.mathReturnParam.setP_new(p_new);
		}
		
	}
