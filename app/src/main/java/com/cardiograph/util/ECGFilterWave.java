package com.cardiograph.util;

import java.util.ArrayList;
import java.util.List;

public class ECGFilterWave{
/*	%带阻滤波
	%使用注意事项：通带或阻带的截止频率与采样率的选取范围是不能超过采样率的一半
	%即，f1,f3,fs1,fsh,的值小于 Fs/2
	%x:需要带通滤波的序列
	% f 1：通带左边界
	% f 3：通带右边界
	% fs1：衰减截止左边界
	% fsh：衰变截止右边界
	%rp：边带区衰减DB数设置
	%rs：截止区衰减DB数设置
	%FS：序列x的采样频率
	% f1=300;f3=500;%通带截止频率上下限
	% fsl=200;fsh=600;%阻带截止频率上下限
	% rp=0.1;rs=30;%通带边衰减DB值和阻带边衰减DB值
	% Fs=2000;%采样率*/

	private int f1=45;  //% f 1：通带左边界       
	private int f3=55;  //% f 3：通带右边界          
	private int fsl=48; //% fs1：衰减截止左边界        
	private int fsh=52; //% fsh：衰变截止右边界        
	private float rp=0.1f;             //%rp：边带区衰减DB数设置       
	private int rs=1;   //%rs：截止区衰减DB数设置
	
//	public int trapper(int data, int fs){
//		float wp1=2.0*Math.PI*f1/fs;
//		float wp3=2.0*Math.PI*f3/fs;
//		float wsl=2.0*Math.PI*fsl/fs;
//		float wsh=2.0*Math.PI*fsh/fs;
////		wp=[wp1 wp3];
////		ws=[wsl wsh];
//		
//		//% 设计切比雪夫滤波器；
//		[n,wn]=cheb1ord(ws/pi,wp/pi,rp,rs);
//		[bz1,az1]=cheby1(n*wn/wn,rp,wp/pi,'stop');
//		y=filter(bz1,az1,x);
//	}
	
	//一阶band stop 滤波器，滤掉50Hz的工频
	public int trapper(int data, int fs){
		//传输函数：
//		double s = (data*data+0.987*Math.E+4)/(data*data+6.283*data+0.987*Math.E+4);
		/*转换为时域后的数学表达式：其中：dirac就是数学意义上的delta函数，
		此函数在x不等于0的地方都为0，在等于0时为无穷大。*/
		int dirac = data!=0 ? 0 : 17000000;
		double y = dirac - 6283.0/1000*Math.exp(-6283.0/2000*data)*
				Math.cos(1.0/2000*Math.pow(394760523911.0, 0.5)*data)+
				39476089.0/394760523911.0/1000*Math.pow(394760523911.0, 0.5)*
				Math.exp(-6283.0/2000*data)*Math.sin(1.0/2000*Math.pow(394760523911.0, 0.5));
		return (int) y;
	}
	
	/**
	 *  函数名称 : Filter
	 *  功能描述 : 对蓝牙接收到的ECG数据进行过滤
	 *  参数及返回值说明：
	 *  	@param DataList
	 *  	@return
	 *
	 *  修改记录：
	 *  	日期 ：2015-2-5 下午4:47:02	修改人：yjx
	 *  	描述	：
	 *
	 */
	public static List<Integer> Filter(List<Integer> DataList) {
		int total_number = DataList.size();
		int temp_Data_One[] = new int[total_number];
		int temp_Data_Two[] = new int[total_number];
		List<Integer> reslut_Data = new ArrayList<Integer>();
		for (int index = 0; index < total_number; index++) {
			temp_Data_One[index] = DataList.get(index);
			temp_Data_Two[index] = 0;
		}
		int sample_Index = 10;
		for (int index = 0; index <= sample_Index; index++) {
			temp_Data_Two[index] = temp_Data_One[index];
			reslut_Data.add(temp_Data_Two[index]);
		}
		for (int index_i = sample_Index + 1; index_i < total_number; index_i++) {
			for (int index_j = 0; index_j <= sample_Index + 1; index_j++) {
				temp_Data_Two[index_i] += temp_Data_One[index_i - index_j];
			}
			int data = (int) (temp_Data_Two[index_i]*1.0/ (sample_Index + 1));
			reslut_Data.add(data);
		}
		return reslut_Data;
	}
}