package com.java.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.dto.AddressDto;
import com.java.back.model.TeTownship;

@Repository
public class TownshipDao extends AbstractDao<TeTownship> {

	@Override
	public Class<TeTownship> getEntityClass() {
		// TODO Auto-generated method stub
		return TeTownship.class;
	}

	@SuppressWarnings("unchecked")
	public List<AddressDto> findTownshipByCountyId(long countyId) {
		// TODO Auto-generated method stub
		String query = "select new com.java.back.dto.AddressDto(townshipId as id, townshipName as name) from TeTownship where countyId = ?";
		return findSession().createQuery(query).setLong(0, countyId).list();
	}

}
