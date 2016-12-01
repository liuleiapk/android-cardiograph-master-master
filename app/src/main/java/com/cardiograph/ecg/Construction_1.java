package com.cardiograph.ecg;
public class Construction_1
{
	public static double[] a2_rec = new double[256];
	//private int s_len = 256;
	private static double[] c_orign = new double[256];
	private static int firstA = 0;
	private static int firstD = 1;
	
	public static double[] cons_1(double[] a2,double[] w2)
	{

	    int[] idxA = new int[128];
		for(int i = 0; i < 128; i++)
		{
			idxA[i] = 2*i+firstA;
		}
		
		int[] idxD=new int[128];
		for(int i = 0; i < 128; i++)
		{
			idxD[i] = 2*i+firstD;
		}
		
		for(int i = 0 ; i < 128 ; i++)
		{
			c_orign[2*i] = a2[i];
			c_orign[2*i+1]=w2[i];
		}
		
		for(int i=0;i<128;i++)
		{
		    c_orign[idxA[i]] = c_orign[idxA[i]]/1.5707000714496564;
		    c_orign[idxD[i]] = c_orign[idxD[i]]/0.6366587855802818;
		}
		
		double[] t9=new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			t9[i]=1.0492551980492930*c_orign[idxA[i]];
		}
		for(int i = 127 ; i >= 0 ; i-- )
		{
			double[] t9_1 = t9;
			if(i==127)
			{
				t9[i]=0;
			}
			else
			{
				t9[i]=t9_1[i+1];
			}
		}
		
		//s_orign[idxD] = s_orign[idxD] +t9;
		for(int i = 0 ; i < 128 ; i++)
		{
			c_orign[idxD[i]]=c_orign[idxD[i]]+t9[i];
		}
		//p
		//double[] t6 = (-0.4312834159749964)*s_orign[idxD];//写成循环
		double[] t6 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			t6[i]=(-0.4312834159749964)*c_orign[idxD[i]];
		}
		//double[] t7 = (-0.1459830772565225)*s_orign[idxD];
		double[] t7 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			t7[i]=(-0.1459830772565225)*c_orign[idxD[i]];
		}
		double[] t8 = new double[128];
		for(int i = 0 ; i < t7.length-1 ; i++)
		{
			t8[i+1]=t7[i];
			t8[0]=0;
		}
		//double[] y3=t6 + t8;
		double[] y3 = new double [128];
		for(int i = 0 ; i < 128 ; i++)
		{
			y3[i]=t6[i]+t8[i];
		}
		//s_orign[idxA] = s_orign[idxA]+ y3;
		for(int i = 0 ; i < 128 ; i++)
		{
			c_orign[idxA[i]] = c_orign[idxA[i]] + y3[i];
		}
		//d
		//double[] t3= (1.4195148522334731)*s_orign[idxA];
		double[] t3 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			t3[i] = (1.4195148522334731)*c_orign[idxA[i]];
		}
		//double[] t4= (-0.1620314520393038)*s_orign[idxA];
		double[] t4 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			t4[i] = (-0.1620314520393038)*c_orign[idxA[i]];
		}
		double[] t5 = new double[128];
		for(int i = 0 ; i < t4.length-1 ; i++)
		{
			t5[i+1]=t4[i];
			t5[0]=0;
		}
		//double[] y2=t3 + t5;
		double[] y2 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			y2[i] = t3[i] + t5[i];
		}
		//s_orign[idxD] = s_orign[idxD]+ y2;
		for(int i = 0 ; i < 128 ; i++)
		{
			c_orign[idxD[i]] = c_orign[idxD[i]] + y2[i];
		}
		//p
		//double[] t1=(0.1243902829333865)*s_orign[idxD];
		double[] t1 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			t1[i] = (0.1243902829333865)*c_orign[idxD[i]];
		}
		
		for(int i = 127 ; i >= 0 ; i-- )
		{
			double[] t1_1 = t1;
			if(i==127)
			{
				t1[i]=0;
			}
			else
			{
				t1[i]=t1_1[i+1];
			}
		}
		//double[] t2=(0.3392439918649451)*s_orign[idxD];
		double[] t2 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			t2[i] = (0.3392439918649451)*c_orign[idxD[i]];
		}
		//double[] y1=t1 + t2;
		double[] y1 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			 y1[i]=t1[i] + t2[i];
		}
		//s_orign[idxA] = s_orign[idxA] + y1;
		for(int i = 0 ; i < 128 ; i++)
		{
			c_orign[idxA[i]] = c_orign[idxA[i]] + y1[i];
		}
		//d
		//double[] t0 = (-0.3911469419700402)*s_orign[idxA];
		double[] t0 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			t0[i] = (-0.3911469419700402)*c_orign[idxA[i]];
		}
		double[] y0 = new double[128];
		for(int i = 0 ; i < 128 ; i++)
		{
			y0[i] = t0[i];
		}
		
		for(int i = 0 ; i < 128 ; i++)
		{
			c_orign[idxD[i]] = c_orign[idxD[i]] + y0[i];
		}
		
		//最后一步
	    //a2_rec = s_orign;
		for(int i = 0 ; i < 256 ; i++)
		{
			a2_rec[i] = c_orign[i];
		}
		return a2_rec;
	}
}