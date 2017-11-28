package com.java.back.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.PageConstant;
import com.java.back.dao.SystemLogDao;
import com.java.back.model.SystemLog;
import com.java.back.service.SystemLogService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.DateUtil;
import com.java.back.utils.PageUtils;

@Service
@Transactional
public class SystemLogServiceImpl implements SystemLogService {

	@Autowired
	private SystemLogDao systemLogDao;

	public JSONReturn findLogList(int type, String beginTime, String endTime,
			Long acctId, int page, String des) {
		// TODO Auto-generated method stub
		List<SystemLog> list = systemLogDao.queryHqlForList(type, beginTime,
				endTime, acctId, page, des);
		return JSONReturn.buildSuccess(list);
	}

	public void addLog(SystemLog log) {
		// TODO Auto-generated method stub
		log.setCreateDate(DateUtil.getAllDate());
		systemLogDao.save(log);
	}

	public JSONReturn deleteLog(String id) {
		// TODO Auto-generated method stub
		if (systemLogDao.deleteByPropertyString("id", id) > 0) {
			return JSONReturn.buildSuccess("删除成功");
		}
		return JSONReturn.buildFailure("删除失败");
	}

	public JSONReturn findLogById(String id) {
		// TODO Auto-generated method stub
		SystemLog systemLog = systemLogDao.get(id);
		if (CompareUtil.isEmpty(systemLog))
			return JSONReturn.buildFailure("源数据不存在!");
		return JSONReturn.buildSuccess(systemLog);
	}

	public JSONReturn findLogCount(int type, String beginTime, String endTime,
			Long acctId, int page, String des) {
		// TODO Auto-generated method stub
		int count = systemLogDao.findLogPage(type, beginTime, endTime, acctId,
				des);
		return JSONReturn.buildSuccess(PageUtils.calculatePage(page, count,
				PageConstant.PAGE_LIST));
	}
}
