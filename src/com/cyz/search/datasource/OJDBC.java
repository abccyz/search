package com.cyz.search.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyz.search.exception.SelfException;
import com.cyz.search.reflect.CglibBean;
import com.cyz.search.reflect.TableName;
import com.cyz.search.util.ConversionType;
import com.cyz.search.util.PropertieUtil;
import com.cyz.search.util.Resolve;
import com.mysql.jdbc.CallableStatement;

/**
 * <pre>
 * Copyright:	Copyright(C) 2012-2014
 * Filename:	com.cyz.search.datasource.OJDBC 
 * Class:       OJDBC.java
 * Date:        2014-5-3
 * Author:		<a href="mailto:mrchenyazhou@163.com">mrchenyazhou</a>
 * Version      Fast-Search V1.0.2
 * Description: OJDBC工具
 * </pre>
 **/
public class OJDBC {
	// 日志
	private final Logger logger = LoggerFactory.getLogger(getClass());
	// 数据库连接
	private static Connection conn = null;
	// 数据库执行
	private static PreparedStatement pStatement;// 执行动态sql
	private static CallableStatement callableStatement;// 执行存储过程
	// 结果集
	private static ResultSet rSet;
	// 设置类成员属性
	HashMap<String, Class<?>> propertyMap = null;
	// 生成动态 Bean
	CglibBean bean = null;
	// 存放反射对象
	List<CglibBean> rmList = null;

	private static String url = PropertieUtil.getVal("url");// "jdbc:mysql://localhost:3306/test";
	private static String userName = PropertieUtil.getVal("userName");// "root";
	private static String passWord = PropertieUtil.getVal("passWord");// "root";

	public OJDBC() {

	}

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			new SelfException(e);
		}
	}

	public static synchronized Connection getConnection() {
		// 加入oracle的驱动，“”里面是驱动的路径
		Connection conn = null;
		try {
			// 获取连接
			conn = DriverManager.getConnection(url, userName, passWord);
		} catch (SQLException e) {
			new SelfException(e);
		}
		return conn;
	}

	/**
	 * 查询贮备
	 * 
	 * @param sql
	 *            查询语句
	 * @return
	 */
	private PreparedStatement returnP(String sql) {
		try {
			pStatement = getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			closeResources();
			new SelfException(e);
		}
		return pStatement;
	}

	/**
	 * 执行动态sql
	 * 
	 * @param sql
	 *            查询语句
	 * @param tableName
	 * @return
	 */
	public List<?> dynamicSQL(String sql, Class<?> clazz,String tableName) {
	 
		// 创建一个对象集合
		propertyMap = new HashMap<String, Class<?>>();
		rmList = new ArrayList<CglibBean>();
		try {
			List<String> typeList =null;
			//优先采用实体映射查询
			if(clazz!=null &&!"".equals(clazz)){
				// 得到表结构
				System.out.println(">>>实体映射得到表名:"+TableName.getTableName(clazz));
				typeList = new Resolve().getOColum(TableName.getTableName(clazz));
			}else{
				System.out.println(">>>输入的表名:"+tableName);
				typeList = new Resolve().getOColum(tableName);
			}
			// 得到表结构
			//List<String> typeList = new Resolve().getOColum(TableName.getTableName(clazz));
			rSet = returnP(sql).executeQuery();
			while (rSet.next()) {
				for (String str : typeList) {
					String[] dat = str.split(",");

					logger.debug(dat[0] + "=" + rSet.getString(dat[0]) + " 类型："
							+ new ConversionType().rJavaType(dat[1]));
					// 赋予对象属性
					propertyMap.put(dat[0], Class.forName(new ConversionType()
							.rJavaType(dat[1])));

				}
				bean = new CglibBean(propertyMap);
				for (String str : typeList) {
					String[] dat = str.split(",");
					// 给对象赋值
					// bean.setValue(dat[0], rSet.getString(dat[0]));
					if ("java.lang.String".equals(new ConversionType()
							.rJavaType(dat[1]))) {
						bean.beanMap.put(dat[0], rSet.getString(dat[0]));
					} else if ("java.util.Date".equals(new ConversionType()
							.rJavaType(dat[1]))) {
						bean.beanMap.put(dat[0], rSet.getDate(dat[0]));
					} else if ("java.lang.Long".equals(new ConversionType()
							.rJavaType(dat[1]))) {
						bean.beanMap.put(dat[0], rSet.getLong(dat[0]));
					} else if ("java.lang.Byte".equals(new ConversionType()
							.rJavaType(dat[1]))) {
						bean.beanMap.put(dat[0], rSet.getByte(dat[0]));
					} else if ("java.lang.Boolean".equals(new ConversionType()
							.rJavaType(dat[1]))) {
						bean.beanMap.put(dat[0], rSet.getBoolean(dat[0]));
					} else if ("java.lang.Float".equals(new ConversionType()
							.rJavaType(dat[1]))) {
						bean.beanMap.put(dat[0], rSet.getFloat(dat[0]));
					} else if ("java.lang.Integer".equals(new ConversionType()
							.rJavaType(dat[1]))) {
						bean.beanMap.put(dat[0], rSet.getInt(dat[0]));
					} else if ("java.lang.Short".equals(new ConversionType()
							.rJavaType(dat[1]))) {
						bean.beanMap.put(dat[0], rSet.getShort(dat[0]));
					}
				}
				// 将bean对象放倒集合
				rmList.add(bean);
			}
			// 关闭所有连接
			closeResources();
		} catch (Exception e) {
			new SelfException(e);
		}
		return rmList;
	}

	// 批处理查询
	private CallableStatement returnC(String sql) {
		// getConn().prepareCall("{CALL demoSp(? , ?)}") ;
		try {
			callableStatement = (CallableStatement) getConnection()
					.prepareCall(sql);
		} catch (SQLException e) {
			closeResources();
			new SelfException(e);
		}
		return callableStatement;
	}

	// 执行存储过程sql
	@SuppressWarnings({ "unused", "rawtypes" })
	private List<?> proceduresSQL(String sql) {
		Collection<?> collection = new ArrayList();
		try {
			rSet = returnC(sql).executeQuery();
			while (rSet.next()) {
				collection.add(null);
			}
		} catch (SQLException e) {
			new SelfException(e);
		}
		return null;
	}

	public void closeResources() {
		try {
			// 清除
			pStatement.clearBatch();
			callableStatement.clearBatch();
			// 关闭
			conn.close();
			rSet.close();
			pStatement.close();
			callableStatement.close();
		} catch (Exception e) {
			// 不理会该异常
		}
	}
}
