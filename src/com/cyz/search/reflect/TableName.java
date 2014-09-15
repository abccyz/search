package com.cyz.search.reflect;

import javax.persistence.Table;

public class TableName {
	/**
	 * 获得表名
	 * 
	 * @param clazz
	 *            映射到数据库的po类
	 * @return tableName
	 */
	public static String getTableName(Class<?> clazz) {

		Table table = clazz.getClass().getAnnotation(Table.class);
		if (table != null) {
			return table.name();
		}
		return null;
	}

}
