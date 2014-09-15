package com.cyz.search.exception;

import java.io.Serializable;

/**
 * <pre>
 * Copyright:	Copyright(C) 2012-2014
 * Filename:	com.cyz.search.exception.SelfException
 * Class:       SelfException.java
 * Date:        2014-5-3
 * Author:		<a href="mailto:mrchenyazhou@163.com">mrchenyazhou</a>
 * Version      Fast-Search V1.0.2
 * Description: 异常的统一处理
 * </pre>
 **/
public class SelfException extends Exception implements Serializable {

	private static final long serialVersionUID = 7216267738107722403L;

	public SelfException(Exception exception) {
		super();
		System.out.println("{引起异常信息：" + exception.getMessage() + ",引起异常原因："
				+ exception.getCause() + "}");
	}

	public SelfException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(cause);
		System.out.println("{引起异常信息：" + message + ",引起异常原因：" + cause
				+ ",是否能够处理该异常：" + enableSuppression + ",是否可以堆栈跟踪："
				+ writableStackTrace + "}");

	}

	public SelfException(String message, Throwable cause) {
		super(message, cause);
		System.out.println("{引起异常信息：" + message + "，引起原因：" + cause + "}");
	}

	public SelfException(String message) {
		super(message);
		System.out.println("{引起异常信息：" + message + "}");
	}

	public SelfException(Throwable cause) {
		super(cause);
		System.out.println("{引起异常原因：" + cause + "}");
	}

}
