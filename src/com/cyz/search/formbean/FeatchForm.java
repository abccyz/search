package com.cyz.search.formbean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cyz.search.util.PropertieUtil;

/**
 * <pre>
 * Copyright:	Copyright(C) 2012-2014
 * Filename:	com.cyz.search.formbean.FeatchForm
 * Class:       FeatchForm.java
 * Date:        2014-5-3
 * Author:		<a href="mailto:mrchenyazhou@163.com">mrchenyazhou</a>
 * Version      Fast-Search V1.0.2
 * Description: 表单处理
 * </pre>
 **/
public class FeatchForm {
	/*
	 * 大于  		greater than 			 --->GreateThan
	 * 
	 * 小于  		less than 				 --->LessThan
	 * 
	 * 等于 		equal to 				 --->EqualTo
	 * 
	 * 不等于		unequal to 				 --->UnequalTo
	 * 
	 * 大于等于	greater than be equal or --->GreaterToEqual
	 * 
	 * 小于等于 	less than or equal to	 --->LessToEqual
	 * 
	 */
	// 参数集合
	private List<Object> paras = new ArrayList<Object>();
	//定义表单中关键字
	private String[] formCondition={"GreateThan","GreaterToEqual","EqualTo","UnequalTo","LessToEqual","LessThan"};
	// 定义查询条件
	private String[] condition = { ">", ">=", "=", "!=", "<=", "<", "like" };
	
	private List<String> list=new ArrayList<String>();

	/**
	 * 获取表单中的参数对象
	 * 
	 * @param request
	 *            表单对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> featchFrom(HttpServletRequest request) {
		paras.clear();
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			Object obj = (Object) enums.nextElement();
			if (than(obj)) {
				//模糊查询中注意字符之间的空格会影响查询
				if(!"".equalsIgnoreCase(request.getParameter(obj.toString().trim()))){
					/*paras.add(obj + " like '%"
							+ request.getParameter(obj.toString()).trim() + "%'");*/
					paras.add(pagingForm(request,obj));
				}
			}
		}
		return paras;
	}

	/**
	 * 提取配置文件中的关键字
	 * 
	 * @param 表单中的参数
	 * @return 返回一个布尔值
	 */
	public Boolean than(Object obj) {
		for (String str : PropertieUtil.getVal("keyW").split(",")) {
			if (str.equalsIgnoreCase(obj.toString())) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 
	 * @param request 表单请求
	 * @param keyName 字段名
	 * @return
	 */
	public String pagingForm(HttpServletRequest request,Object keyName){
		String data[]=request.getParameter(keyName.toString()).trim().split(PropertieUtil.getVal("keyMake"));
		if(data.length>1){
			//区间查询   to_char(a.BIRTHDAY,'yyyy-mm-dd') >= '2004-08-01' and to_char(a.BIRTHDAY,'yyyy-mm-dd') <= '2004-08-08';
			return "to_char("+keyName+",'yyyy-mm-dd') >= '"+data[0]+"' and to_char("+keyName+",'yyyy-mm-dd') <= '"+data[1]+"'";
		}else {
			//不是区间查询
			list=Arrays.asList(formCondition);
			if(list.contains(keyName)){
				return keyName+" "+condition[list.indexOf(keyName)]+request.getParameter(keyName.toString()).trim()+" ";
			}else {
				return keyName+" "+ " like '%"+ request.getParameter(keyName.toString()).trim() + "%'";
			}
		}
		
	}

	public static void main(String[] args) {
		String str="1000-500";
		String[] uu=str.split(PropertieUtil.getVal("keyMake"));
		System.err.println(uu.length>1);
		for(String dt:uu){
			System.err.println(dt);
		}
	
	}
}
