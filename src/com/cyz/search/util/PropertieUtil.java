package com.cyz.search.util;

import java.util.Properties;

import com.cyz.search.exception.SelfException;

/**
 * <pre>
 * Copyright:		Copyright(C) 2012-2014
 * Filename:		com.cyz.search.util.PropertieUtil.java
 * Class:			PropertiesUtil
 * Date:			2014-5-3
 * Author:			<a href="mailto:mrchenyazhou@163.com">mrchenyazhou</a>
 * Version          Fast-Search V1.0.2
 * Description:		配置文件加载工具
 * </pre>
 **/
public class PropertieUtil {
	Properties properties = new Properties();

	public static Properties getPro() {
		Properties properties = new Properties();
		try {
			properties.load(PropertieUtil.class.getClassLoader()
					.getResourceAsStream("Configuration"));
		} catch (Exception e) {
			new SelfException(e);
		}
		return properties;
	}

	/**
	 * 根据key返回对应的value
	 * 
	 * @param key
	 *            帶查找的key
	 * @return
	 */
	public static String getVal(String key) {
		return getPro().getProperty(key);
	}
}
