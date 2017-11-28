package com.java.back.support;

import java.io.Serializable;

public class JSONReturn implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * JSON头
	 */
	private boolean head;

	/**
	 * 其他类型弹窗的JSON头
	 */
	private String type;

	/*
	 * JSON主体
	 */
	private Object body;

	/*
	 * 返回头
	 */
	public static final boolean HEAD_SUCCESS = true;
	public static final boolean HEAD_FAILURE = false;

	public static final String TYPE_SUCCESS = "type-success";
	public static final String TYPE_INFO = "type-info";
	public static final String TYPE_WARNING = "type-warning";
	public static final String TYPE_DANGER = "type-danger";

	// * 有关闭按钮的弹窗 (BootstrapDialog.TYPE_INFO or 'type-info'
	// * BootstrapDialog.TYPE_PRIMARY or 'type-primary' (default)
	// * BootstrapDialog.TYPE_SUCCESS or 'type-success'
	// BootstrapDialog.TYPE_WARNING
	// * or 'type-warning' BootstrapDialog.TYPE_DANGER or 'type-danger')

	/**
	 * 返回其他格式的弹窗消息
	 * 
	 * @param TYPE
	 * @param body
	 * @return
	 */
	public static JSONReturn buildOther(boolean head,String type, Object body) {
		return new JSONReturn(head, type, body);
	}

	/*
	 * 返回空主体
	 */
	private static final String EMPTY_BODY = "系统内部错误";

	public JSONReturn() {
		// TODO Auto-generated constructor stub
	}

	public boolean isHead() {
		return head;
	}

	public void setHead(boolean head) {
		this.head = head;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public JSONReturn(boolean head, Object body) {
		this.head = head;
		this.body = body;
	}

	public JSONReturn(boolean head, String type, Object body) {
		this.head = head;
		this.type = type;
		this.body = body;
	}

	public static JSONReturn build(boolean head, Object body) {
		return new JSONReturn(head, body);
	}

	/*
	 * 成功
	 */
	public static JSONReturn buildSuccess(Object body) {
		return build(HEAD_SUCCESS, body);
	}

	/*
	 * 失败
	 */
	public static JSONReturn buildFailure(Object body) {
		return build(HEAD_FAILURE, body);
	}

	/*
	 * 成功,空主体
	 */
	public static JSONReturn buildSuccessWithEmptyBody() {
		return build(HEAD_SUCCESS, EMPTY_BODY);
	}

	/*
	 * 失败,空主体
	 */
	public static JSONReturn buildFailureWithEmptyBody() {
		return build(HEAD_FAILURE, EMPTY_BODY);
	}

	/*
	 * 操作失败
	 */
	public static JSONReturn buildFailure() {
		return build(HEAD_FAILURE, "操作失败");
	}
}
