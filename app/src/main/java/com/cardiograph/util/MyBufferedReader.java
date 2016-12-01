package com.cardiograph.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MyBufferedReader {

	private final  static String FILE="src/homework8_8/scores.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			writer();
			reader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	static void writer() throws IOException{
		
		
		//1 FileOutputStream
		
		//2 OutputStreamWriter
		
		//3 BufferedWriter
		BufferedWriter bw = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			/**
			 * 创建一个字节流 true 是否append
			 */
			
			fos = new FileOutputStream(FILE,true);// 
			
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			int i = 0;
			String s = "";

			for (int j = 0; j < 10; j++) {
				s = "student[" + j + "]" + "\tname:" + "std" + j + "\tscore:"
						+ 10 * j;
				bw.write(s);//写入
				bw.newLine();//写入换行符

			}

			bw.flush();

		} finally {
			bw.close();
			osw.close();
			fos.close();
		}
		
	}
	
	/**
	 * 演示 字符流 套一个字节流
	 * @throws IOException
	 */
	static void reader()throws IOException{
		
		//1 FileInputStream
		
		//2 InputStreamReader
		
		//3 BufferedReader
		
		BufferedReader bw = null;
		FileInputStream fos = null;
		InputStreamReader osw = null;
		try {
			fos = new FileInputStream(FILE);
			osw = new InputStreamReader(fos);
			bw = new BufferedReader(osw);
			int i = 0;
			String s = "";

			while((s=bw.readLine())!=null){
				
				System.out.println(s);
			}

			

		} finally {
			bw.close();
			fos.close();
		}
		
	}

}
