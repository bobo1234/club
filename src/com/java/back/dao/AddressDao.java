package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeAddress;

@Repository
public class AddressDao extends AbstractDao<TeAddress> {

	@Override
	public Class<TeAddress> getEntityClass() {
		// TODO Auto-generated method stub
		return TeAddress.class;
	}

}
