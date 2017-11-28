package com.java.back.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.PageConstant;
import com.java.back.constant.TimeFormatConstant;
import com.java.back.dao.DepartmentDao;
import com.java.back.dao.EmployeesBasicDao;
import com.java.back.dao.PositionDao;
import com.java.back.dto.PositionDto;
import com.java.back.dto.PositionListDto;
import com.java.back.field.TeDepartmentField;
import com.java.back.field.TePositionField;
import com.java.back.model.TeDepartment;
import com.java.back.model.TePosition;
import com.java.back.service.PositionService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.DateTimeUtil;
import com.java.back.utils.PageUtils;

@Scope
@Service
@Transactional(readOnly = true)
public class PositionServiceImpl implements PositionService {

	@Autowired
	private PositionDao positionDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private EmployeesBasicDao employeesBasicDao;

	public JSONReturn findPositionListInfo(int page, long deptId, String searchValue, String acctName) {
		// TODO Auto-generated method stub
		List<PositionListDto> dtoList = positionDao.findPositionListInfo(page, deptId, searchValue);
		if (CollectionUtils.isEmpty(dtoList))
			return JSONReturn.buildFailure("未获取到数据!");
		for (PositionListDto dto : dtoList)
			dto.setTime(DateTimeUtil.conversionTime(dto.getDate(), TimeFormatConstant.YYYY_MM_DD));//转换时间
		return JSONReturn.buildSuccess(dtoList);
	}

	@Transactional
	public JSONReturn addPosition(long deptId, String name, String desc, String acctName) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(name))
			return JSONReturn.buildFailure("添加失败,名称为空!");
		TeDepartment dept = departmentDao.findUniqueByProperty(TeDepartmentField.DEPT_ID, deptId);
		if (CompareUtil.isEmpty(dept))
			return JSONReturn.buildFailure("添加失败, 部门不存在!");
		positionDao.save(new TePosition(deptId, name, desc, acctName, DateTimeUtil.getCurrentTime()));
		return JSONReturn.buildSuccess("职位添加成功!");
	}

	public JSONReturn findPositionPage(int page, long deptId, String searchValue, String acctName) {
		// TODO Auto-generated method stub
		int count = positionDao.findPositionPage(page, deptId, searchValue);
		return JSONReturn.buildSuccess(PageUtils.calculatePage(page, count, PageConstant.PAGE_LIST));
	}

	@Transactional
	public JSONReturn deletePosition(long id, String acctName) {
		// TODO Auto-generated method stub
		employeesBasicDao.chearEmplPositionByPositionId(id); // 清除员工部门信息
		positionDao.deleteByProperty(TePositionField.PO_ID, id);
		return JSONReturn.buildSuccess("删除成功!");
	}

	public JSONReturn findPositionById(long id) {
		// TODO Auto-generated method stub
		TePosition position = positionDao.findUniqueByProperty(TePositionField.PO_ID, id);
		if (CompareUtil.isEmpty(position))
			return JSONReturn.buildFailure("源数据不存在!");
		return JSONReturn.buildSuccess(position);
	}

	@Transactional
	public JSONReturn modifyPosition(long id, long deptId, String name, String desc, String acctName) {
		// TODO Auto-generated method stub
		TePosition position = positionDao.findUniqueByProperty(TePositionField.PO_ID, id);
		if (CompareUtil.isEmpty(position))
			return JSONReturn.buildFailure("源数据不存在!");
		TeDepartment dept = departmentDao.findUniqueByProperty(TeDepartmentField.DEPT_ID, deptId);
		if (CompareUtil.isEmpty(dept))
			return JSONReturn.buildFailure("修改失败, 部门不存在!");
		position.setPoName(name);
		position.setPoDescription(desc);
		position.setPoDepartment(deptId);
		return JSONReturn.buildSuccess("修改成功!");
	}

	public JSONReturn findPositionByDeptId(long deptId) {
		// TODO Auto-generated method stub
		List<PositionDto> list = positionDao.findByDeptId(deptId);
		if (CollectionUtils.isEmpty(list))
			return JSONReturn.buildFailure("该部门下不存在职位!");
		return JSONReturn.buildSuccess(list);
	}
}
