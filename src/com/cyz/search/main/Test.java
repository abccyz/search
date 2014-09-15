package com.cyz.search.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test {
	public static void main(String[] args) {
		try {
			String sourceURL = "http://i1.sinaimg.cn/blog/2011/1202/U3370P346DT20111202110614.jpg";
			URL url = new URL(sourceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 获取方式
			conn.setRequestMethod("GET");
			// 链接超时
			conn.setConnectTimeout(5 * 1000);
			// 通过输入流获取图片数据
			InputStream inStream = conn.getInputStream();
			byte[] data = readInputStream(inStream);
			File imageFile = new File("C:/sean.jpg");
			FileOutputStream outStream = new FileOutputStream(imageFile);
			outStream.write(data);
			outStream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static byte[] readInputStream(InputStream inStream) {
		try {
			byte[] buffer = new byte[1024];
			int len = 0;
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			return outStream.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	 
	
}
