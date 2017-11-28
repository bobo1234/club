package com.java.back.intercept;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.java.back.annotation.SecureValid;
import com.java.back.constant.LoginState;
import com.java.back.constant.SessionKey;
import com.java.back.model.SystemLog;
import com.java.back.model.TeAccount;
import com.java.back.service.AccountService;
import com.java.back.service.ModuleService;
import com.java.back.service.SystemLogService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.Common;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.JsonUtil;
import com.java.back.utils.StringUtil;

public class SecureValidInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ModuleService moduleService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SystemLogService systemLogService;

	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object handle) throws Exception {
		// TODO Auto-generated method stub
		if (req.getRequestURI().contains("mgr/0"))// 验证图片
			return true;
		String userName = (String) req.getSession().getAttribute(
				SessionKey.MODULEACCTNAME);
		/**
		 * 用户未登录时候
		 */
		if (StringUtils.isEmpty(userName)) {
			resp.getOutputStream().print(
					JSONObject.fromObject(
							JSONReturn.buildFailure(LoginState.UNLOGIN))
							.toString());
			return false;
		}
		/**
		 * 从数据库中获取当前账户是否存在, 如果不存在, 提示未登录
		 */
		TeAccount teAccount = accountService.findAccountByName(userName);
		if (CompareUtil.isEmpty(teAccount)) {
			resp.getOutputStream().print(
					JSONObject.fromObject(
							JSONReturn.buildFailure(LoginState.UNLOGIN))
							.toString());
			return false;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handle;
		
		SecureValid secureValid = handlerMethod.getMethod().getAnnotation(
				SecureValid.class);
		Map<String, String[]> map = req.getParameterMap();
		System.out.println("方法:" + handlerMethod.getMethod().getName()
				+ "   参数:" + JsonUtil.mapTojson(map));

		/**
		 * 方法有注释的情况下,才去记录操作日志
		 */
		if (!CompareUtil.isEmpty(secureValid)) {
			if (secureValid.code().length > 0) {// 如果controller的方法里有权限验证的情况下,判断权限
				if (StringUtil.isNotEmpty(secureValid.code()[0])) {
					if (!moduleService.secureValid(userName,
							secureValid.code(), secureValid.type())) {
						resp.getOutputStream()
								.print(JSONObject
										.fromObject(
												JSONReturn
														.buildFailure(LoginState.PERMISSION_DENIED))
										.toString());
						return false;
					}
				}

			}

			/**
			 * 记录操作日志
			 */
			Long acctId = Long.parseLong(req.getSession()
					.getAttribute(SessionKey.acctId).toString());
			SystemLog systemLog = new SystemLog();
			systemLog.setCreateByUser(acctId);
			systemLog.setMethod(handlerMethod.getMethod().getName());
			systemLog.setDescription(secureValid.desc());
			systemLog.setLogType(secureValid.type().getId());// 操作类型
			if (!handlerMethod.getMethod().getName().toUpperCase()
					.contains("LOGIN")) {// 不是登录接口时候记录
				systemLog.setParams(JsonUtil.mapTojson(req.getParameterMap()));
			}
			systemLog.setCreateByUserName(userName);
			String address = Common.getIpAddress(req);
			systemLog.setRequestIp(address);
			try {
				systemLogService.addLog(systemLog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 如果是超管, 直接通过拦截器
		if (teAccount.getAcctSuper())
			return true;
		return true;
	}

}
