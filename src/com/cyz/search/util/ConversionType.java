package com.cyz.search.util;

import java.util.Arrays;

/**
 * <pre>
 * Copyright:	Copyright(C) 2012-2014 
 * Filename:	com.cyz.search.reflect.TypeConversion
 * Class:       TypeConversion.java
 * Date:        2014-5-3
 * Author:		<a href="mailto:mrchenyazhou@163.com">mrchenyazhou</a>
 * Version      Fast-Search V1.0.2
 * Description: 数据库中字段类型转java实体中字段类型
 * </pre>
 **/
public class ConversionType {
	// 数据库中的类型
	private final java.lang.String[] sqlType = { "CHAR", "VARCHAR", "VARCHAR2",
			"NCHAR", "NVARCHAR2", "DATE", "LONG", "RAW", "LONGRAW", "BLOB",
			"CLOB", "NCLOB", "BFILE", "ROWID", "NROWID", "NUMBER", "DECIMAL",
			"INTEGER", "FLOAT", "REAL", "INT" };
	// 匹配的java中的类型
	private final java.lang.String[] strType = { "java.lang.String",
			"java.lang.String", "java.lang.String", "java.lang.String",
			"java.lang.String", "java.util.Date", "java.lang.Long",
			"java.lang.Bytes", "java.lang.Byte", "", "", "", "", "", "",
			"java.lang.Integer", "", "java.lang.Integer", "java.lang.Float",
			"", "java.lang.Integer" };

	public String rJavaType(String type) {
		return strType[findIndex(type)];
	}

	private int findIndex(String type) {
		for (String str : sqlType) {
			if (str.equalsIgnoreCase(type)) {
				return Arrays.asList(sqlType).indexOf(str);
			}
		}
		// 不存在就给string的索引
		return 0;
	}
}
