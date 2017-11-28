package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeEmployeesCompany;

@Repository
public class EmployeesCompanyDao extends AbstractDao<TeEmployeesCompany> {

	@Override
	public Class<TeEmployeesCompany> getEntityClass() {
		// TODO Auto-generated method stub
		return TeEmployeesCompany.class;
	}

}
