package com.weaver.rpa.tender.search.util;

import java.io.Serializable;

/**
 * 返回值包装类
 *
 * @author pengyonglei
 * @version 1.0.0
 */
public class ResponseModel<T> implements Serializable{

	private static final long serialVersionUID = -3364729785649603324L;
	/**
	 * 成功返回的数据
	 */
	private T data;
	/**
	 * 是否成功
	 */
	private boolean isSuccess;
	/**
	 * 错误码
	 */
	private Integer errorCode;
	/**
	 * 错误信息
	 */
	private String errorMsg;

	/**
	 * 分页信息
	 */
	private Pagination pagination;

	public ResponseModel() {

	}

	public ResponseModel(T data) {
		this.isSuccess = true;
		this.data = data;
	}

	public ResponseModel(Integer errorCode, String errorMsg) {
		this.isSuccess = false;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean success) {
		isSuccess = success;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
