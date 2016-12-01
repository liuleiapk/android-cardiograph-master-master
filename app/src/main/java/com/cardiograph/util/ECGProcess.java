package com.cardiograph.util;

import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileReader;  
import java.io.IOException;
import java.io.DataInputStream;
import java.lang.Math;


public class ECGProcess
{
	static int noSig;			//信号通道数目
	static int sFreq;			//数据采样频率
	static int SAMPLES2READ;	//指定需要读入的样本数; 若.dat文件中存储有两个通道的信号, 则读入 2*SAMPLES2READ 个数据 
	static int refTime;			//atr文件中，代码28给出的参考时间；
	static int[] dformat; 		//信号格式; 这里只允许为 212 格式
	static int[] gain;			//每 mV 包含的整数个数
	static int[] bitres;		//采样精度（位分辨率）
	static int[] zerovalue; 	//ECG 信号零点相应的整数值
	static int[] firstvalue; 	//信号的第一个整数值 (用于偏差测试)
	static double[] M;			//由dat文件读出的信号
	static double[] ATRTIME;
	static double[] Y;			//四阶小波变换后得出的信号
	static double[] Yabs;		//四阶小波变换后得出的绝对值信号
	static double[] Z;			//十阶小波变换后得出的信号
	
	static int[] rPosition;		//R波的位置
	static int[] rDirection;	//R波的方向
	static double[] rValue;		//R波的值
	static int nRPosition;		//R波的数量
	static double[] rrInterval;
	static double[] rrIntervalMean; 
	
	static int[] qPosition;		//Q波的位置
	static double[] qValue;		//Q波的值
	static int nQPosition;		//Q波的数量
	static int[] sPosition;		//S波的位置
	static double[] sValue;		//S波的值
	static int nSPosition;		//S波的数量
	
	static int[] pPosition;		//P波的位置
	static double[] pValue;		//P波的值
	static int nPPosition;		//P波的数量
	static int[] tPosition;		//T波的位置
	static double[] tValue;		//T波的值
	static int nTPosition;		//T波的数量
	
	static double[] baselineValue;
	static int[] baselineStartPosition;
	static int[] baselineEndPosition;
	static int lenBaseline;
	
	static int[] qrsOnPosition;
	static int[] qrsOffPosition;
	static int[] qrsDuration;
	static int lenQRS; 
	
	public static void initial(int int1, int int2){
		noSig = int1;
		sFreq = int2;
		dformat = new int[int1];
		gain = new int[int1];
		bitres = new int[int1];
		zerovalue = new int[int1];
		firstvalue = new int[int1];
		M = new double[SAMPLES2READ*2]; 
		ATRTIME = new double[SAMPLES2READ];
		Y = new double[SAMPLES2READ];
		Yabs = new double[SAMPLES2READ];
		Z = new double[SAMPLES2READ];
		rPosition = new int[3*SAMPLES2READ/sFreq];
		rDirection = new int[3*SAMPLES2READ/sFreq];
		rValue = new double[3*SAMPLES2READ/sFreq];
		qPosition = new int[3*SAMPLES2READ/sFreq];
		qValue = new double[3*SAMPLES2READ/sFreq];
		sPosition = new int[3*SAMPLES2READ/sFreq];
		sValue = new double[3*SAMPLES2READ/sFreq];
		pPosition = new int[3*SAMPLES2READ/sFreq];
		pValue = new double[3*SAMPLES2READ/sFreq];
		tPosition = new int[3*SAMPLES2READ/sFreq];
		tValue = new double[3*SAMPLES2READ/sFreq];
		baselineValue = new double[3*SAMPLES2READ/sFreq];
		baselineStartPosition = new int[3*SAMPLES2READ/sFreq];
		baselineEndPosition = new int[3*SAMPLES2READ/sFreq];
		qrsOnPosition = new int[3*SAMPLES2READ/sFreq];
		qrsOffPosition = new int[3*SAMPLES2READ/sFreq];
		qrsDuration = new int[3*SAMPLES2READ/sFreq];
	}
	
//	%% ------ LOAD HEADER DATA ---------------------------------------
//	%
	public static void readHeadFile(String fileName)
	{  
		File file = new File(fileName);  
		BufferedReader reader = null;  
		try {  
			System.out.println("以行为单位读取文件内容，一次读一整行：");  
			reader = new BufferedReader(new FileReader(file));  
			String tempString = null;  
			int line = 1; //一次读入一行，直到读入null为文件结束  
			while ((tempString = reader.readLine()) != null){  //显示行号 
				//System.out.println("line " + line + ": " + tempString); 
				if(line == 1){
					String[] tokens = tempString.split(" ");
					initial(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
					//System.out.println(noSig  + "," + sFreq);
				}
				else if((line - noSig)< 2){
					String[] tokens = tempString.split(" ");
					dformat[line-2] = Integer.parseInt(tokens[1]);
					gain[line-2] = Integer.parseInt(tokens[2]);
					bitres[line-2] = Integer.parseInt(tokens[3]);
					zerovalue[line-2] = Integer.parseInt(tokens[4]);
					firstvalue[line-2]= Integer.parseInt(tokens[5]);
					//System.out.println(dformat[line-2] + "," + gain[line-2] + "," + bitres[line-2]+ "," + zerovalue[line-2]+ "," + firstvalue[line-2]);
				}
				line++;  
			}  
			reader.close();  
		} 
		catch (IOException e) {  
			e.printStackTrace();  
		} 
		finally {  
			if (reader != null){  
				try {  
					reader.close();  
				} 
				catch (IOException e1) {  
				}  
			}  
		}  
	}
	
//	%% ------ LOAD BINARY DATA ---------------------------------------
//	%
//	% 最终结果保存在M矩阵中，直接调用M即可使用
//	%
	public static void readDataFile(String fileName)
	{
		int M1H,M2H,PRL,PRR;
		int Mi[] = new int[SAMPLES2READ*3];  //若.dat文件中存储有两个通道的信号, 则读入 2*SAMPLES2READ 个数据
		double TIME[] = new double[SAMPLES2READ];
		int intTmp1, intTmp2, intTmp3;
		if(dformat[1] != 212){
			System.out.println("this script does not apply binary formats different to 212."); 
		}
		File file = new File(fileName);  
		DataInputStream reader = null;  
		int line = 0; //一次读入一行，直到读入null为文件结束  
		try {  
			System.out.println("开始读data文件，uint8格式");  
			reader = new DataInputStream(new FileInputStream(file));  
			while (line < 3*SAMPLES2READ){
				intTmp1 = reader.readUnsignedShort();
				intTmp2 = reader.readUnsignedShort();
				intTmp3 = reader.readUnsignedShort();
				Mi[line]   = intTmp1/256;
				Mi[line+1] = intTmp1%256;
				Mi[line+2] = intTmp2/256;
				Mi[line+3] = intTmp2%256;
				Mi[line+4] = intTmp3/256;
				Mi[line+5] = intTmp3%256;
				//System.out.println(intTmp1 + ", " + intTmp2 + ", " + intTmp3);
				//for(int j=0;j<6;j++){	
				//	System.out.println((line+j) + ", " + M[line+j]);
				//}
				line+=6;
			} 
			reader.close();
		} 
		catch (IOException e) {  
			e.printStackTrace();  
		}
		finally {  
			if (reader != null){  
				try {  
					reader.close();  
				} 
				catch (IOException e1) {  
				}  
			}  
		}
		for(int j=0;j<SAMPLES2READ;j++){
			M2H = Mi[3*j+1]/16;      	//字节向右移四位，即取字节的高四位
			M1H = Mi[3*j+1]%16;       	//取字节的低四位
			PRL = (Mi[3*j+1]&8)*512;    //sign-bit   取出字节低四位中最高位，向右移九位
			PRR = (Mi[3*j+1]&128)*32;  	//sign-bit   取出字节高四位中最高位，向右移五位
			Mi[2*j] = (M1H*256)+ Mi[3*j] -PRL;
			Mi[2*j+1] = (M2H*256)+ Mi[3*j+2] -PRR;
			if((Mi[0]!=firstvalue[0])||(Mi[1]!=firstvalue[1])){
				System.out.println("Error: inconsistency in the first bit values"); 
			}
			//System.out.println(j + ", " + M[3*j+1]  + ", " + M2H + ", " + firstvalue[1]  + ", " + M1H);
			//System.out.println(j + ", " + M[3*j+1]  + ", " +M[2*j] + ", " +M[2*j+1]);			
		}
		for(int j=0;j<SAMPLES2READ;j++){
			switch(noSig){
				case 2: 
					M[2*j] = (double)(Mi[2*j]- zerovalue[0])/gain[0];
					M[2*j+1]= (double)(Mi[2*j+1]- zerovalue[1])/gain[1];
					TIME[j] = j/sFreq;
					//System.out.println(j + ", " +M[2*j] + ", " +M[2*j+1] + ", " +TIME[j]);	
					break;
			    case 1:
			        M[2*j] = (double)Mi[2*j]- zerovalue[0];
			        M[2*j+1]= (double)Mi[2*j+1]- zerovalue[0];
			        M[2*j] = (double)Mi[2*j]/gain[0];
			        M[2*j+1] = (double)Mi[2*j+1]/gain[0];
			        TIME[j] = j/sFreq;
			        break;
			    default:  // this case did not appear up to now!
			              // here M has to be sorted!!!
			        System.out.println("Sorting algorithm for more than 2 signals not programmed yet!");
			        break;
			}
			//System.out.println(j + ", " +M[2*j] + ", " +M[2*j+1] + ", " +TIME[j]);	
		}
	}
	
//	%% ------ LOAD ATTRIBUTES DATA -----------------------------------
//	%
//	% 最终的注释保存在ANNOTD中，时间信息保存在ATRTIMED中，直接调用即可使用；
//	% 其中注释的数字对应的信息保存在ecgcodes.h文件中，查看即可。
//	%
	public static void readAtrFile(String fileName)
	{
		int MM[] = new int[SAMPLES2READ];
		File file = new File(fileName);  
		DataInputStream reader = null; 
		int annoth, hilfe, temp, line = 0; //一次读入一行，直到读入null为文件结束 
		int iAtr = 0;//ATRTIME专用
		try {  
			System.out.println("开始读atr文件，uint16格式");  
			reader = new DataInputStream(new FileInputStream(file));  
			while (line < SAMPLES2READ){
				MM[line] = reader.readUnsignedShort();
				annoth = (MM[line]%256)/4;//高六位
				if(annoth == 28){//节律变换
				    refTime = (MM[line]%4)*256 + MM[line]/256;
				}
				else if(annoth == 59){//
					//ANNOT = [ANNOT;bitshift(A(i+3,2),-2)];
				    //ATRTIME = [ATRTIME;A(i+2,1)+bitshift(A(i+2,2),8)+ bitshift(A(i+1,1),16)+bitshift(A(i+1,2),24)];
					//System.out.println(line + ", " + M[line] + ", " + annoth);
					iAtr++;
				}
				else if(annoth == 60){}//nothing to do!
				else if(annoth == 61){}// nothing to do!
				else if(annoth == 62){}// nothing to do!
				else if(annoth == 63){//辅助信息，求辅助信息长度
					hilfe = (MM[line]%4)*256 + MM[line]/256;//低十位，字符长;
				    hilfe = hilfe/2 + hilfe%2;
				    //System.out.println(line + ", " + M[line] + ", " + annoth + ",   hilfe  = " + hilfe);
				    while(hilfe>0){
				    	temp = reader.readUnsignedShort();
				    	//System.out.println(temp);
				    	hilfe--;
				    }
				}
				else{//时间信息
				    ATRTIME[iAtr] = (double)((MM[line]%4)*256 + MM[line]/256 + refTime)/sFreq;//低十位，时间信息;
				    //System.out.println(line + ", " + M[line] + ", " + annoth + ", " + refTime + ", " + ATRTIME[iAtr] );
				    iAtr++;
				}
				line++;
			} 
			reader.close();
		} 
		catch (IOException e) {  
			e.printStackTrace();  
		}
		finally {  
			if (reader != null){  
				try{  
					reader.close();  
				} 
				catch (IOException e1) {  
				}  
			}  
		}
	}	

//	%% ------ CWT  -----------------------------------
	public static void cwt(int delta){
		int N = delta*20;
		int n = M.length/2;
		int SS, EE;
		double x, phi_x_sum=0.0;
		double phi_x[] = new double[N];
		for(int i=0; i<N; i++){
			x = i-0.5*(N-1);
			phi_x[i]=(Math.pow(Math.PI,-0.25)*2.0/Math.sqrt(3.0))*(1.0 - x*x/(delta*delta))*Math.exp(-(x*x)/(2*delta*delta));
			phi_x_sum += phi_x[i];
		}

		phi_x_sum = Math.sqrt(Math.abs(phi_x_sum));//能量归一化
		for(int i=0; i<N; i++)
			phi_x[i] /= phi_x_sum;

		 //卷积,且保持信号长度
		if(delta == 4){
			for(int j=0;j<n;j++){	
				Y[j] = 0.0;
				if(j<N/2)
					EE = N/2+j ;
				else
					EE = N;	
				if(j >= n-N/2-1)
					SS = N/2-(n-j-1);
				else
					SS = 0;
				for(int k=SS;k<EE;k++)
					Y[j] += M[2*(j-k)+N+1]*phi_x[k];
				//System.out.println(j +"y, "+ Y[j]);
				Yabs[j] = Math.abs(Y[j]);
			}
			for(int j=0;j<delta*2;j++){	
				Y[j] = 0.0;
				Y[n-j-1] = 0.0;
			}
		}
		else if(delta == 10){
			for(int j=0;j<n;j++){	
				Z[j] = 0.0;
				if(j<N/2)
					EE = N/2+j ;
				else
					EE = N;	
				if(j >= n-N/2-1)
					SS = N/2-(n-j-1);
				else
					SS = 0;
				for(int k=SS;k<EE;k++)
					Z[j] += M[2*(j-k)+N+1]*phi_x[k];
				//System.out.println(j +"z, "+ Z[j]);
			}
			for(int j=0;j<delta*2;j++){	
				Z[j] = 0.0;
				Z[n-j-1] = 0.0;
			}
		}
		else{
			//do nothing!!
		}
	}
//	%% ------Ceil-----------------------------------
	public static int ceil(double dIn){
		int out = (int)dIn;
		if(((int)dIn*10.0)%10 != 0)
			out++;
		return out;

	}
	
//	%% ------ R PEAK DETECTION ---------------------------------------
//	%
//	% 比较简单的一种处理算法，利用小波变换后某尺度进行峰值检测
//	% 结果保存在3个数组中
//	% rPosition保存R峰的坐标信息
//	% r_value保存R峰的峰值
//	% r_direction保存R峰的方向，1为向上，0为向下。
//	% 由于R波一定是向上的，因此如果出现方向向下，则一定为错检。
//	% len_r保存R波的个数
//	%
	public static void rPeakDet(String[] args){
		int ii;
		double temp;
		double[] tranY = new double[SAMPLES2READ];
		double[] sigMax = new double[SAMPLES2READ*2];//包含sigMax的位置与数值
		int nSigMax = 0;
		ECGProcess.cwt(4);

		//% 峰值检测
		System.arraycopy(Y, 0, tranY, 0, Y.length); 
		for(int i=0; i<M.length/2-2; i++){     
		    if(((Y[i+1]>Y[i])&&(Y[i+1]>Y[i+2]))||((Y[i+1]<Y[i])&&(Y[i+1]<Y[i+2]))){          
		        sigMax[nSigMax*2+1] = Math.abs(tranY[i+1]);
		        sigMax[nSigMax*2] = i+1;
		        nSigMax++;  
		    }
		}    
		
		//% 取阈值,阈值为相对幅值的差的30%    
		for(int i=0;i<nSigMax;i++){  //对sigMax冒泡排序
	        for(int j=i+1;j<nSigMax;j++){  
	        	if(sigMax[2*i+1]>sigMax[2*j+1]){  
	        		temp=sigMax[2*i+1];  
	        		sigMax[2*i+1]=sigMax[2*j+1];  
	        		sigMax[2*j+1]=temp;
	        		temp=sigMax[2*i];  
	        		sigMax[2*i]=sigMax[2*j];  
	        		sigMax[2*j]=temp; 
	        	}
	        }  
	    }  
		double thr = 0, zerovalue = 0;
		for (int i=0;i<8;i++){     
		    thr += sigMax[2*(nSigMax-i)-1]; 
		} 
		
		for(int i=0;i<tranY.length;i++){  //对tranY冒泡排序
	        for(int j=i+1;j<tranY.length;j++){  
	        	if(tranY[i]>tranY[j]){  
	        		temp=tranY[i];  
	        		tranY[i]=tranY[j];  
	        		tranY[j]=temp; 
	        	}
	        }  
	    }  
		for(int i=0;i<100;i++){ 	//最小幅度平均值，对消幅度，
			zerovalue += tranY[i]; 
		}      
		thr /= 8;               	//最大幅度平均值，8个最大幅值点的平均值    
		zerovalue /= 100;     		//100个最小幅值点的平均值    
		thr = (thr-zerovalue)*0.3; 	//最大、最小幅度的差值的30%为判别R波的阈值
		//System.out.println( "thr = " + thr + "nSigMax = " + nSigMax);

		//% 定位R波  
		nRPosition = 0;
		for(int i=0;i<nSigMax;i++){    
		    if(sigMax[2*i+1]>thr){          
		        rPosition[nRPosition] = (int)sigMax[2*i];
		        //System.out.println( "thr = " + thr + "nSigMax = " + nSigMax);
		        nRPosition++;
			} 
		}
		for(int i=0;i<nRPosition;i++){  //对rPosition冒泡排序
	        for(int j=i+1;j<nRPosition;j++){  
	        	if(rPosition[i]>rPosition[j]){  
	        		ii=rPosition[i];  
	        		rPosition[i]=rPosition[j];  
	        		rPosition[j]=ii; 
	        	}
	        }  
	    } 
    	//for(int j=0;j<nRPosition;j++)
    	//	System.out.println("Line" + j + ": " + rPosition[j]); 
		
		//% 排除误检，如果相邻两个极大值间距小于0.4，则去掉幅度较小的一个 
		ii = 1;  
		while(ii < nRPosition){       
		    if((rPosition[ii]-rPosition[ii-1]) < 0.4*sFreq){          
		        if(Yabs[rPosition[ii]]>Yabs[rPosition[ii-1]])               
		        	rPosition[ii-1] = rPosition[ii];           
		    	for(int j=ii;j<nRPosition-1;j++)
		    		rPosition[j] = rPosition[j+1];  
		    	rPosition[nRPosition-1]=0; 
		    	nRPosition--;
		        ii--;       
		    }      
		    ii++; 
		}
    	//for(int j=0;j<nRPosition;j++)
    	//	System.out.println("Line" + j + ": " + rPosition[j]); 

		//% 在原信号上精确校准 
    	for(int j=0;j<nRPosition;j++){
		    if(Y[rPosition[j]]>0){
		    	ii = -5;
		    	for(int i=-4;i<5;i++) //对rPosition冒泡排序
			        if(M[2*rPosition[j]+2*i+1]>M[2*rPosition[j]+2*ii+1])  
			        	ii = i;
		    	rPosition[j] += ii;
		    	rDirection[j] = 1;
		    	//System.out.println("Line" + j + ": " + rPosition[j] + ", " + ii); 
		    }
		    else{       
		    	ii = -5;
		    	for(int i=-4;i<5;i++) //对rPosition冒泡排序
			        if(M[2*rPosition[j]+2*i+1]<Y[2*rPosition[j]+2*ii+1])  
			        	ii = i;
		    	rDirection[j] = 0;
		    	System.out.println("ERROR: R波错检");
		    }
    	}

		//% 提取R峰峰值信息
    	for(int j=0;j<nRPosition;j++)
		    rValue[j] = M[2*rPosition[j]+1];
	}
	
//	%% ------ RR INTERVAL --------------------------------------------
//	%
//	% RR间期的平均值检测是根据10个相邻的RR间期值确定的
//	% rr_interval保存相邻两个RR间期的距离
//	% rrIntervalMean保存RR间期的平均值
//	% rr_interval的长度比rPosition长度少1,rrIntervalMean的长度与rPosition相等，rrIntervalMean(1) = rrIntervalMean(2) 
//	%
	public static void rrInterval(String[] args)
	{
		// 计算初始的rrIntervalMean
		int num, n;
		double rrim = 0;
		rrInterval = new double[nRPosition];
		rrIntervalMean = new double[nRPosition];
		if(nRPosition<= 10){
		    num = nRPosition/2;
		    n = nRPosition/2 + nRPosition%2;
	    	for(int j=0;j<num;j++)
	    		rrim += rPosition[nRPosition-j-1] - rPosition[j];
		    rrim /= (num*n);
		}
		else{
	    	for(int j=0;j<5;j++)
	    		rrim += rPosition[9-j] - rPosition[j];
		    rrim /= 25;
		}
		// 计算全部的rr_interval和剩余的rrIntervalMean
		for(int i=0; i<nRPosition-1; i++){
		    rrInterval[i] = rPosition[i+1] - rPosition[i];
		    if(i <= 10)
		    	rrIntervalMean[i+1] = rrim;
		    else
		    	rrIntervalMean[i+1] = rrIntervalMean[i] + (rPosition[i] + rPosition[i-10] - 2 * rPosition[i-5])/25.0;
		    //System.out.println("Line" + i + ": " + rPosition[i] + ", " + rValue[i]+ ", " + rrIntervalMean[i+1]);
		}
		rrIntervalMean[0] = rrIntervalMean[1];
		System.out.println("RR INTERVAL CALCULATION FINISHED ");
	}
	
//	%% ------ Q & S WAVE DETECTION------------------------------------
//	%
//	% 时间窗扫描算法，窗宽度50ms
//	% 结果保存在4个数组中
//	% qPosition和s_posotion保存Q和S波的波峰坐标信息
//	% qValue和sValue保存Q和S波的幅值信息
//	% len_q和len_s分别保存Q波和S波的个数
//	%
	public static void qsWaveDet(String[] args)
	{
		int qsWindowWidth = sFreq/20;       // width of the window: 50ms 
		int flagQ = 0, flagS = 0;
		int minTemp = 0;
		nQPosition = 0;
		nSPosition = 0;
		// first Q wave detection
		// 如果第一个R波小于QS窗的宽度，单独检测第一个波，否则跟其他波一起检测
		if(rPosition[0] < qsWindowWidth){
	    	for(int i=1;i<rPosition[0];i++){ //对第一个R之前冒泡排序，求最小值
		        if(M[2*i+1]<M[2*minTemp+1])  
		        	minTemp = i;
	    	}
		    if(minTemp>3){
		        if(rDirection[0] == 1){
		            if((M[2*minTemp+1]<M[2*minTemp+3])&&(M[2*minTemp+1]<M[2*minTemp-1])){
		                qPosition[0] = minTemp;
		                qValue[0] = M[2*minTemp+1];
		                flagQ= 1;     //标志检测到了第一个Q波
		                nQPosition ++;
		            }
		        }
		        else{// nothing to do!
		        }
			}
		    System.out.println("Line : " + qPosition[0] + ", " + qValue[0]);
		}
		//% last S wave detection
		//% 如果最后一个R波距离信号终端小于QS窗的宽度，单独检测最后一个波，否则跟其他波一起检测
		minTemp = rPosition[nRPosition-1];
		if((M.length/2 - rPosition[nRPosition-1]) < qsWindowWidth){
	    	for(int i=rPosition[nRPosition-1]+1;i<M.length/2;i++){ //对第一个R之前冒泡排序，求最小值
		        if(M[2*i+1]<M[2*minTemp+1])  
		        	minTemp = i;
	    	}
		    if((M.length/2 - minTemp)<4){
		        if(rDirection[0] == 1){
		            if((M[2*minTemp+1]<M[2*minTemp+3])&&(M[2*minTemp+1]<M[2*minTemp-1])){
		                sPosition[nRPosition-1] = minTemp;
		                sValue[nRPosition-1] = M[2*minTemp+1];
		                flagS= 1;     //标志检测到了最后一个Q波
		                nSPosition ++;
		            }
		        }
		        else{// nothing to do!
		        }
			}
		    System.out.println("Line : " + qPosition[1] + ", " + qValue[1]);
		}
		
		//other Q & S wave detection
		for(int i=0;i<nRPosition;i++){
		    if(!((flagQ==1)&&(i==0))){
		    	minTemp = rPosition[i]- qsWindowWidth;
		    	for(int j=rPosition[i]- qsWindowWidth+1;j<rPosition[i];j++){ //对第i个R之前冒泡排序，求最大值
			        if(M[2*j+1]<M[2*minTemp+1])  
			        	minTemp = j;
		    	}
		        qPosition[i] = minTemp;
		        qValue[i] = M[2*qPosition[i]+1];
		        nQPosition ++;
			}
		    if(!((flagS==1)&&(i==nRPosition-1))){
		    	minTemp = rPosition[i];
		    	for(int j=rPosition[i]+1;j<rPosition[i]+qsWindowWidth;j++){ //对第i个R之前冒泡排序，求最大值
			        if(M[2*j+1]<M[2*minTemp+1])  
			        	minTemp = j;
		    	}
		        sPosition[i] = minTemp;
		        sValue[i] = M[2*sPosition[i]+1];
		        nSPosition ++;
		    }
		    //System.out.println("Line" + i + ": " + qPosition[i] + ", " + qValue[i] + ", " + sPosition[i] + ", " + sValue[i]);
		}
		
		//% 如果第一个Q波不存在，则删掉第一个空位
		if ((qPosition[0] == qValue[0])&&(qValue[0] == 0)){
			for(int i=0;i<nQPosition;i++){
				qPosition[i] = qPosition[i+1];
		    	qValue[i] = qValue[i+1];
		    	nQPosition--;
			}
		}
	}	

//	%% ------ P & T DETECTION ----------------------------------------
//	%
//	% 用第十阶小波分析结果。
//	% T波检测：假定ST段时间为0.05-0.12s，因此从S波右侧的0.05s处开始检测，在0.4倍rr_interval的时间窗内检测峰值点。
//	% t_position和p_posotion保存T和P波的波峰坐标信息
//	% t_value和p_value保存T和P波的幅值信息
//	% len_t和len_p分别保存T波和P波的个数
//	% 
	public static void ptWaveDet(String[] args)
	{
		ECGProcess.cwt(10);
		int stMiniWidth = sFreq/20;        // width of the window: 50ms
		int pWindowWidth;
		int baselineWindowWidth;
		int flagP = 0, flagT = 0;
		int minTemp = 0, ii;
		nPPosition = 0;
		nTPosition = 0;
		
		// 检测第一个P波
		// 如果第一个Q波小于0.4倍的rr_interval，那么需要单独检测。否则跟其他波一起检测。
		if(qPosition[0] < 0.4*rrIntervalMean[0]){
	    	for(int i=1;i<qPosition[0];i++){ //对第一个R之前冒泡排序，求最小值
		        if(M[2*i+1]>M[2*minTemp+1])  
		        	minTemp = i;
	    	}
		    if(minTemp>3){
		    	if((M[2*minTemp+1]>M[2*minTemp+3])&&(M[2*minTemp+1]>M[2*minTemp-1])){
		            pPosition[0] = minTemp;
		            pValue[0] = M[2*minTemp+1];
		            flagP= 1;     //标志检测到了第一个Q波
		            nPPosition ++;
		        }
		        else{// nothing to do!
		        }
		    	System.out.println("Line0: " + pPosition[0] + ", " + pValue[0]); 
			}
		}
		// 检测P波和T波（第一个P波除外）
		for(int i=0;i<nRPosition;i++){
			if (i<nQPosition){    
				if((i!= 0)||(flagP!= 1)){ 
			        pWindowWidth = (int)(0.4*rrIntervalMean[i]);
			        minTemp = qPosition[i]- pWindowWidth;
			        for(int j=qPosition[i]- pWindowWidth+1;j<qPosition[i];j++){ //对第一个R之前冒泡排序，求最小值
			        	if(Z[j]>Z[minTemp])  
					    minTemp = j;
			        }
				    ii = -10;
				    for(int j=-9;j<10;j++) //对pPosition冒泡排序
					    if(M[2*minTemp+2*j+1]>M[2*minTemp+2*ii+1])  
					       	ii = j;
				    pPosition[i] = minTemp + ii;
				    pValue[i] = M[2*pPosition[i]+1];
				    nPPosition++;
				    //System.out.println("Line" + i + ": " + pPosition[i] + ", " + pValue[i]  + ", " + ii); 
			    }
			}
		    //% 如果最后一个S波到信号终端小于0.4倍的rr_interval，那么需要单独检测。否则跟其他波一起检测。
			if((i<nRPosition-1)||((i==nRPosition-1)&&((M.length/2-sPosition[nSPosition-1])>=(0.4*rrIntervalMean[i])))){
		        baselineWindowWidth = (int)(0.4 * rrIntervalMean[i]);
		        minTemp = sPosition[i]+stMiniWidth;
		        for(int j=sPosition[i]+stMiniWidth+1;j<sPosition[i]+stMiniWidth+baselineWindowWidth;j++){ //冒泡排序，求最小值
		        	if(Z[j]>Z[minTemp])  
		        		minTemp = j;
		        }
		        ii = -10;
			    for(int j=-9;j<10;j++) //对pPosition冒泡排序
				    if(M[2*minTemp +2*j+1]>M[2*minTemp+2*ii+1])  
				       	ii = j;
			    tPosition[i] = minTemp + ii;
			    tValue[i] = M[2*tPosition[i]+1];
			    nTPosition++;
		    }
		    else if((i==nRPosition)&&((M.length/2-sPosition[nSPosition-1])<(0.4*rrIntervalMean[i]))){
		    	//% 最后一个T波的单独检测
		        if(nSPosition == nRPosition){   //% 最后一个S波存在，T波才可能存在
		        	minTemp = sPosition[nSPosition-1];
		        	for(int j=sPosition[nSPosition-1]+1;j<M.length/2;j++){ //冒泡排序，求最小值
			        	if(M[2*j+1]>M[2*minTemp+1])  
					    minTemp = j;
			        }
		        	if ((M.length/2- minTemp) < 4){
		                if((M[2*minTemp+1] > M[2*minTemp+3]) &&(M[2*minTemp+1]> M[2*minTemp-1])){
		                    tPosition[nRPosition] = minTemp;
		                    tValue[nRPosition] = M[2*minTemp+1];
		                    flagT = 1;     //% 标志检测到了最后一个T波
		                }
		             }
		        }
		    }
		    //System.out.println("Line" + i + ": "+ pPosition[i] + ", " + pValue[i]  + ", " + tPosition[i] + ", " + tValue[i]);
		}
	}
	
//	%% ------ BASE LINE DETECTION ------------------------------------
//	% 
//	% 提取基线，使用前一个波的T端结束到当前波P波开始这段时间。
//	% baselineValue保存波形的基线值
//	% baselineStartPosition保存基线开始时刻值
//	% baselineEndPosition保存基线结束时刻值
//	% len_baseline保存检测到的基线的个数
//	%
	public static void baseLineDet(String[] args)
	{
		int  minTemp1, minTemp2 = 0,pp, k;
		int startBaseline, endBaseline;
		int baselineWindowWidth;
		double temp;
		lenBaseline = 0;
		for(int i=0; i<nTPosition;i++){
		    baselineWindowWidth = ceil(rrIntervalMean[i]*0.3);
		    
		    //% 寻找基线起点
		    if((tPosition[i] + baselineWindowWidth) > Z.length){
		        break;
		    }
	        minTemp1 = tPosition[i];
	        for(int j=tPosition[i]+1;j<tPosition[i] + baselineWindowWidth;j++){ //对第一个R之前冒泡排序，求最小值
	        	if(Z[j]>Z[minTemp1])  
			    minTemp1 = j;
	        }
		    for(int j=tPosition[i]+10;j<tPosition[i] + baselineWindowWidth;j++){
		        if((Z[j] < Z[j+1]) && (Z[j]<Z[j-1]) ){
		        	minTemp2 = j;
		            break;
		        }
		    }
		    //System.out.println("Line" + i + ": "+ minTemp1 + ", " + minTemp2);
		    if(minTemp1 > minTemp2){
		        startBaseline = minTemp1;
		    }
		    else{
		        startBaseline = minTemp2;
		    }
		    
		    //% 寻找基线终点
		    if(i>nPPosition-1)
		        break;
		    if(pPosition[i] < tPosition[i]){
		        if(i> nPPosition)
		            break;
		        else
		            pp = pPosition[i+1];
		    }
		    else{
		        pp = pPosition[i];
		    }
	        minTemp1 = pp - baselineWindowWidth;
	        for(int j=pp - baselineWindowWidth+1;j<pp;j++){ //对第一个R之前冒泡排序，求最小值
	        	if(Z[j]>Z[minTemp1])  
			    minTemp1 = j;
	        }
		    k = pp - 10;
		    while(k > pp - baselineWindowWidth-1){
		        if((Z[k] < Z[k+1]) && (Z[k]<Z[k-1]) ){
		        	minTemp2 = k;
		            break;
		        }
		        k--;
		    }
		    if(minTemp1  > minTemp2)
		        endBaseline = minTemp2;
		    else{
		        endBaseline = minTemp1;
		    }
		    
		    //% 计算基线值
		    temp = 0.0;
		    for(int j=startBaseline+5;j<endBaseline-5;j++){
		    	temp += M[2*j+1];
		    }
		    baselineValue[i] = temp/(endBaseline-startBaseline-10);
		    baselineStartPosition[i] = startBaseline + 5;
		    baselineEndPosition[i] = endBaseline - 5;
		    lenBaseline++;
		    //System.out.println("Line" + i + ": "+ baselineValue[i] + ", " + baselineStartPosition[i]  + ", " + baselineEndPosition[i]);
		}	
	}
//	%% ------ ONSET, OFFSET AND DURATION OF QRS COMPLEX --------------
//	%
//	% 检测QRS复合波的起止点和持续时间。
//	% 时间窗宽度：起点窗：35ms；终点窗：20ms。
//	% 方案一：从Q波左侧检测幅值为Q波1/8的点为起点，S波右侧幅值为S波1/5的点为终点；
//	% 方案二：从Q波左侧检测最接近基线的点为起点，S波右侧最接近基线的点为终点；
//	% 在这两个方案里，挑选更靠近R波的点作为起点或终点。
//	% 最后结果保存在qrsOnPosition,qrsOffPosition和qrsDuration中。
//	% len_qrs保存检测到的有效QRS复合波的个数（第一个QRS复合波由于没有基线可能被忽略）
//	%	
	public static void qrsComplex(String[] args)
	{
		int qrsOnWindowWidth = ceil(35*sFreq/1000);
		int qrsOffWindowWidth = ceil(sFreq/50);
		int flagQRS = 1;
		int qInd = 0, sInd = 0;
		int i = 0, index, minTemp;
		lenQRS = 0;

		while(qPosition[qInd] < baselineEndPosition[0]){
		    qInd++;
		    flagQRS = 0;
		}

		while ((qInd <= nQPosition) &&( sInd <= nSPosition) &&(i <= lenBaseline)){
		    while(qPosition[qInd] > sPosition[sInd]){
		        sInd++;
		    }
		    //% 确定onset
		    index = qPosition[qInd];
		    while ( index >= (qPosition[qInd] - qrsOnWindowWidth) ){
		        if(qValue[qInd] < baselineValue[i]){
		            if ( (baselineValue[i] - M[2*index+1]) < (baselineValue[i] - qValue[qInd])/8 ){
		                break;
		            }
		        }
		        else{
		            index = qPosition[qInd];
		            break;
		        }
		        index--;
		    }
		    minTemp = qPosition[qInd]- qrsOnWindowWidth;
	        for(int j=qPosition[qInd]- qrsOnWindowWidth+1; j<qPosition[qInd];j++){ //冒泡排序，求最小值
	        	if(Math.abs(M[2*j+1]-baselineValue[i])>Math.abs(M[2*minTemp+1]-baselineValue[i])) 
	        		minTemp = j;
	        }
		    if(minTemp < index){
		        qrsOnPosition[i] = index;
		    }
		    else{
		        qrsOnPosition[i] = minTemp;
		    }
		       
		    //确定offset
		    index = sPosition[sInd];
		    while ( index <= sPosition[sInd] + qrsOffWindowWidth ){
		        if ( sValue[sInd] < baselineValue[i] ){
		            if ( (baselineValue[i] - M[2*index+1]) < (baselineValue[i] - sValue[sInd])/5 ){
		                break;
		            }
		        }
		        else{
		            index = sPosition[sInd];
		            break;
		        }
		        index++;
		    }
	        minTemp = sPosition[sInd];
	        for(int j=sPosition[sInd]+1; j<sPosition[sInd]+ qrsOffWindowWidth;j++){ //冒泡排序，求最小值
	        	if(Math.abs(M[2*j+1]-baselineValue[i])>(Math.abs(M[2*minTemp+1]-baselineValue[i]))) 
	        		minTemp = j;
	        }
		    if(minTemp < index)
		        qrsOffPosition[i] = minTemp;
		    else
		        qrsOffPosition[i] = index;
		    
		    // 确定duration
		    qrsDuration[i] = qrsOffPosition[i] - qrsOnPosition[i];
		    System.out.println("Line" + i + ": "+ qrsDuration[i] + ","+ qrsOnPosition[i] + ", "+ qrsOffPosition[i]);
		    lenQRS++;
		    qInd++;
		    sInd++;
		    i++;
		}
	}
	
//	%% ------ ONSET, OFFSET AND DURATION OF P & T WAVE ---------------
//	%
//	% 检测P波和T波的起止点和持续时间。
//	%
//	% P波起止点检测使用两套方案：
//	% 方案1：分别从P波两侧一定宽度内检测原始信号幅值为P波1/8的点作为起止点；
//	% 方案2：分别从P波两侧一定宽度内检测原始信号波形拐点，即二阶导接近零的点；（这个方案误差很大，排除不用）
//	% 方案3：分别从P波两侧一定宽度内检测原始信号最接近基线的点作为起止点；
//	% 在这两个方案里，挑选更靠近P波的点作为起点或终点。
//	% 最后结果保存在pOnPosition,pOffPosition和pDuration中。len_pw保存个数。
//	%
//	% T波的检验使用如下思路：
//	% 方案1：（实验表明此方案误检率高，排除不用）
//	% 首先针对第十阶小波分析结果，检验T波两侧一定宽度内波形拐点，即二阶导接近零的点；
//	% 然后在这拐点两侧10个点内，搜索原始信号上最接近基线的点作为T波的起点或终点。
//	% 方案2：
//	% 首先针对第十阶小波分析结果，从T波两侧一定宽度内检测信号幅值为T波1/8的点；
//	% 然后在该点两侧10个点内，搜索原始信号上最接近基线的点作为T波的起点或终点。
//	% 方案3：
//	% 首先针对第十阶小波分析结果，从T波两侧一定宽度内检测信号极值点（正向T波检测极小，负向T波检测极大）；
//	% 然后在该点两侧10个点内，搜索原始信号上最接近基线的点作为T波的起点或终点。
//	% 最后结果保存在tOnPosition,tOffPosition和tDuration中。len_tw保存个数。
//	%
/*	public static void ptWave(String[] args)
	{
		pOnPosition = [];
		pOffPosition = [];
		pDuration = [];
		pOnWindowWidth = ceil(sFreq/125);
		pOffWindowWidth = ceil(sFreq/125);
		tOnPosition = [];
		tOffPosition = [];
		tDuration = [];

		//% 初始化P波检测索引
		int bInd = 1, pInd = 1, i = 1;

		//% 找到第一个具有基线信息的P波
		while p_position(pInd) < baselineStartPosition(bInd)
		    pInd = pInd + 1;
		end

		//% 检测P波的起止点和持续时间
		while ( bInd <= len_baseline && pInd <= len_p )
		    % onset of P wave
		    tmp1 = p_position(pInd);
		    while tmp1 > p_position(pInd) - pOnWindowWidth
		        if p_value(pInd) < baseline_value(bInd)
		            if ( baseline_value(bInd) - sig(tmp1) < 1/8*(baseline_value(bInd) - p_value(pInd)) )
		                break;
		            end
		        else
		            if ( baseline_value(bInd) - sig(tmp1) > 1/8*(baseline_value(bInd) - p_value(pInd)) )
		                break;
		            end
		        end
		        tmp1 = tmp1 - 1;
		    end
		    pOnPosition(i) = tmp1;
		    
		    target = sig( (p_position(pInd) - pOnWindowWidth) : p_position(pInd) );

		    [~, tmp3] = min( abs(target - baseline_value(bInd)) );
		    tmp3 = tmp3 + p_position(pInd) - pOnWindowWidth - 1;
		    if pOnPosition(i) < tmp3
		        pOnPosition(i) = tmp3;
		    end

		    % offset of P wave
		    tmp1 = p_position(pInd);
		    while tmp1 < p_position(pInd) + pOffWindowWidth
		        if p_value(pInd) < baseline_value(bInd)
		            if ( baseline_value(bInd) - sig(tmp1) < 1/8*(baseline_value(bInd) - p_value(pInd)) )
		                break;
		            end
		        else
		            if ( baseline_value(bInd) - sig(tmp1) > 1/8*(baseline_value(bInd) - p_value(pInd)) )
		                break;
		            end
		        end
		        tmp1 = tmp1 + 1;
		    end
		    pOffPosition(i) = tmp1;
		    
		    target = sig(p_position(pInd) : p_position(pInd) + pOffWindowWidth);

		    [~, tmp3] = min( abs(target - baseline_value(bInd)) );
		    tmp3 = tmp3 + p_position(pInd);
		    if pOffPosition(i) > tmp3
		        pOffPosition(i) = tmp3;
		    end

		    // % 确定duration
		    pDuration(i) = pOffPosition(i) - pOnPosition(i);
		    
		    pInd = pInd + 1;
		    bInd = bInd + 1;
		    i = i+1;
		end

		//% 初始化T波检测索引
		bInd = 1;
		t_ind = 1;
		qrs_ind = 1;
		i = 1;

		//% 找到第一个具有基线信息的T波
		while t_position(t_ind) < baselineStartPosition(bInd)
		    t_ind = t_ind + 1;
		end
		while t_position(t_ind) < qrsOffPosition(qrs_ind)
		    t_ind = t_ind + 1;
		end

		//% 检测T波的起止点和持续时间
		while ( bInd <= len_baseline && t_ind <= len_t )
		    % onset of T wave
		    target = wtsig10( qrsOffPosition(qrs_ind)+5 : t_position(t_ind) );
		    
		    tmp = t_position(t_ind);
		    while tmp >= qrsOffPosition(qrs_ind)+5
		        if t_value(t_ind) < baseline_value(bInd)
		            if wtsig10(tmp) > 0
		                break;
		            end
		        else
		            if wtsig10(tmp) < 0
		                break;
		            end
		        end
		        tmp = tmp - 1;
		    end
		    [~, tmp2] = min( abs( sig(tmp-20 : tmp+15) - baseline_value(bInd) ) );
		    tmp2 = tmp2 + tmp - 21;
		    tOnPosition(i) = tmp2;
		    
		    tmp = t_position(t_ind);
		    while tmp >= qrsOffPosition(qrs_ind)+5
		        if t_value(t_ind) < baseline_value(bInd)
		            if ( wtsig10(tmp) > wtsig10(tmp-1) && wtsig10(tmp) > wtsig10(tmp+1) )
		                break;
		            end
		        else
		            if ( wtsig10(tmp) < wtsig10(tmp-1) && wtsig10(tmp) < wtsig10(tmp+1) )
		                break;
		            end
		        end
		        tmp = tmp - 1;
		    end
		    [~, tmp3] = min( abs( sig(tmp-10 : tmp+10) - baseline_value(bInd) ) );
		    tmp3 = tmp3 + tmp - 11; 
		    if tmp3 > pOnPosition(i)
		        tOnPosition(i) = tmp3;
		    end

		    % offset of T wave
		    if ( t_ind == len_t || bInd == len_baseline )
		        rri = rr_interval(i-1);
		    else
		        rri = rr_interval(i);
		    end
		    target = wtsig10( t_position(t_ind) : ceil(t_position(t_ind) + 0.125 * rri) );
		    
		    tmp = t_position(t_ind);
		    while tmp <= ceil(t_position(t_ind) + 0.125 * rri)
		        if t_value(t_ind) < baseline_value(bInd)
		            if wtsig10(tmp) > 0
		                break;
		            end
		        else
		            if wtsig10(tmp) < 0
		                break;
		            end
		        end
		        tmp = tmp + 1;
		    end
		    [~, tmp2] = min( abs( sig(tmp-10 : tmp+15) - baseline_value(bInd) ) );
		    tmp2 = tmp2 + tmp - 11;

		    tmp = t_position(t_ind);
		    while tmp <= ceil(t_position(t_ind) + 0.125 * rri)
		        if t_value(t_ind) < baseline_value(bInd)
		            if ( wtsig10(tmp) > wtsig10(tmp-1) && wtsig10(tmp) > wtsig10(tmp+1) )
		                break;
		            end
		        else
		            if ( wtsig10(tmp) < wtsig10(tmp-1) && wtsig10(tmp) < wtsig10(tmp+1) )
		                break;
		            end
		        end
		        tmp = tmp + 1;
		    end
		    [~, tmp3] = min( abs( sig(tmp-10 : tmp+15) - baseline_value(bInd) ) );
		    tmp3 = tmp3 + tmp - 11;
		    if tmp3 < pOnPosition(i)
		        tOffPosition(i) = tmp3;
		    end
		    
		    % duration of T wave
		    tDuration(i) = tOffPosition(i) - tOnPosition(i);
		    
		    bInd = bInd + 1;
		    t_ind = t_ind + 1;
		    qrs_ind = qrs_ind + 1;
		    i = i + 1;
		end

		len_pw = length (pDuration);
		len_tw = length (tDuration);
	}*/
	
//	%% ------ OTHER FEATURES DETECTION -------------------------------
//	%
//	% 根据已经得到的信息提取生理上需要的信息。
//	% 提取的信息包括：
//	% 幅值信息：P、Q、R、S、T、U波（U波暂时不考虑）波峰，QRS振幅绝对值
//	% 间期：P波、PR、Q、QR（室壁激动时间）、QRS、ST、QT、QTc、T、U（U波暂时不考虑）、rr_interval，PPI
//	% 保存的结果见下列清单，其中幅度的单位为毫伏（mv），时间相关的量的单位为秒（s）。
//	%
	public static void otherFeature(String[] args)
	{
		
	}	
	
	public static void main(String[] args) {  
		SAMPLES2READ = 5000;
		ECGProcess.readHeadFile("F:/Java/117.hea");
		ECGProcess.readDataFile("F:/Java/117.dat");
		ECGProcess.readAtrFile("F:/Java/117.atr");
		ECGProcess.rPeakDet(args);
		ECGProcess.rrInterval(args);
		ECGProcess.qsWaveDet(args);
		
		ECGProcess.ptWaveDet(args);
		ECGProcess.baseLineDet(args);
		ECGProcess.qrsComplex(args);
	} 
}