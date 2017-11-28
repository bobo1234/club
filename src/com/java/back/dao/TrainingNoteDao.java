package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.TeTrainingNote;

@Repository
public class TrainingNoteDao extends AbstractDao<TeTrainingNote> {

	@Override
	public Class<TeTrainingNote> getEntityClass() {
		// TODO Auto-generated method stub
		return TeTrainingNote.class;
	}

}
