package com.java.back.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.back.annotation.SecureValid;
import com.java.back.constant.MethodType;
import com.java.back.controller.support.AbstractController;
import com.java.back.service.SystemLogService;
import com.java.back.support.JSONReturn;

@Scope
@Controller
public class LogController extends AbstractController {

	@Autowired
	private SystemLogService systemLogService;
	/**
	 * 分页显示
	 * @param page
	 * @param Logtype
	 * @param beginTime
	 * @param endTime
	 * @param acctId
	 * @param des
	 * @param httpSession
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findLogListInfo")
	public JSONReturn findLogListInfo(@RequestParam int page,
			@RequestParam int Logtype, @RequestParam String beginTime,
			@RequestParam String endTime, @RequestParam Long acctId,@RequestParam String des,
			HttpSession httpSession) {
		return systemLogService.findLogList(Logtype, beginTime, endTime, acctId,
				page,des);
	}

	@ResponseBody
	@RequestMapping(value = "findLogPage")
	public JSONReturn findLogPage(@RequestParam int page,
			@RequestParam int Logtype, @RequestParam String beginTime,
			@RequestParam String endTime, @RequestParam Long acctId,@RequestParam String des,
			HttpSession httpSession) {
		return systemLogService.findLogCount(Logtype, beginTime, endTime, acctId, page,des);
	}

	@ResponseBody
	@RequestMapping(value = "deleteLog")
	@SecureValid(code = "05003", desc = "删除日志", type = MethodType.DELETE)
	public JSONReturn deleteLog(@RequestParam String id) {
		return systemLogService.deleteLog(id);
	}
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findLogById")
	public JSONReturn findLogById(@RequestParam String id) {
		return systemLogService.findLogById(id);
	}

}
