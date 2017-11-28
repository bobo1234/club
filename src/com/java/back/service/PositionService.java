package com.java.back.service;

import com.java.back.support.JSONReturn;

public abstract interface PositionService {

	public abstract JSONReturn findPositionListInfo(int page, long deptId, String searchValue, String acctName);

	public abstract JSONReturn addPosition(long deptId, String name, String desc, String acctName);

	public abstract JSONReturn findPositionPage(int page, long deptId, String searchValue, String acctName);

	public abstract JSONReturn deletePosition(long id, String acctName);

	public abstract JSONReturn findPositionById(long id);

	public abstract JSONReturn modifyPosition(long id, long deptId, String name, String desc, String acctName);

	public abstract JSONReturn findPositionByDeptId(long deptId);

}
