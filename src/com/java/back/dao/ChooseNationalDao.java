package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeChooseNational;

@Repository
public class ChooseNationalDao extends AbstractDao<TeChooseNational> {

	@Override
	public Class<TeChooseNational> getEntityClass() {
		// TODO Auto-generated method stub
		return TeChooseNational.class;
	}

}
