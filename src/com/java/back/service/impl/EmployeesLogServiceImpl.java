package com.java.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.java.back.dao.EmployeesLogDao;
import com.java.back.model.TeEmployeesLog;
import com.java.back.service.EmployeesLogService;
import com.java.back.utils.DateTimeUtil;

@Scope
@Service
public class EmployeesLogServiceImpl implements EmployeesLogService {

	@Autowired
	private EmployeesLogDao employeesLogDao;

	public void save(long emplId, String acctName, int type, String note) {
		// TODO Auto-generated method stub
		employeesLogDao.save(new TeEmployeesLog(emplId, type, note, DateTimeUtil.getCurrentTime(), acctName));
	}
}
