package com.cyz.search.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cyz.search.datasource.JDBC;
import com.cyz.search.datasource.OJDBC;
import com.cyz.search.exception.SelfException;

/**
 * <pre>
 * Copyright:	Copyright(C) 2012-2014
 * Filename:	com.cyz.search.util.Resolve
 * Class:       Resolve.java
 * Date:        2014-5-3
 * Author:		<a href="mailto:mrchenyazhou@163.com">mrchenyazhou</a>
 * Version      Fast-Search V1.0.2
 * Description: 表结构解析工具
 * </pre>
 **/
public class Resolve {

	// 记录日志
	//final Logger logger = LoggerFactory.getLogger(getClass());
	Logger logger=new Logger();
	/**
	 * 存储表结构
	 * 
	 * @param tableName
	 *            表名
	 * @return 拼接后的字段名和字段类型
	 */
	@SuppressWarnings({ "static-access" })
	public List<String> getColum(String tableName) {
		// 存放列名参数的数组
		List<String> list = null;
		// 列名
		String columnName;
		// 列类型
		String columnType;
		try {
			DatabaseMetaData dbMeta = (DatabaseMetaData) new JDBC().getConn()
					.getMetaData();
			// 获取列的信息
			ResultSet colRet = dbMeta.getColumns(null, "%", tableName, "%");
			// 获取主键
			// ResultSet pkRSet = dbMeta.getPrimaryKeys(null, null, "tab_user");
			/*
			 * 获取主键 相关信息 System.err.println("TABLE_CAT : "+pkRSet.getObject(1));
			 * System.err.println("TABLE_SCHEM: "+pkRSet.getObject(2));
			 * System.err.println("TABLE_NAME : "+pkRSet.getObject(3));
			 * System.err.println("COLUMN_NAME: "+pkRSet.getObject(4));
			 * System.err.println("KEY_SEQ : "+pkRSet.getObject(5));
			 * System.err.println("PK_NAME : "+pkRSet.getObject(6));
			 */
			list = new ArrayList<String>();
			logger.debug("-------------------↓表结构字段信息↓-------------------");
			while (colRet.next()) {
				// 列名
				columnName = colRet.getString("COLUMN_NAME");
				// 类型
				columnType = colRet.getString("TYPE_NAME");
				// 大小
				int datasize = colRet.getInt("COLUMN_SIZE");
				// 位数
				int digits = colRet.getInt("DECIMAL_DIGITS");
				// 是否为空 0否 1是
				int nullable = colRet.getInt("NULLABLE");
				logger.debug(" ┌───────────────────────────────────────────────────┐");
				logger.debug(" │ " + columnName + " │ " + columnType + " │ "
						+ datasize + " │ " + digits + " │ " + nullable + " │");
				logger.debug(" └───────────────────────────────────────────────────┘");
				// 拼接的结构 str1+","+str2
				list.add(columnName + "," + columnType);
			}
			logger.debug("-------------------↑表结构字段信息↑-------------------");
			new JDBC().closeResources();
		} catch (SQLException e) {
			new SelfException(e);
		}
		return list;
	}

	/**
	 * 存储表结构 ORACLE
	 * 
	 * @param tableName
	 *            表名
	 * @return 拼接后的字段名和字段类型
	 */
	@SuppressWarnings({ "static-access" })
	public List<String> getOColum(String tableName) {
		// 存放列名参数的数组
		List<String> list = null;
		// 列名
		String columnName;
		// 列类型
		String columnType;
		try {
			DatabaseMetaData dbMeta = (DatabaseMetaData) new OJDBC()
					.getConnection().getMetaData();
			// 获取列的信息
			ResultSet colRet = dbMeta.getColumns(null, "%", tableName, "%");
			// 获取主键
			// ResultSet pkRSet = dbMeta.getPrimaryKeys(null, null, "tab_user");
			/*
			 * 获取主键 相关信息 System.err.println("TABLE_CAT : "+pkRSet.getObject(1));
			 * System.err.println("TABLE_SCHEM: "+pkRSet.getObject(2));
			 * System.err.println("TABLE_NAME : "+pkRSet.getObject(3));
			 * System.err.println("COLUMN_NAME: "+pkRSet.getObject(4));
			 * System.err.println("KEY_SEQ : "+pkRSet.getObject(5));
			 * System.err.println("PK_NAME : "+pkRSet.getObject(6));
			 */
			list = new ArrayList<String>();
			logger.debug("┏━━━━━━━━━━━━━━━━━━━━━━[表结构字段信息 ]━━━━━━━━━━━━━━━━━━━━━━┓");
			logger.debug("┃─────────────────────────────────────────────────────┃");
			logger.debug("┃                               作者:陈亚洲                 日期:2014/05/19         版本:V1.0.2                   ┃");
			logger.debug("┃─────────────────────────────────────────────────────┃");
			while (colRet.next()) {
				// 列名
				columnName = colRet.getString("COLUMN_NAME");
				// 类型
				columnType = colRet.getString("TYPE_NAME");
				// 大小
				int datasize = colRet.getInt("COLUMN_SIZE");
				// 位数
				int digits = colRet.getInt("DECIMAL_DIGITS");
				// 是否为空 0否 1是
				int nullable = colRet.getInt("NULLABLE");
				logger.debug("┃ ┌───────────────────────────────────────────────────┐┃");
				logger.debug("┃ │ " + columnName + " │ " + columnType + " │ "
						+ datasize + " │ " + digits + " │ " + nullable + " │");
				logger.debug("┃ └───────────────────────────────────────────────────┘┃");
				// 拼接的结构 str1+","+str2
				list.add(columnName + "," + columnType);
			}
			logger.debug("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			new JDBC().closeResources();
		} catch (SQLException e) {
			new SelfException(e);
		}
		return list;
	}
}
