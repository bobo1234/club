package com.java.back.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.java.back.constant.MethodType;
import com.java.back.controller.support.AbstractController;
import com.java.back.model.SystemLog;
import com.java.back.service.SystemLogService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.DateUtil;
import com.java.back.utils.IpUtil;
import com.java.back.utils.JsonUtil;

@ControllerAdvice
public class ExceptionIntercept extends AbstractController{

	@Autowired
	private SystemLogService systemLogService;

	/**
	 * spring捕捉到的异常 在此进行处理
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler({ Exception.class })
	public JSONReturn exception(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {
		ex.printStackTrace();
		SystemLog log=new SystemLog();
		log.setExceptionDetail(ex.getMessage());
		log.setLogType(MethodType.EXCEPTION.getId());
		log.setCreateDate(DateUtil.getAllDate());
		log.setRequestIp(IpUtil.getIpAddr(request));
		String url=request.getRequestURL().toString();
		Map<String, String[]> map = request.getParameterMap();
		String params=JsonUtil.mapTojson(map);
		log.setDescription("服务器发生异常");
		log.setParams(params);
		log.setCreateByUser(acctId(request.getSession()));
		log.setCreateByUserName(acctName(request.getSession()));
		log.setMethod(url.substring(url.lastIndexOf("/")+1, url.length()));
		systemLogService.addLog(log);
		return JSONReturn.buildFailure("服务器错误!");
	}

	/**
	 * 用于处理异常的
	 * 
	 * @return
	 */
	// @ExceptionHandler({ Exception.class })
	// public ModelAndView exception(Exception e) {
	// ModelAndView modelAndView = new ModelAndView();
	// System.out.println("错误信息--------------:" + e.getMessage());
	// modelAndView.addObject("mess", e.getMessage());
	// modelAndView.setViewName("error/error");
	// return modelAndView;
	// }
}
