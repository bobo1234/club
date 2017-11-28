package com.java.back.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.dao.CityDao;
import com.java.back.dao.CountyDao;
import com.java.back.dao.ProvinceDao;
import com.java.back.dao.TownshipDao;
import com.java.back.dao.VillageDao;
import com.java.back.dto.AddressDto;
import com.java.back.service.AddressService;
import com.java.back.support.JSONReturn;

@Scope
@Service
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private CityDao cityDao;
	@Autowired
	private CountyDao countyDao;
	@Autowired
	private TownshipDao townshipDao;
	@Autowired
	private VillageDao villageDao;

	public JSONReturn findProvinceAll() {
		// TODO Auto-generated method stub
		List<AddressDto> dtoList = provinceDao.findAllProvince();
		if (CollectionUtils.isEmpty(dtoList))
			return JSONReturn.buildFailure("未获取到数据!");
		return JSONReturn.buildSuccess(dtoList);
	}

	public JSONReturn findCityByProvinceId(long provinceId) {
		// TODO Auto-generated method stub
		List<AddressDto> dtoList = cityDao.findCityByProvinceId(provinceId);
		if (CollectionUtils.isEmpty(dtoList))
			return JSONReturn.buildFailure("未获取到数据!");
		return JSONReturn.buildSuccess(dtoList);
	}

	public JSONReturn findCountyByCityId(long cityId) {
		// TODO Auto-generated method stub
		List<AddressDto> dtoList = countyDao.findCountyByCityId(cityId);
		if (CollectionUtils.isEmpty(dtoList))
			return JSONReturn.buildFailure("未获取到数据!");
		return JSONReturn.buildSuccess(dtoList);
	}

	public JSONReturn findTownshipByCountyId(long countyId) {
		// TODO Auto-generated method stub
		List<AddressDto> dtoList = townshipDao.findTownshipByCountyId(countyId);
		if (CollectionUtils.isEmpty(dtoList))
			return JSONReturn.buildFailure("未获取到数据!");
		return JSONReturn.buildSuccess(dtoList);
	}

	public JSONReturn findVillageByTownshipId(long towhshipId) {
		// TODO Auto-generated method stub
		List<AddressDto> dtoList = villageDao.findVillageByTownshipId(towhshipId);
		if (CollectionUtils.isEmpty(dtoList))
			return JSONReturn.buildFailure("未获取到数据!");
		return JSONReturn.buildSuccess(dtoList);
	}

}
