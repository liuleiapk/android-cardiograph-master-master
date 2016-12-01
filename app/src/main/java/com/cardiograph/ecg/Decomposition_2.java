package com.cardiograph.ecg;

public class Decomposition_2 {

	public static double[] w2 = new double[256];
	public static double[] a2 = new double[256];
	public static double[][] d2=new double[2][256];
	//private int s_len = 256;
	private static int firstA = 0;
	private static int firstD = 1;
	static double[][] decom_2(double[] a1)
	{
		 int[] idxA = new int[128];
		 for(int i = 0; i < 128; i++)
		 {
			idxA[i] = 2*i+firstA;
		 }
			
		 int[] idxD = new int[128];
		 for(int j = 0; j < 128; j++)
		 {
			idxD[j] = 2*j+firstD;
		 }
		 //t0 = 0.3911469419700402*s_orign(idxA);
		 double[] t0 = new double[128];
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 t0[i] = 0.3911469419700402*a1[idxA[i]];
		 }
		 //s_orign(idxD) = s_orign(idxD) + t0;
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 a1[idxD[i]] = a1[idxD[i]] + t0[i];
		 }
		 //%%%%%%%　　　p　　%%%%%%%%%%%%%%%%%%%%
		 //t1=(-0.1243902829333865)*s_orign(idxD);
		 double[] t1 = new double[128];
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 t1[i]=(-0.1243902829333865)*a1[idxD[i]];
		 }
		 //t1(end+1) = 0; t1 = t1(1+1:end);
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
		 
		 // t2=(-0.3392439918649451)*s_orign(idxD);
		 double[] t2 = new double[128];
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 t2[i]=(-0.3392439918649451)*a1[idxD[i]];
		 }
		 // s_orign(idxA) = s_orign(idxA) + t1+ t2;
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 a1[idxA[i]] = a1[idxA[i]] + t1[i]+ t2[i];
		 }
		 // %%%%%%%%　　d　　%%%%%%%%%%%%%%%%%%%%
		 // t3= (-1.4195148522334731)*s_orign(idxA);
		 double[] t3 = new double[128];
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 t3[i] = (-1.4195148522334731)*a1[idxA[i]];
		 }
		 // t4= 0.1620314520393038*s_orign(idxA);
		 double[] t4 = new double[128];
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 t4[i]= 0.1620314520393038*a1[idxA[i]];
		 }
		 // t5 = [0;t4(1:length(t4)-1)];
		 double[] t5 = new double[128];
		 for(int i = 0 ; i < t4.length-1 ; i++)
		 {
			 t5[i+1]=t4[i];
			 t5[0]=0;
		 }		 
		 //s_orign(idxD) = s_orign(idxD)+ t3 + t5;
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 a1[idxD[i]] = a1[idxD[i]]+ t3[i] + t5[i];
		 }
		 //%%%%%%%%%　　p　　%%%%%%%%%%%%%%%%%%%%
		 //t6= 0.4312834159749964*s_orign(idxD);
		 double[] t6 = new double[128];
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 t6[i] = 0.4312834159749964*a1[idxD[i]];
		 }
		 //t7= 0.1459830772565225*s_orign(idxD);
		 double[] t7 = new double[128];
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 t7[i] = 0.1459830772565225*a1[idxD[i]];
		 }
		 //t8 = [0;t7(1:length(t7)-1)];
		 double[] t8 = new double[128];
		 for(int i = 0 ; i < t7.length-1 ; i++)
		 {
			 t8[i+1]=t7[i];
			 t8[0]=0;
		 }
		 //s_orign(idxA) = s_orign(idxA)+ t6 + t8;
		 for(int  i = 0 ; i < 128 ; i++)
		 {
			 a1[idxA[i]] = a1[idxA[i]]+ t6[i] + t8[i];
		 }
		 //%%%%%%%%%　　d　　　%%%%%%%%%%%%%%%%%%%%
		 //t9=(-1.0492551980492930)* s_orign(idxA);
		 double[] t9 = new double[128];
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 t9[i] = (-1.0492551980492930)* a1[idxA[i]];
		 }
		 //t9(end+1) = 0; t9 = t9(1+1:end);
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
		 //s_orign(idxD) = s_orign(idxD) +t9;
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 a1[idxD[i]] = a1[idxD[i]]+t9[i];
		 }
		 //%%%%%%%%%%　　　最后一步
		 //s_orign(idxA) = 1.5707000714496564*s_orign(idxA);
		 //s_orign(idxD) = 0.6366587855802818*s_orign(idxD);
		 for(int i=0;i<128;i++)
		 {
			 a1[idxA[i]] = a1[idxA[i]]*1.5707000714496564;
			 a1[idxD[i]] = a1[idxD[i]]*0.6366587855802818;
		 }

		 //a2 = s_orign(idxA);  
		 //w2 = s_orign(idxD);
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 a2[i] = a1[idxA[i]];  
			 w2[i] = a1[idxD[i]];
		 }
		 for(int i = 0 ; i < 128 ; i++)
		 {
			 d2[0][i]=a2[i];
			 d2[1][i]=w2[i];
		 }
		 return d2;
	}
}
