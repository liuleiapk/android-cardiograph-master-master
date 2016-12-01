package com.cardiograph.ecg;
public class Construction_2
{
	private static double[] s_rec = new double[512];
	//private int s_len = 512;
	private static double[] c2_orign = new double[512];
	private static int firstA = 0;
	private static int firstD = 1;
	public static double[] cons_2(double[] a2_rec,double[] w1)
	{
		int[] idxA = new int[256];
		for(int i = 0; i < 256; i++)
		{
			idxA[i] = 2*i+firstA;
		}
		
		int[] idxD=new int[256];
		for(int i = 0; i < 256; i++)
		{
			idxD[i] = 2*i+firstD;
		}
		
		for(int i = 0 ; i < 256 ; i++)
		{
			c2_orign[2*i] = a2_rec[i];
			c2_orign[2*i+1]=w1[i];
		}
		
		for(int i = 0 ; i < 256 ; i++)
		{
		    c2_orign[idxA[i]] = c2_orign[idxA[i]]/1.5707000714496564;
		    c2_orign[idxD[i]] = c2_orign[idxD[i]]/0.6366587855802818;
		}
		//t9=(1.0492551980492930)* s_orign(idxA);
		// t9(end+1) = 0; t9 = t9(1+1:end);
		double[] t9=new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			t9[i]=1.0492551980492930*c2_orign[idxA[i]];
		}
		for(int i = 255 ; i >= 0 ; i-- )
		{
			double[] t9_1 = t9;
			if(i==255)
			{
				t9[i]=0;
			}
			else
			{
				t9[i]=t9_1[i+1];
			}
		}
		// s_orign(idxD) = s_orign(idxD) +t9;
		for(int i = 0 ; i < 256 ; i++)
		{
			c2_orign[idxD[i]]=c2_orign[idxD[i]]+t9[i];
		}
		//  %p
		// t6= (-0.4312834159749964)*s_orign(idxD);
		double[] t6 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			t6[i]=(-0.4312834159749964)*c2_orign[idxD[i]];
		}
		// t7= (-0.1459830772565225)*s_orign(idxD);
		double[] t7 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			t7[i]=(-0.1459830772565225)*c2_orign[idxD[i]];
		}
		//t8 = [0;t7(1:length(t7)-1)];
		double[] t8 = new double[256];
		for(int i = 0 ; i < t7.length-1 ; i++)
		{
			t8[i+1]=t7[i];
			t8[0]=0;
		}
		//  y3=t6 + t8;
		double[] y3 = new double [256];
		for(int i = 0 ; i < 256 ; i++)
		{
			y3[i]=t6[i]+t8[i];
		}
		// s_orign(idxA) = s_orign(idxA)+ y3;
		for(int i = 0 ; i < 256 ; i++)
		{
			c2_orign[idxA[i]] = c2_orign[idxA[i]] + y3[i];
		}
		//  %d
		// t3= (1.4195148522334731)*s_orign(idxA);
		double[] t3 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			t3[i] = (1.4195148522334731)*c2_orign[idxA[i]];
		}
		// t4= (-0.1620314520393038)*s_orign(idxA);
		double[] t4 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			t4[i] = (-0.1620314520393038)*c2_orign[idxA[i]];
		}
		// t5 = [0;t4(1:length(t4)-1)];
		double[] t5 = new double[256];
		for(int i = 0 ; i < t4.length-1 ; i++)
		{
			t5[i+1]=t4[i];
			t5[0]=0;
		}
		// y2=t3 + t5; 
		double[] y2 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			y2[i] = t3[i] + t5[i];
		}
		// s_orign(idxD) = s_orign(idxD)+ y2;
		for(int i = 0 ; i < 256 ; i++)
		{
			c2_orign[idxD[i]] = c2_orign[idxD[i]] + y2[i];
		}
		// %p
		// t1=(0.1243902829333865)*s_orign(idxD);
		//   t1(end+1) = 0; t1 = t1(1+1:end);
		double[] t1 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			t1[i] = (0.1243902829333865)*c2_orign[idxD[i]];
		}
		
		for(int i = 255 ; i >= 0 ; i-- )
		{
			double[] t1_1 = t1;
			if(i==255)
			{
				t1[i]=0;
			}
			else
			{
				t1[i]=t1_1[i+1];
			}
		}
		// t2=(0.3392439918649451)*s_orign(idxD);
		double[] t2 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			t2[i] = (0.3392439918649451)*c2_orign[idxD[i]];
		}
		// y1=t1 + t2;
		double[] y1 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			 y1[i]=t1[i] + t2[i];
		}
		// s_orign(idxA) = s_orign(idxA) + y1;
		for(int i = 0 ; i < 256 ; i++)
		{
			c2_orign[idxA[i]] = c2_orign[idxA[i]] + y1[i];
		}
		// %d
		//t0 = (-0.3911469419700402)*s_orign(idxA);
		double[] t0 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			t0[i] = (-0.3911469419700402)*c2_orign[idxA[i]];
		}
		//y0=t0;
		double[] y0 = new double[256];
		for(int i = 0 ; i < 256 ; i++)
		{
			y0[i] = t0[i];
		}
		//s_orign(idxD) = s_orign(idxD) + y0;
		for(int i = 0 ; i < 256 ; i++)
		{
			c2_orign[idxD[i]] = c2_orign[idxD[i]] + y0[i];
		}
		// %最后一步
		// s_rec = s_orign;
		for(int i = 0 ; i < 512 ; i++)
		{
			s_rec[i] = c2_orign[i];
		}
		return s_rec;
	}
}