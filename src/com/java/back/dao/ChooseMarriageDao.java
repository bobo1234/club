package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeChooseMarriage;

@Repository
public class ChooseMarriageDao extends AbstractDao<TeChooseMarriage> {

	@Override
	public Class<TeChooseMarriage> getEntityClass() {
		// TODO Auto-generated method stub
		return TeChooseMarriage.class;
	}

}
