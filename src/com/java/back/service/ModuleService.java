package com.java.back.service;

import java.util.List;

import com.java.back.constant.MethodType;
import com.java.back.model.TeModule;
import com.java.back.support.JSONReturn;

public abstract interface ModuleService {
	/**
	 * 查询一二级菜单
	 * @param acctName
	 * @return
	 */
	public abstract JSONReturn findMenu(String acctName);
	/**
	 * 获取页面权限
	 * @param moduleCode
	 * @param acctName
	 * @return
	 */
	public abstract JSONReturn findModuleParameter(String moduleCode, String acctName);

	public abstract JSONReturn findBreadcrumb(String moduleCode);
	
	/**
	 * 根据角色id勾选对应的菜单(模块权限设置之前展示的数据)
	 * @param roleId
	 * @return
	 */
	public abstract JSONReturn findAllModule(long roleId);
	
	/**
	 * 给角色设置权限菜单
	 * @param rold
	 * @param code
	 * @param type
	 * @param add
	 * @return
	 */
	public abstract JSONReturn setRoleSecureValid(long rold, String code, int type, boolean add);
	/**
	 * 判断用户是否有权限
	 * @param userName
	 * @param code
	 * @param type
	 * @return
	 */
	public abstract boolean secureValid(String userName, String[] code, MethodType type);
	/**
	 * 新增菜单
	 * @param module
	 * @return
	 */
	public abstract JSONReturn saveModule(TeModule module);
	/**
	 * 删除菜单
	 * @param id
	 * @return
	 */
	public abstract JSONReturn deleteModule(String id);
	/**
	 * 修改菜单
	 * @param module
	 * @return
	 */
	public abstract JSONReturn updateModule(TeModule module);
	/**
	 * 获取所有菜单列表
	 * @return
	 */
	public abstract List<TeModule> getAllMenu();
	/**
	 * 查看单个菜单信息
	 * @param id
	 * @return
	 */
	public abstract JSONReturn findByid(Long id);
	/**
	 * 根据url查询code
	 * @param page
	 * @return
	 */
	public JSONReturn findByPage(String page);
	
}
