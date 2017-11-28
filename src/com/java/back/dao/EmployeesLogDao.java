package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.constant.EmployeesLogType;
import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeEmployeesLog;

@Repository
public class EmployeesLogDao extends AbstractDao<TeEmployeesLog> {

	@Override
	public Class<TeEmployeesLog> getEntityClass() {
		// TODO Auto-generated method stub
		return TeEmployeesLog.class;
	}

	public TeEmployeesLog findDepartureNote(long emplId) {
		// TODO Auto-generated method stub
		String query = "from TeEmployeesLog where emplId = ? and type = ? order by id desc";
		return (TeEmployeesLog) findSession().createQuery(query).setLong(0, emplId)
				.setInteger(1, EmployeesLogType.DEPARTURE).setMaxResults(1).uniqueResult();
	}

}
