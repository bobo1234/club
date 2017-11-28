package com.java.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.dto.AddressDto;
import com.java.back.model.TeCity;

@Repository
public class CityDao extends AbstractDao<TeCity> {

	@Override
	public Class<TeCity> getEntityClass() {
		// TODO Auto-generated method stub
		return TeCity.class;
	}

	@SuppressWarnings("unchecked")
	public List<AddressDto> findCityByProvinceId(long provinceId) {
		String query = "select new com.java.back.dto.AddressDto(cityId as id, cityName as name) from TeCity where provinceId = ?";
		return findSession().createQuery(query).setLong(0, provinceId).list();
	}

}
