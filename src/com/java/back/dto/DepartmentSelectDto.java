package com.java.back.dto;

import java.io.Serializable;

/**
 * 部门下拉框
 *   
 * @author Kiro
 */
public class DepartmentSelectDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;

	public DepartmentSelectDto() {
		// TODO Auto-generated constructor stub
	}

	public DepartmentSelectDto(long deptId, String deptName) {
		super();
		this.id = deptId;
		this.name = deptName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
