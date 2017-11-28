package com.java.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeRoleModule;

@Repository
public class RoleModuleDao extends AbstractDao<TeRoleModule> {

	@Override
	public Class<TeRoleModule> getEntityClass() {
		// TODO Auto-generated method stub
		return TeRoleModule.class;
	}

	public TeRoleModule findByRoleLabelByModuleCode(String roleLabel, String moduleCode) {
		// TODO Auto-generated method stub
		StringBuffer query = new StringBuffer();
		query.append("from TeRoleModule where roleLabel = ? and moduleCode = ?");
		return (TeRoleModule) findSession().createQuery(query.toString()).setString(0, roleLabel)
				.setString(1, moduleCode).uniqueResult();
	}

	/**
	 * 根据用户名称与菜单code计算是否有当前菜单的权限(进入页面时候计算权限)
	 * @param moduleCode
	 * @param acctName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TeRoleModule> findMySecureValid(String[] moduleCode, String acctName) {
		// TODO Auto-generated method stub
		StringBuffer query = new StringBuffer();
		query.append("from TeRoleModule trm where exists");
		query.append("(from TeAccountRole tar where tar.roleLabel = trm.roleLabel and tar.acctName = ?) and ");
		query.append("trm.moduleCode in (" + analysisModuleArray(moduleCode) + ")");
		return findSession().createQuery(query.toString()).setString(0, acctName).list();
	}

	public String analysisModuleArray(String[] modules) {
		if (modules.length < 1)
			return "";
		if (modules.length == 1)
			return "'" + modules[0] + "'";
		String codes = "";
		for (String str : modules)
			codes += "'" + str + "',";
		return codes.substring(0, codes.length() - 1);
	}
}
