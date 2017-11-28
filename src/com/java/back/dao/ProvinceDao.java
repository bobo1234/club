package com.java.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.dto.AddressDto;
import com.java.back.model.TeProvince;

@Repository
public class ProvinceDao extends AbstractDao<TeProvince> {

	@Override
	public Class<TeProvince> getEntityClass() {
		// TODO Auto-generated method stub
		return TeProvince.class;
	}

	@SuppressWarnings("unchecked")
	public List<AddressDto> findAllProvince() {
		// TODO Auto-generated method stub
		String query = "select new com.java.back.dto.AddressDto(provinceId as id, provinceName as name) from TeProvince";
		return findSession().createQuery(query).list();
	}

}
