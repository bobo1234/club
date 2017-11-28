package com.java.back.service;

import com.java.back.model.SystemLog;
import com.java.back.support.JSONReturn;

public interface SystemLogService {
	/**
	 * 获取列表
	 * 
	 * @param type
	 *            日志类型
	 * @param date
	 *            时间
	 * @param acctId
	 *            用户id
	 * @param page
	 *            条数
	 * @return
	 */
	public JSONReturn findLogList(int type, String beginTime, String endTime,
			Long acctId, int page,String des);

	/**
	 * 新增
	 * 
	 * @param log
	 * @return
	 */
	public void addLog(SystemLog log);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public JSONReturn deleteLog(String id);

	/**
	 * 查看单条数据
	 * 
	 * @param id
	 * @return
	 */
	public JSONReturn findLogById(String id);

	/**
	 * 查询条数
	 * 
	 * @param type
	 * @param beginTime
	 * @param endTime
	 * @param acctId
	 * @param page
	 * @return
	 */
	public JSONReturn findLogCount(int type, String beginTime, String endTime,
			Long acctId, int page,String des);
}
