package com.cardiograph.ecg;

import java.util.ArrayList;
import java.util.List;

public class C2ECGUtil
{
	private static double[] s_rec = new double[512];
	private static  List<Integer> lstData = new ArrayList<Integer>();
	public static List ECGdenoise(List lsData)
	{
		double[] s_orign= new double[512];
		for(int i = 0 ; i < lsData.size() ; i++) {
			s_orign[i] = Double.parseDouble(lsData.get(512 * i).toString());
		}

			Decomposition_1.decom_1(s_orign);//第一层分解
			Decomposition_2.decom_2(Decomposition_1.d1[0]);//第二层分解
			Construction_1.cons_1(Decomposition_2.d2[0],Decomposition_2.d2[1]);//重构
			s_rec=Construction_2.cons_2(Construction_1.a2_rec,Decomposition_1.d1[1]);//重构
			for(int j = 0 ; j < 512 ; j++)
			{
				lstData.add((int)s_rec[j]);
			}

		return lstData;
	}
}