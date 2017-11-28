package com.java.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeModule;

@Repository
public class ModuleDao extends AbstractDao<TeModule> {

	@Override
	public Class<TeModule> getEntityClass() {
		// TODO Auto-generated method stub
		return TeModule.class;
	}

	@SuppressWarnings("unchecked")
	public List<TeModule> findMgrModule(String acctName) {
		// TODO Auto-generated method stub
		StringBuffer query = new StringBuffer();
		query.append("from TeModule tm where exists ");
		query.append("(from TeAccountRole tar, TeRoleModule trm ");
		query.append("where tar.roleLabel = trm.roleLabel and ");
		query.append("(tm.moduleCode = trm.moduleCode or tm.moduleCode = trm.mosuleSuperCode) ");
		query.append("and trm.finds=1 and tar.acctName = ?)");
		query.append(" order by timestamp desc");
		return findSession().createQuery(query.toString())
				.setString(0, acctName).list();
	}
}
