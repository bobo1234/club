package com.java.back.controller.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.java.back.constant.SessionKey;

public class AbstractController {

	/**
	 * 获取session里用户名称
	 * 
	 * @param httpSession
	 * @return
	 */
	public String acctName(HttpSession httpSession) {
		return (String) httpSession.getAttribute(SessionKey.MODULEACCTNAME);
	}
	/**
	 * 获取session里用户id
	 * 
	 * @param httpSession
	 * @return
	 */
	public Long acctId(HttpSession httpSession) {
		return (Long) httpSession.getAttribute(SessionKey.acctId);
	}

	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView() {
		return new ModelAndView();
	}

	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request;
	}

}
