package com.java.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.SystemLog;
import com.java.back.utils.StringUtil;
@Repository
public class SystemLogDao extends AbstractDao<SystemLog> {

	@Override
	public Class<SystemLog> getEntityClass() {
		// TODO Auto-generated method stub
		return SystemLog.class;
	}

	public List<SystemLog> queryHqlForList(int type,String beginTime,String endTime,Long acctId,int page,String des) {
		// TODO Auto-generated method stub
		StringBuffer hql=new StringBuffer("from SystemLog where 1=1 ");
		hql.append(type>0? " and logType ="+type:"");
		hql.append(!StringUtil.isEmpty(acctId) ? " and createByUser ="+acctId:"");
		hql.append(!StringUtil.isEmpty(des) ? " and description "+StringUtil.formatLike(des):"");
		hql.append(!StringUtil.isEmpty(beginTime) &&!StringUtil.isEmpty(endTime)? StringUtil.formatInterDate("createDate", beginTime, endTime):"");
		hql.append(" order by createDate desc");
		return super.queryHqlForList(hql.toString(),null, page);
	}
	
	public int findLogPage(int type,String beginTime,String endTime,Long acctId,String des) {
		// TODO Auto-generated method stub
		StringBuffer hql=new StringBuffer("SELECT COUNT(*) FROM TE_SYSTEMLOG WHERE 1=1 ");
		hql.append(type>0? " and LOGTYPE ="+type:"");
		hql.append(!StringUtil.isEmpty(des) ? " and description "+StringUtil.formatLike(des):"");
		hql.append(!StringUtil.isEmpty(acctId) ? " and CREATEBYUSER ="+acctId:"");
		hql.append(!StringUtil.isEmpty(beginTime) &&!StringUtil.isEmpty(endTime)? StringUtil.formatInterDate("createDate", beginTime, endTime):"");
		return countAll(hql.toString());
	}
}
