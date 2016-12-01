package com.cardiograph.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicHeader;


/**
 * 多部 Entity 实现
 * 
 * @author Michael
 *
 */

public class MultiPartEntity extends AbstractHttpEntity implements Cloneable {
	static private final String MULTIPART_CHARS = 
			"-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static private final String CRLF = "\r\n";
	static private final String EXTRA = "--";

	
	private String boundary;  //多部分 隔符
	private byte[] end_bytes;  //结尾分隔符
	private PrintStream printer;
	private ByteArrayOutputStream content;
	
	public MultiPartEntity()
	{
		super();

		//生成 Body分隔符
		long   t =       System.nanoTime() ^ System.currentTimeMillis();
		Random random  = new Random(t);	
		int    tabSize = MULTIPART_CHARS.length();
		StringBuffer sb= new StringBuffer();
		
		//产生40位长多部分隔符
		sb.append("--------------------"); 
		for (int i=0;i< 20;i++)
		{
			sb.append(MULTIPART_CHARS.charAt(random.nextInt(tabSize)));
		}
		
		//合成结尾分隔符
		end_bytes = ("--" + sb.toString() + "--\r\n").getBytes();
		
		//合成多部分隔符
		sb.append("\r\n");
		boundary=sb.toString();
		
		//Content 类型
		this.contentType =  new BasicHeader("Content-Type" ,"multipart/form-data; boundary=" + boundary);
		
		//编码类型
		this.contentEncoding =  new BasicHeader("Content-Encoding ",HttpUtil.EncodingCharset);
		
		this.chunked = false;
		
		//初始化数据
		content = new ByteArrayOutputStream();
		printer = new PrintStream(content);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		//提供深层拷贝支持
		MultiPartEntity newobj = (MultiPartEntity) super.clone();
		
		newobj.content = new ByteArrayOutputStream();
		newobj.printer = new PrintStream(newobj.content);
		try {
			printer.write(this.content.toByteArray());
		} catch (IOException e) {
			//忽略 IO 异常
		}
		
		return newobj;
	}

	@Override
	protected void finalize() throws Throwable {
		printer.close();
		super.finalize();
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		writeTo(tmp);
		ByteArrayInputStream bais = new ByteArrayInputStream(tmp.toByteArray());
		tmp.close();
		return bais;
	}

	@Override
	public long getContentLength() {
		return content.size() + end_bytes.length;
	}

	@Override
	public boolean isRepeatable() {
		//是否可以重复调用 writeTo 和 getContent
		return true;
	}

	@Override
	public boolean isStreaming() {
		//指示是否是流 Entity ，如果是需用一个EOF 表示流结尾。
		return false;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		//不在 content 里直接添加结尾分隔符是为为了可以多次调用 
		//getContent 与  writeTo 所以的在该方法中添加结尾。
		
		/*
		//分段向流写入数据
		int block_size = 256;
		int total_bytes = content.size();
		byte[] content_bytes = content.toByteArray();
		
		for (int index = 0;index < total_bytes ;index += block_size)
		{
			if (block_size <= (total_bytes -index))
			{
				outstream.write(content_bytes,index,block_size);
			}
			else
			{
				outstream.write(content_bytes,index,total_bytes -index);
			}
		}
		*/
		outstream.write(content.toByteArray());
		outstream.write(end_bytes);
		outstream.flush();
	}

	/**
	 * 添加普通字段域
	 * @param name   字段名
	 * @param value  字段值
	 * @throws java.io.UnsupportedEncodingException
	 */
	public void addPart(String name,String value) throws UnsupportedEncodingException
	{
		printer.print("--"); //part 起始
		printer.print(boundary);
		printer.print("Content-Disposition: form-data; name=\""); //表单数据头
		printer.print(name); //字段名
		printer.print("\"\r\nContent-Type: application/x-www-form-urlencoded\r\n\r\n"); 
		printer.print(URLEncoder.encode(value,HttpUtil.EncodingCharset)); //数据
		printer.print("\r\n"); //part 结束
		printer.flush();
	}
	
	/**
	 * 添加文件域
	 * @param name 		字段名
	 * @param filename  文件名
	 * @param buffer    文件数据
	 * @throws java.io.IOException
	 */
	public void addPart(String name,String filename,byte[] buffer) throws IOException
	{
		printer.print("--"); //part 起始
		printer.print(boundary);
		printer.print("Content-Disposition: form-data; name=\""); //表单数据头
		printer.print(name); //字段名
		printer.print("\"; filename=\"");
		printer.print(filename); //文件名名
		printer.print("\"\r\nContent-Type: application/octet-stream\r\n\r\n"); 
		printer.write(buffer);//文件数据
		printer.print("\r\n"); //part 结束
		printer.flush();
	}
	
}
