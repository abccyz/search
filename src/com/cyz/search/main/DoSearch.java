package com.cyz.search.main;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cyz.search.datasource.OJDBC;
import com.cyz.search.exception.SelfException;
import com.cyz.search.formbean.FeatchForm;
import com.cyz.search.reflect.CglibBean;
import com.cyz.search.reflect.TableName;

/**
 * <pre>
 * Copyright:	Copyright(C) 2012-2014
 * Filename:	com.cyz.search.main.Search
 * Class:       Search.java
 * Date:        2014-5-3
 * Author:		<a href="mailto:mrchenyazhou@163.com">mrchenyazhou</a>
 * Version      Fast-Search V1.0.2
 * Description: Fast-Search V1.0.2
 * </pre>
 **/
public class DoSearch {
	// 查询结果集
	List<CglibBean> list = null;
	// 条件拼接
	private StringBuffer splice = null;
	// 存放排序字段
	StringBuffer sortList = null;
	// 定义查询条件
	private String[] condition = { ">", ">=", "=", "!=", "<=", "<", "like" };

	StringBuffer sBuffer = null;

	@SuppressWarnings("unchecked")
	public List<?> fastSearch(String tabName, String[] args) {
		// 表名不为空
		splice = new StringBuffer();
		// 存放排序字段
		sortList = new StringBuffer();
		// 初始化sql语句
		splice.append("SELECT * FROM " + tabName);
		// 排序
		String[] maxTmp = null;
		String[] minTmp = null;
		boolean flag = false;
		String dat = "";
		// 存在条件查询
		if (args.length > 0) {
			if (args.length > condition.length) {
				maxTmp = args;
				minTmp = condition;
			} else if (condition.length > args.length) {
				maxTmp = condition;
				minTmp = args;
			}
			foo1: for (int i = 0; i < maxTmp.length; i++) {
				for (int j = 0; j < minTmp.length; j++) {
					if (minTmp[j].indexOf(maxTmp[i]) > 0) {
						dat = args[j];
						flag = true;
						break foo1;
					}
				}
			}
			if (flag) {
				// 是条件查询的一种
				splice.append(" WHERE " + dat);
			} else {
				// 是排序操作
				sortList.append(dat + ",");
			}

			// 排序字段的处理
			if (sortList.length() > 0) {
				splice.append(" ORDER BY ");
				splice.append("'"
						+ sortList.toString().substring(0,
								sortList.toString().lastIndexOf(",") - 1) + "'");
				splice.append(" DESC");// 排序类型
			}
			// 创建查询对象
			OJDBC oj = new OJDBC();
			// 执行查询
			list = (List<CglibBean>) oj.dynamicSQL(splice.toString(),null, tabName);
		} else {
			throw new RuntimeException("请正确传入查询参数,异常代码:0000X2");
		}
		return list;
	}

	/**
	 * 简单web查询接口
	 * 
	 * @param request
	 *            请求
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> fastSearch(Class<?> clazz,String tabName,
			HttpServletRequest request) {
		// 取得表单元素
		List<Object> lists = new FeatchForm().featchFrom(request);

		sBuffer = new StringBuffer("SELECT * FROM " + tabName);

		if (!lists.isEmpty() && lists.size() > 0) {
			sBuffer.append(" WHERE ");
			for (int i = 0; i < lists.size(); i++) {
				/*
				 * if (lists.get(i).toString().split("like").length == 1) {
				 * sBuffer.append(lists.get(i).toString().split(":")[0]); } else
				 * if (lists.get(i).toString().split("like").length == 2) {
				 * sBuffer.append(lists.get(i).toString().split(":")[0] +
				 * " like '%" + lists.get(i).toString().split(":")[1] + "%'"); }
				 */
				if (i >= 1) {
					sBuffer.append(" AND ");
				}
				sBuffer.append(lists.get(i).toString());
			}
		}
		// 创建查询对象
		OJDBC oj = new OJDBC();
		// 执行查询     优先采用实体映射查询
		if(clazz!=null&&!"".equals(clazz)){
			tabName=TableName.getTableName(clazz);
		}
		return (List<CglibBean>) oj.dynamicSQL(sBuffer.toString(),clazz, tabName);
	}
	/**
	 * 本地化查询接口
	 * 
	 * 根据输入表名查询
	 */
	@SuppressWarnings("unchecked")
	public List<?> fastSearch(String tabName) {
		sBuffer = new StringBuffer("SELECT * FROM " + tabName);
		// 创建查询对象
		OJDBC oj = new OJDBC();
		// 执行查询
		return (List<CglibBean>) oj.dynamicSQL(sBuffer.toString(),null, tabName);
	}
	/**
	 * 本地化查询接口
	 * 
	 * 根据对实体的映射得到表名
	 */
	@SuppressWarnings("unchecked")
	public List<?> fastSearch(Class<?> clazz) {
		sBuffer = new StringBuffer("SELECT * FROM " + TableName.getTableName(clazz));
		// 创建查询对象
		OJDBC oj = new OJDBC();
		// 执行查询
		return (List<CglibBean>) oj.dynamicSQL(sBuffer.toString(),clazz, null);
	}
	
	
	/**
	 * 对已查询出的结果进行分页处理
	 * 
	 * @param pageSize
	 *            每页大小
	 * @param page
	 *            分页
	 * @param list
	 *            记录集合
	 * @return
	 */
	public List<CglibBean> paging(String pageNow, String pageSize,
			List<CglibBean> list) {

		try {
			if (pageNow == null) {
				pageNow = "1";// 初始化当前页数
			} else {
				pageNow = (Integer.parseInt(pageNow) + 1) + "";
			}
			if (pageSize == null) {
				pageSize = "10";// 初始化每页显示条数
			}
			int pN = Integer.parseInt(pageNow);// 当前页 2 3

			int pS = Integer.parseInt(pageSize);// 每页显示个数 10

			int MaxTotal = pN * pS;
			/*
			 * if (pN > 1) { pN = Integer.parseInt((pN - 1) + "1"); }
			 */

			// 判断最大记录数
			if (MaxTotal > list.size()) {
				MaxTotal = list.size();
			}
			pN = pN * pS - pS + 1;
			/*
			 * if(MaxTotal>1){ MaxTotal=MaxTotal-1; }
			 */
			if (list.size() == 0) {
				// 搜索的集合为空
				MaxTotal = 0;
				pN = 0;
			}
			if (list.size() == 1) {
				// 搜索的集合为空
				MaxTotal = 1;
				pN = 0;
			}
			// 抓取条件
			if (pN >= 1) {
				pN = pN - 1;
			}
			list = list.subList(pN, MaxTotal);

		} catch (NumberFormatException e) {
			new SelfException(e);
		}
		return list;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public static void main(String[] args) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:ss:dd");
		Long start=System.currentTimeMillis();
		DoSearch doSearch = new DoSearch();

		OJDBC ojdbc = new OJDBC();

//		List<CglibBean> list = (List<CglibBean>) ojdbc.dynamicSQL(
//				"select * from s_teausercard",
//				"s_teausercard");
		
		
		/*for (CglibBean cglibBean : list) {
			System.out.println(cglibBean.getValue("cardno")+":"+cglibBean.getValue("opentime"));
		}
		if(list.size()<=0){
			System.err.print("未查询到符合的记录 ！");
		}*/
		List<CglibBean> lists=	(List<CglibBean>) doSearch.fastSearch("s_usersignin");
		for (CglibBean cglibBean : lists) {
			System.out.println(cglibBean.getValue("id")+":"+cglibBean.getValue("status"));
		}
		Long end=System.currentTimeMillis();
		System.err.println(">>>耗时:"+(end-start)+"毫秒");
	}

}
