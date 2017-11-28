package com.java.back.service;

import com.java.back.support.JSONReturn;

public abstract interface RoleService {

	public abstract JSONReturn findRoleList(int page, String searchVal, String acctName);

	public abstract JSONReturn findRolePage(int page, String searchVal);

	public abstract JSONReturn addRole(String roleName, String desc, String acctName);

	public abstract JSONReturn modifyRole(long id, String roleName, String desc, String acctName);

	public abstract JSONReturn deleteRole(long id, String acctName);

	public abstract JSONReturn findRoleById(long id);

}
