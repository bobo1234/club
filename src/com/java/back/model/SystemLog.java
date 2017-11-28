package com.java.back.model;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.GenericGenerator;

/**
 * 系统日志 表
 * 
 * @author gyb
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "te_systemlog")
public class SystemLog extends BaseBean {
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String id;

	private String description;// 描述

	private String method;// 方法

	private int logType;// 操作类型

	private String requestIp;// ip

	private String exceptioncode;

	private String exceptionDetail;

	private String params;

	private Long createByUser;// 操作人id
	
	private String createByUserName;// 操作人名称

	private String createDate;// 时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public int getLogType() {
		return logType;
	}


	public void setLogType(int logType) {
		this.logType = logType;
	}


	public String getRequestIp() {
		return requestIp;
	}


	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}


	public String getExceptioncode() {
		return exceptioncode;
	}


	public void setExceptioncode(String exceptioncode) {
		this.exceptioncode = exceptioncode;
	}


	public String getExceptionDetail() {
		return exceptionDetail;
	}


	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}


	public String getParams() {
		return params;
	}


	public void setParams(String params) {
		this.params = params;
	}


	public Long getCreateByUser() {
		return createByUser;
	}


	public void setCreateByUser(Long createByUser) {
		this.createByUser = createByUser;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getCreateByUserName() {
		return createByUserName;
	}

	public void setCreateByUserName(String createByUserName) {
		this.createByUserName = createByUserName;
	}

	public SystemLog() {
		super();
	}

}