package com.java.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.dto.AddressDto;
import com.java.back.model.TeVillage;

@Repository
public class VillageDao extends AbstractDao<TeVillage> {

	@Override
	public Class<TeVillage> getEntityClass() {
		// TODO Auto-generated method stub
		return TeVillage.class;
	}

	@SuppressWarnings("unchecked")
	public List<AddressDto> findVillageByTownshipId(long towhshipId) {
		// TODO Auto-generated method stub
		String query = "select new com.java.back.dto.AddressDto(villageId as id, villageName as name) from TeVillage where townshipId = ?";
		return findSession().createQuery(query).setLong(0, towhshipId).list();
	}

}
