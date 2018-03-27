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
 * <p>2016年7月8日     姚家俊       v1.0.0        create</p>
 *
 *
 */
//**********************************************************************
//LU Decomposition.
//
//For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n
//unit lower triangular matrix L, an n-by-n upper triangular matrix U,
//and a permutation vector piv of length m so that A(piv,:) = L*U.
//If m < n, then L is m-by-m and U is m-by-n.
//
//The LU decompostion with pivoting always exists, even if the matrix is
//singular, so the constructor will never fail.  The primary use of the
//LU decomposition is in the solution of square systems of simultaneous
//linear equations.  This will fail if isNonsingular() returns false.
//                                                                    
//**********************************************************************
public class CLU_Decomposition
{
	private double[] LU_;
	private int m, n, pivsign;
	private int[] piv;
	private Math_ReturnParam mathReturnParam;

	public CLU_Decomposition()
	{
		mathReturnParam=new Math_ReturnParam();
	}

	//**********************************************************************
//   LU Decomposition
//@param  A   Rectangular matrix
//@return     LU Decomposition object to access L, U and piv.				
	// 构造函数                                                            
	//**********************************************************************
	public CLU_Decomposition(double[] A, int m, int n)
	{
		this.m = m;
		this.n = n;
		LU_ = new double[m * n];
		piv = new int[m];

		// Use a "left-looking", dot-product, Crout/Doolittle algorithm.

		int i, j, k;
		for (i = 0; i < m; i++)
		{
			piv[i] = i;
		}
		for (i = 0; i < m * n; i++)
		{
			LU_[i] = A[i];
		}

		pivsign = 1;
		double[] LUrowi = new double[m];
		double[] LUcolj = new double[m];

		// Outer loop.	
		for (j = 0; j < n; j++)
		{
			// Make a copy of the j-th column to localize references.	
			for (i = 0; i < m; i++)
			{
				LUcolj[i] = LU_[i * n + j];
			}

			// Apply previous transformations.

			for (i = 0; i < m; i++)
			{
				LUrowi[i] = (LU_[i * n]);
				// Most of the time is spent in the following dot product.

				int kmax = i > j ? j : i;
				double s = 0.0;
				for (k = 0; k < kmax; k++)
				{
					s += LUrowi[k] * LUcolj[k];
				}

				LUrowi[j] = LUcolj[i] -= s;
			}

			// Find pivot and exchange if necessary.

			int p = j;
			for (i = j + 1; i < m; i++)
			{
				if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p]))
				{
					p = i;
				}
			}
			if (p != j)
			{
				k = 0;
				for (k = 0; k < n; k++)
				{
					double t = LU_[p * n + k];
					LU_[p * n + k] = LU_[j * n + k];
					LU_[j * n + k] = t;
				}
				k = piv[p];
				piv[p] = piv[j];
				piv[j] = k;
				pivsign = -pivsign;
			}

			// Compute multipliers.

			if ((j < m) && (LU_[j * n + j] != 0.0))
			{
				for (i = j + 1; i < m; i++)
				{
					LU_[i * n + j] /= LU_[j * n + j];
				}
			}

		}
	}

	/**
	 * 多维的矩阵转置
	 */
	public final void permute_copy(double[] A, int m, int n, int[] piv, int piv_length, int j0, int j1,double[] x)
	{
		int col = j1 - j0 + 1;
		for (int i = 0; i < piv_length; i++)
		{
			for (int j = j0; j < j1 + 1; j++)
			{
				x[i * col + (j - j0)] = A[piv[i] * n + j];
			}
		}
		this.mathReturnParam.setPermute(x);
	}
	
	/**
	 * 多维的矩阵转置
	 */
	private void permute_copy(double[] A, int m, int[] piv, int piv_length,double[]x)
	{
		if (m != piv_length)
		{
			return;
		}

		for (int i = 0; i < piv_length; i++)
		{
			x[i] = A[piv[i]];
		}
		this.mathReturnParam.setPermute(x);
	}


	//**********************************************************************
//  Is the matrix nonsingular?
//@return     1 (true)  if upper triangular factor U (and hence A) 
//            is nonsingular, 0 otherwise.
//                                                                        
	//**********************************************************************
	public final boolean isNonsingular()
	{
		for (int j = 0; j < n; j++)
		{
			if (LU_[j * n + j] == 0)
			{
				return false;
			}
		}
		return true;
	}

	//**********************************************************************
//	Return lower triangular factor(下三角因子)
//    @return     L
//                                                                        
	//**********************************************************************
	private void getL()
	{
		double[] L=new double[n];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i > j)
				{
					L[i * n + j] = LU_[i * n + j];

				}
				else if (i == j)
				{
					L[i * n + j] = 1.0;
				}
				else
				{
					L[i * n + j] = 0.0;
				}
			}
		}
		this.mathReturnParam.setLowerTriangularFactor(L);
	}

	//**********************************************************************
// Return upper triangular factor(上三角因子)
//   @return     U portion of LU factorization.                           
	//**********************************************************************
	private void getU()
	{
		double[] U=new double[n];
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i < j + 1)
				{
					U[i * n + j] = LU_[i * n + j];
				}
				else
				{
					U[i * n + j] = 0.0;
				}
			}
		}
		this.mathReturnParam.setUpperTriangularFactor(U);
	}

	//**********************************************************************
	// Return pivot permutation vector  （主元，每行的非零第一个元素 ）                                    
	//**********************************************************************
	private void getPivot()
	{
		int[] pivot =new int[m];
		for (int i = 0; i < m; i++)
		{
			pivot[i] = piv[i];
		}
		this.mathReturnParam.setPivot(pivot);
	}

	//**********************************************************************
//   Compute determinant using LU factors.
//     @return     determinant of A, or 0 if A is not square.             
	//**********************************************************************
	private double det()
	{
		if (m != n)
		{
			return 0.0;
		}

		double d = (pivsign);
		for (int j = 0; j < n; j++)
		{
			d *= LU_[j * n + j];
		}

		return d;
	}

	//**********************************************************************
// if nx = 1, then b is a vector. Solve A*x = b, where x and b are 
//    vectors of length equal	to the number of rows in A.
//  @param  b   a vector (Array1D> of length equal to the first dimension
//              of A.
//  @return x   a vector (Array1D> so that L*U*x = b(piv), if B is 
//              nonconformant, returns 0x0 (null) array.                  
//
//  if nx > 1, then b is a matrix.  Solve A*X = B
//  @param  B   A Matrix with as many rows as A and any number of columns.
//  @return     X so that L*U*X = B(piv,:), if B is nonconformant, returns
//                0x0 (null) array.
///***********************************************************************
	public final void solve(double[] b, int nx,double[]x)
	{
		int k, i;
		if (nx == 1)
		{
			// Dimensions: A is m*n, X is n*nx, B is m*nx 
			if (!isNonsingular())
			{
				return;
			}

			permute_copy(b, m, piv, m, x);

			// Solve L*Y = B(piv)
			for (k = 0; k < n; k++)
			{
				for (i = k + 1; i < n; i++)
				{
					x[i] -= x[k] * LU_[i * n + k];
				}
			}

			// Solve U*X = Y;
			for (k = n - 1; k > -1; k--)
			{
				x[k] /= LU_[k * n + k];
				for (i = 0; i < k; i++)
				{
					x[i] -= x[k] * LU_[i * n + k];
				}
			}

		}
		else if (nx > 1)
		{
			// Dimensions: A is m*n, X is n*nx, B is m*nx 
			if (!isNonsingular())
			{
				return;
			}

			// Copy right hand side with pivoting
			permute_copy(b, m, nx, piv, m, 0, nx - 1, x);

			// Solve L*Y = B(piv,:)
			int j;
			for (k = 0; k < n; k++)
			{
				for (i = k + 1; i < n; i++)
				{
					for (j = 0; j < nx; j++)
					{
						x[i * nx + j] -= x[k * nx + j] * LU_[i * n + k];
					}
				}
			}
			// Solve U*X = Y;
			for (k = n - 1; k > -1; k--)
			{
				for (j = 0; j < nx; j++)
				{
					x[k * nx + j] /= LU_[k * n + k];
				}

				for (i = 0; i < k; i++)
				{
					for (j = 0; j < nx; j++)
					{
						x[i * nx + j] -= x[k * nx + j] * LU_[i * n + k];
					}
				}
			}

		}
		this.mathReturnParam.getVetor().setB(b);
		this.mathReturnParam.getVetor().setX(x);
	}
}
