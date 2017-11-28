package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeChoosePolitics;

@Repository
public class ChoosePoliticsDao extends AbstractDao<TeChoosePolitics> {

	@Override
	public Class<TeChoosePolitics> getEntityClass() {
		// TODO Auto-generated method stub
		return TeChoosePolitics.class;
	}

}
