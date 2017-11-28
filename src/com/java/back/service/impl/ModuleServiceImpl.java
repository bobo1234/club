package com.java.back.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.LoginState;
import com.java.back.constant.MethodType;
import com.java.back.dao.AccountDao;
import com.java.back.dao.ModuleDao;
import com.java.back.dao.RoleDao;
import com.java.back.dao.RoleModuleDao;
import com.java.back.dto.ModuleDto;
import com.java.back.field.TeAccountField;
import com.java.back.field.TeModuleField;
import com.java.back.field.TeRoleField;
import com.java.back.model.TeAccount;
import com.java.back.model.TeModule;
import com.java.back.model.TeRole;
import com.java.back.model.TeRoleModule;
import com.java.back.service.ModuleService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.DateUtil;
import com.java.back.utils.StringUtil;

@Scope
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	private ModuleDao moduleDAO;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private RoleModuleDao roleModuleDao;
	@Autowired
	private RoleDao roleDao;

	public static final int TYPE_FIND = 1;
	public static final int TYPE_DELETE = 2;
	public static final int TYPE_MDOIFY = 3;
	public static final int TYPE_ADD = 4;

	@Cacheable(value = "findMenu", key = "'findMenu:'+#acctName",condition="2%2==0")
	public JSONReturn findMenu(String acctName) {
		// TODO Auto-generated method stub
		System.out.println("======菜单缓存======"+acctName);
		TeAccount teAccount = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_NAME, acctName);
		if (CompareUtil.isEmpty(teAccount))
			return JSONReturn.buildFailure(LoginState.UNLOGIN);
		List<TeModule> moduleList = null;
//		if (teAccount.getAcctSuper())
//			moduleList=moduleDAO.findAllByOrderAsc("timestamp");
//			moduleList = moduleDAO.findAll();
//		else
			moduleList = moduleDAO.findMgrModule(acctName);
		if (CollectionUtils.isEmpty(moduleList))
			return JSONReturn.buildFailure("获取菜单失败!");
		return JSONReturn.buildSuccess(moduleList);
	}
	
	@Cacheable(value = "findModuleParameter", key = "#acctName+':'+#moduleCode")
	public JSONReturn findModuleParameter(String moduleCode, String acctName) {
		// TODO Auto-generated method stub
		System.out.println("======页面按钮缓存======"+acctName);
		Map<String, Object> map = new HashMap<String, Object>();
		TeAccount teAccount = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_NAME, acctName);
		if (CompareUtil.isEmpty(teAccount))
			return JSONReturn.buildFailure(map);
		map.put("acctount", teAccount.getAcctNickname());
		if ("0".equals(moduleCode)) {
			map.put("find", true);
			map.put("add", teAccount.getAcctSuper() ? true : false);
			map.put("del", teAccount.getAcctSuper() ? true : false);
			map.put("modify", teAccount.getAcctSuper() ? true : false);
			return JSONReturn.buildSuccess(map);
		}
		TeModule teModule = moduleDAO.findUniqueByProperty(
				TeModuleField.MODULE_CODE, moduleCode);
		if (CompareUtil.isEmpty(teModule))
			return JSONReturn.buildFailure("非法操作!");
		map.put("moduleName", teModule.getModuleName());
		// 获取当前模块名与上级模块名, 前台做为面包绡显示
		map = findModuleName(map, moduleCode, teModule);
		boolean add = false, del = false, modify = false, find = false;
		List<TeRoleModule> secureValidList = roleModuleDao.findMySecureValid(
				findModuleArray(moduleCode), acctName);
		if (!teAccount.getAcctSuper()
				&& CollectionUtils.isNotEmpty(secureValidList)) {
			for (TeRoleModule rm : secureValidList) {
				if (rm.getAdds())
					add = rm.getAdds();
				if (rm.getDeletes())
					del = rm.getDeletes();
				if (rm.getModifys())
					modify = rm.getModifys();
				if (rm.getFinds())
					find = rm.getFinds();
			}
		}
		map.put("add", teAccount.getAcctSuper() ? true : add);
		map.put("del", teAccount.getAcctSuper() ? true : del);
		map.put("modify", teAccount.getAcctSuper() ? true : modify);
		map.put("find", teAccount.getAcctSuper() ? true : find);
		return JSONReturn.buildSuccess(map);
	}

	/*
	 * 获取当前模块名称以及上级模块名称
	 */
	public Map<String, Object> findModuleName(Map<String, Object> map,
			String moduleCode, TeModule teModule) {
		teModule = moduleDAO.findUniqueByProperty(TeModuleField.MODULE_CODE,
				teModule.getModuleSuperCode());
		if (CompareUtil.isNotEmpty(teModule)) {
			map.put("superModuleName", teModule.getModuleName());
			map.put("code", teModule.getModuleCode());
		}
		return map;
	}

	public JSONReturn findBreadcrumb(String moduleCode) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		TeModule teModule = moduleDAO.findUniqueByProperty(
				TeModuleField.MODULE_CODE, moduleCode);
		if (CompareUtil.isEmpty(teModule))
			return JSONReturn.buildFailure("ERROR");
		TeModule superModule = moduleDAO.findUniqueByProperty(
				TeModuleField.MODULE_CODE, teModule.getModuleSuperCode());
		if (CompareUtil.isEmpty(superModule))
			return JSONReturn.buildFailure("ERROR");
		map.put("superName", superModule.getModuleName());
		map.put("name", teModule.getModuleName());
		map.put("code", teModule.getModuleSuperCode());
		return JSONReturn.buildSuccess(map);
	}

	public JSONReturn findAllModule(long roleId) {
		// TODO Auto-generated method stub
		TeRole teRole = roleDao.findUniqueByProperty(TeRoleField.ROLE_ID,
				roleId);
		if (CompareUtil.isEmpty(teRole))
			return JSONReturn.buildFailure("未获取到模块信息!");
		List<TeModule> moduleList = moduleDAO.findAll();
		if (CollectionUtils.isEmpty(moduleList))
			return JSONReturn.buildFailure("未获取到相关数据!");
		List<ModuleDto> infoList = new ArrayList<ModuleDto>();
		TeRoleModule tm = null;
		for (TeModule mo : moduleList) {
			ModuleDto dto = new ModuleDto();
			dto.setId(mo.getModuleId());
			dto.setName(mo.getModuleName());
			dto.setCode(mo.getModuleCode());
			dto.setSuperCode(mo.getModuleSuperCode());
			dto.setUrl(mo.getModulePage());
			dto.setLevel(mo.getModuleLevel());
			tm = roleModuleDao.findByRoleLabelByModuleCode(
					teRole.getRoleLabel(), mo.getModuleCode());
			if (CompareUtil.isNotEmpty(tm)) {
				dto.setAdd(tm.getAdds());
				dto.setDel(tm.getDeletes());
				dto.setModify(tm.getModifys());
				dto.setFind(tm.getFinds());
			}
			infoList.add(dto);
		}
		return JSONReturn.buildSuccess(infoList);
	}
	
	/**
	 * 给角色设置权限菜单
	 */
	@CacheEvict(value = { "findMenu" ,"findModuleParameter"}, allEntries = true)
	@Transactional
	public JSONReturn setRoleSecureValid(long rold, String code, int type,
			boolean add) {
		// TODO Auto-generated method stub
		TeModule mo = moduleDAO.findUniqueByProperty(TeModuleField.MODULE_CODE,
				code);
		if (CompareUtil.isEmpty(mo))
			return JSONReturn.buildFailure("操作失败, 模块不存在!");
		TeRole role = roleDao.findUniqueByProperty(TeRoleField.ROLE_ID, rold);
		if (CompareUtil.isEmpty(role))
			return JSONReturn.buildFailure("操作失败, 角色不存在!");
		TeRoleModule rm = roleModuleDao.findByRoleLabelByModuleCode(
				role.getRoleLabel(), mo.getModuleCode());
		if (CompareUtil.isEmpty(rm)) {
			rm = new TeRoleModule(role.getRoleLabel(), code,
					mo.getModuleSuperCode(), false, false, false, false);
			roleModuleDao.save(rm);
		}
		rm = setRoleSecureValid(rm, type, add);
		return JSONReturn.buildSuccessWithEmptyBody();
	}

	public TeRoleModule setRoleSecureValid(TeRoleModule rm, int type,
			boolean add) {
		if (type == TYPE_FIND)
			rm.setFinds(add);
		else if (type == TYPE_DELETE)
			rm.setDeletes(add);
		else if (type == TYPE_ADD)
			rm.setAdds(add);
		else
			rm.setModifys(add);
		return rm;
	}

	public boolean secureValid(String userName, String[] code, MethodType type) {
		// TODO Auto-generated method stub
		List<TeRoleModule> secureValidList = roleModuleDao.findMySecureValid(
				code, userName);
		if (CollectionUtils.isEmpty(secureValidList))
			return false;
		for (TeRoleModule te : secureValidList) {
			if (type == MethodType.FIND && te.getFinds())
				return true;
			else if (type == MethodType.ADD && te.getAdds())
				return true;
			else if (type == MethodType.MODIFY && te.getModifys())
				return true;
			else if (type == MethodType.DELETE && te.getDeletes())
				return true;
		}
		return false;
	}

	public String[] findModuleArray(String moduleCode) {
		return new String[] { moduleCode };
	}

	/**
	 * 计算id
	 * 
	 * @param sql
	 * @return
	 */
	private Object getMaxId(String sql) {
		Object object = moduleDAO.findSession().createSQLQuery(sql)
				.uniqueResult();
		return object;
	}

	/**
	 * 计算规则code
	 * 
	 * @param i
	 * @param length
	 * @return
	 */
	private String getfomatNum(int i, int length) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMinimumIntegerDigits(length);
		String result = format.format(i).replace(",", "");
		System.out.println(result);
		return result;
	}

	/**
	 * 设置管理员权限
	 * 
	 * @param code
	 * @param spcode
	 */
	private void setQX(String code, String spcode) {
		TeRole role = roleDao
				.findUniqueByProperty(TeRoleField.ROLE_NAME, "管理员");
		TeRoleModule rm = new TeRoleModule(role.getRoleLabel(), code, spcode,
				true, true, true, true);
		roleModuleDao.save(rm);
	}
	
	// allEntries是boolean类型，表示是否需要清除缓存中的所有元素。
	@CacheEvict(value = { "findMenu" ,"findModuleParameter"}, allEntries = true)
	public JSONReturn saveModule(TeModule module) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(getMaxId(
				"select max(moduleId) from te_module ").toString());
		module.setModuleId(id + 1);
		System.out.println("菜单id:" + (id + 1));
		if (module.getModuleLevel().equals(0)) {// 一级菜单
			module.setModuleSuperCode("0");
			Object object = getMaxId("select max(moduleCode) from te_module where moduleLevel=0");
			if (object != null) {
				id = Integer.parseInt(object.toString());
			}
			module.setModuleCode(getfomatNum(id + 1, 2));
		} else {// 二级菜单
			int i = moduleDAO.findCountByProperty("modulePage", module.getModulePage());
			if (i>0) {
				return JSONReturn.buildFailure("新增失败,菜单地址已存在!");
			}
			Object object = getMaxId("select max(moduleCode) from te_module where moduleSuperCode"
					+ StringUtil.formatEqual(module.getModuleSuperCode()));
			if (object == null) {// 没有二级菜单
				String num = getfomatNum(1, 3);
				module.setModuleCode(module.getModuleSuperCode() + num);
			} else {// 存在菜单
				String code = object.toString().substring(
						module.getModuleSuperCode().length(),
						object.toString().length());
				String num = getfomatNum(Integer.parseInt(code) + 1, 3);
				module.setModuleCode(module.getModuleSuperCode() + num);
			}
		}
		module.setTimestamp(DateUtil.StrToTime(DateUtil.getAllDate()));
		try {
			moduleDAO.save(module);
			// 默认给管理员赋予所有权限
			if (module.getModuleLevel() == 1) {
				setQX(module.getModuleCode(), module.getModuleSuperCode());
			}
			return JSONReturn.buildSuccess("新增成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JSONReturn.buildFailure("新增失败");
		}
	}

	// @CacheEvict 注释来标记要清空缓存的方法，当这个方法被调用后，即会清空缓存。注意其中一个
	@CacheEvict(value = { "findMenu" ,"findModuleParameter"}, allEntries = true)
	public JSONReturn deleteModule(String id) {
		// TODO Auto-generated method stub
		TeModule module = moduleDAO.get(Long.parseLong(id));
		String sql = "select count(*) from te_module where moduleCode like'"
				+ module.getModuleCode() + "%' ";
		int countAll = moduleDAO.countAll(sql);
		if (countAll > 1) {
			return JSONReturn.buildFailure("删除失败,存在其下的二级菜单");
		}
		sql = "delete from te_module where moduleId"
				+ StringUtil.formatEqual(id);
		moduleDAO.executeBySql(sql, null);
		sql = "delete from te_role_module where moduleCode"
				+ StringUtil.formatEqual(module.getModuleCode());
		moduleDAO.executeBySql(sql, null);// 删除角色对应的权限菜单
		return JSONReturn.buildSuccess("删除成功");
	}

	@CacheEvict(value = { "findMenu" ,"findModuleParameter"}, allEntries = true)
	public JSONReturn updateModule(TeModule module) {
		// TODO Auto-generated method stub
		try {
			int a = 0;
			String sql = "select count(*) from te_module where modulePage=? and moduleId!=? ";
			a = Integer.parseInt(moduleDAO.findSession().createSQLQuery(sql).setString(0, module.getModulePage()).setLong(1, module.getModuleId()).uniqueResult().toString());
			if (a>0) {
				return JSONReturn.buildFailure("修改失败, "+module.getModulePage()+"已存在!");
			}
			if (module.getModuleLevel() == 1) {
				sql = "UPDATE TE_MODULE SET MODULEPAGE=? , MODULENAME=? WHERE MODULEID=?";
				Object[] params = { module.getModulePage(),
						module.getModuleName(), module.getModuleId() };
				a = moduleDAO.executeBySql(sql, params);
			} else {
				sql = "UPDATE TE_MODULE SET  MODULENAME=? WHERE MODULEID=?";
				Object[] params = { module.getModuleName(),
						module.getModuleId() };
				a = moduleDAO.executeBySql(sql, params);
			}
			if (a > 0) {
				return JSONReturn.buildSuccess("修改成功");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailure("修改失败");
	}
	
//	@Cacheable("getAllMenu")
	public List<TeModule> getAllMenu() {
		// TODO Auto-generated method stub
		List<TeModule> moduleList = moduleDAO.findAllByOrderAsc("timestamp");
		return moduleList;
	}

	public JSONReturn findByid(Long id) {
		// TODO Auto-generated method stub
		TeModule module = moduleDAO.findById(id);
		return JSONReturn.buildSuccess(module);
	}

	public JSONReturn findByPage(String page) {
		// TODO Auto-generated method stub
		try {
			System.out.println("页面url:" + page);
			TeModule teModule = moduleDAO.findUniqueByProperty(
					TeModuleField.MODULE_PAGE, page);
			System.out.println("菜单code:" + teModule.getModuleCode());
			return JSONReturn.buildSuccess(teModule.getModuleCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return JSONReturn.buildFailureWithEmptyBody();
	}

}
