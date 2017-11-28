package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeChooseEducation;

@Repository
public class ChooseEducationDao extends AbstractDao<TeChooseEducation> {

	@Override
	public Class<TeChooseEducation> getEntityClass() {
		// TODO Auto-generated method stub
		return TeChooseEducation.class;
	}

}
