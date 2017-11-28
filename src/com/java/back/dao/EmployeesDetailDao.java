package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeEmployeesDetail;

@Repository
public class EmployeesDetailDao extends AbstractDao<TeEmployeesDetail> {

	@Override
	public Class<TeEmployeesDetail> getEntityClass() {
		// TODO Auto-generated method stub
		return TeEmployeesDetail.class;
	}

}
