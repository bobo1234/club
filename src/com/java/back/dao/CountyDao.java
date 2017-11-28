package com.java.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.dto.AddressDto;
import com.java.back.model.TeCounty;

@Repository
public class CountyDao extends AbstractDao<TeCounty> {

	@Override
	public Class<TeCounty> getEntityClass() {
		// TODO Auto-generated method stub
		return TeCounty.class;
	}

	@SuppressWarnings("unchecked")
	public List<AddressDto> findCountyByCityId(long cityId) {
		// TODO Auto-generated method stub
		String query = "select new com.java.back.dto.AddressDto(countyId as id, countyName as name) from TeCounty where cityId = ?";
		return findSession().createQuery(query).setLong(0, cityId).list();
	}

}
