package com.java.back.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.AccountDeleteState;
import com.java.back.constant.AccountInitPassword;
import com.java.back.constant.AccountState;
import com.java.back.constant.MethodType;
import com.java.back.constant.PageConstant;
import com.java.back.constant.SessionKey;
import com.java.back.dao.AccountDao;
import com.java.back.dao.AccountRoleDao;
import com.java.back.dao.RoleDao;
import com.java.back.dto.AccountListDto;
import com.java.back.dto.RoleDto;
import com.java.back.field.TeAccountField;
import com.java.back.field.TeRoleField;
import com.java.back.model.SystemLog;
import com.java.back.model.TeAccount;
import com.java.back.model.TeAccountRole;
import com.java.back.model.TeRole;
import com.java.back.redis.RedisSpringProxy;
import com.java.back.service.AccountService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.BrowserUtils;
import com.java.back.utils.Common;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.DateTimeUtil;
import com.java.back.utils.DateUtil;
import com.java.back.utils.EncryptUtil;
import com.java.back.utils.PageUtils;
import com.java.back.utils.SpringContextUtil;

@Scope
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl extends RedisSpringProxy implements
		AccountService {

	@Autowired
	private AccountDao accountDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private AccountRoleDao accountRoleDao;

	private static String Keys = "LOGIN";

	@Transactional
	public JSONReturn login(String name, String pass, HttpServletRequest request) {
		// TODO Auto-generated method stub
		// 单用户登录
		// if (exists(name.toUpperCase())) {
		// return JSONReturn.buildFailure("此帐号已经登录,不可重复登录");
		// }
		TeAccount account = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_NAME, name);
		if (CompareUtil.isEmpty(account) || account.getAcctDeleteState()) {
			return JSONReturn.buildFailure("登录失败, 用户不存在");
		}
		/**
		 * 非正常帐号提示信息
		 */
		if (account.getAcctState() != 0) {
			return JSONReturn.buildFailure(AccountState.getState(account
					.getAcctState()));
		}
		if (!account.getAcctPassword()
				.equals(EncryptUtil.encodeMD5String(pass))) {//密码不正确的情况下
			boolean exists = exists(Keys + name);
			if (exists) {
				Long read = Long.parseLong(read(Keys + name).toString());
				if (read == 3) {// 十分钟内连续失败三次,限制用户再次登录
					account.setAcctState(AccountState.ACCOUNT_LIMIT);
					accountDao.update(account);
					return JSONReturn.buildFailure("您已经连续登录失败三次,帐号被锁定,请联系管理员");
				}
				incr(Keys + name);// 存在加一
			} else {
				saveEx(Keys + name, 1, 60 * 10L);// 缓存保存时间为10分钟
			}
			return JSONReturn.buildFailure("登录失败, 用户名或密码错误!");
		}
		// save(name.toUpperCase(), "TRUE");

		request.getSession().setAttribute(SessionKey.MODULEACCTNAME,
				account.getAcctName());
		request.getSession().setAttribute(SessionKey.acctId,
				account.getAcctId());
		SystemLog systemLog = new SystemLog();
		systemLog.setCreateByUser(account.getAcctId());
		systemLog.setCreateByUserName(account.getAcctName());
		systemLog.setCreateDate(DateUtil.getAllDate());
		systemLog.setMethod("login");
		systemLog.setDescription("用户登录");
		systemLog.setLogType(MethodType.LOGIN.getId());// 操作类型(登录)
		systemLog.setParams("浏览器:" + BrowserUtils.checkBrowse(request));
		String address = Common.getIpAddress(request);
		systemLog.setRequestIp(address);
		accountDao.save(systemLog);
		account.setTimestamp(new Date());// 更新最后登录时间
		return JSONReturn.buildSuccess("登录成功!");
	}

	public JSONReturn findAccountInfo(String acctName) {
		// TODO Auto-generated method stub
		TeAccount account = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_NAME, acctName);
		if (CompareUtil.isEmpty(account) || account.getAcctDeleteState())
			return JSONReturn.buildFailure("未获取到账户信息");
		account.setAcctPassword("");
		return JSONReturn.buildSuccess(account);
	}

	@Transactional
	public JSONReturn exit(HttpSession httpSession, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String acctName = (String) httpSession
				.getAttribute(SessionKey.MODULEACCTNAME);
		Long acctId = (Long) httpSession.getAttribute(SessionKey.acctId);
		if (StringUtils.isEmpty(acctName))
			return JSONReturn.buildFailure("操作失败!");
		/**
		 * 保存登出日志
		 */
		SystemLog systemLog = new SystemLog();
		systemLog.setCreateByUser(acctId);
		systemLog.setCreateDate(DateUtil.getAllDate());
		systemLog.setMethod("exit");
		systemLog.setDescription("用户登出");
		String address = Common.getIpAddress(request);
		systemLog.setRequestIp(address);
		systemLog.setCreateByUserName(acctName);
		accountDao.save(systemLog);
		httpSession.removeAttribute(SessionKey.MODULEACCTNAME);// 从session里移除用户名和id
		httpSession.removeAttribute(SessionKey.acctId);
		return JSONReturn.buildSuccessWithEmptyBody();
	}

	@Transactional
	public JSONReturn addAccount(String user, String nick, String pass,
			String acctName) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(user) || StringUtils.isEmpty(nick)
				|| StringUtils.isEmpty(pass))
			return JSONReturn.buildFailure("添加失败!");
		TeAccount account = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_NAME, user);
		if (CompareUtil.isNotEmpty(account))
			return JSONReturn.buildFailure("添加失败, 用户名重复!");
		account = new TeAccount();
		account.setAcctName(user);
		account.setAcctNickname(nick);
		account.setAcctPassword(EncryptUtil.encodeMD5String(pass));
		account.setAcctState(AccountState.ACCOUNT_GENERAL);
		account.setAcctSuper(false);
		account.setCreateTime(DateTimeUtil.getCurrentTime());
		account.setCreator(acctName);
		account.setAcctDeleteState(AccountDeleteState.NO_DELETE);
		accountDao.save(account);
		return JSONReturn.buildSuccess("添加成功!");
	}

	public JSONReturn findAccountList(int page, String searchValue,
			String acctName) {
		// TODO Auto-generated method stub
		List<AccountListDto> dtoList = accountDao.findAccountList(page,
				searchValue);
		if (CollectionUtils.isEmpty(dtoList))
			return JSONReturn.buildFailure("未获取到数据!");
		return JSONReturn.buildSuccess(dtoList);
	}

	public JSONReturn findAccountPage(int page, String searchValue) {
		// TODO Auto-generated method stub
		return JSONReturn
				.buildSuccess(PageUtils.calculatePage(page,
						accountDao.findAccountPage(searchValue),
						PageConstant.PAGE_LIST));
	}

	@Transactional
	public JSONReturn delAccount(long id, String acctName) {
		// TODO Auto-generated method stub
		TeAccount account = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_ID, id);
		if (CompareUtil.isEmpty(account) || account.getAcctDeleteState())
			return JSONReturn.buildFailure("操作失败, 源数据不存在!");
		if (account.getAcctSuper())
			return JSONReturn.buildFailure("非法操作!");
		// account.setAcctDeleteState(AccountDeleteState.IS_DELETE); //
		// 标识账户信息为删已经
		accountDao.delete(account); // 直接删除帐户信息
		return JSONReturn.buildSuccess("删除成功!");
	}

	@Transactional
	public JSONReturn initPassword(long id, String acctName) {
		// TODO Auto-generated method stub
		TeAccount account = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_ID, id);
		if (CompareUtil.isEmpty(account) || account.getAcctDeleteState())
			return JSONReturn.buildFailure("操作失败, 源数据不存在!");
		account.setAcctPassword(EncryptUtil
				.encodeMD5String(AccountInitPassword.ACCOUNT_INIT_PASSWORD));
		return JSONReturn.buildSuccess("操作成功, 初始化密码为"
				+ AccountInitPassword.ACCOUNT_INIT_PASSWORD + "!");
	}

	public JSONReturn findAccountById(long id) {
		// TODO Auto-generated method stub
		TeAccount acct = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_ID, id);
		if (CompareUtil.isEmpty(acct) || acct.getAcctDeleteState())
			return JSONReturn.buildFailure("操作失败, 源数据不存在");
		return JSONReturn.buildSuccess(new AccountListDto(acct.getAcctId(),
				acct.getAcctName(), acct.getAcctNickname(), String.valueOf(acct
						.getCreateTime()), acct.getCreator(), acct
						.getAcctSuper(), acct.getAcctState(), acct.getMark()));
	}

	public JSONReturn findRole(String acctName) {
		// TODO Auto-generated method stub
		List<TeRole> roleList = roleDao.findAll();
		if (CollectionUtils.isEmpty(roleList))
			return JSONReturn.buildFailure("未获取到角色信息!");
		List<RoleDto> dtoList = new ArrayList<RoleDto>();
		for (TeRole ro : roleList) {
			RoleDto dto = new RoleDto();
			dto.setId(ro.getRoleId());
			dto.setRoleName(ro.getRoleName());
			dto.setOpt(CollectionUtils.isNotEmpty(accountRoleDao
					.findByAcctNameAndRoleLabel(acctName, ro.getRoleLabel())));// 前台页面的遍历是否选中状态
			dto.setAcctName(acctName);
			dtoList.add(dto);
		}
		return JSONReturn.buildSuccess(dtoList);
	}

	@Transactional
	public JSONReturn addAccountRole(long id, String account, boolean add,
			String acctName) {
		// TODO Auto-generated method stub
		TeRole teRole = roleDao.findUniqueByProperty(TeRoleField.ROLE_ID, id);
		if (CompareUtil.isEmpty(teRole))
			return JSONReturn.buildFailure("操作失败, 数据源不存在!");
		if (!add) {
			accountRoleDao.delByAcctNameAndRoleLabel(account,
					teRole.getRoleLabel());
			return JSONReturn.buildSuccessWithEmptyBody();
		}
		if (CollectionUtils.isNotEmpty(accountRoleDao
				.findByAcctNameAndRoleLabel(account, teRole.getRoleLabel()))) {
			return JSONReturn.buildFailure("添加失败, 重复添加!");
		}
		accountRoleDao.save(new TeAccountRole(account, teRole.getRoleLabel()));
		return JSONReturn.buildSuccessWithEmptyBody();
	}

	public TeAccount findAccountByName(String userName) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(userName))
			return null;
		return accountDao.findUniqueByProperty(TeAccountField.ACCT_NAME,
				userName);
	}

	@Transactional
	public JSONReturn mdoifyPass(String password, String acctName) {
		// TODO Auto-generated method stub
		TeAccount acct = accountDao.findUniqueByProperty(
				TeAccountField.ACCT_NAME, acctName);
		if (CompareUtil.isEmpty(acct))
			return JSONReturn.buildFailure("修改密码失败, 用户不存在!");
		acct.setAcctPassword(EncryptUtil.encodeMD5String(password));
		SpringContextUtil.getRequest().getSession().invalidate();// 清空session内容
		return JSONReturn.buildSuccess("密码修改成功,即将转入登录页面!");
	}

	public JSONReturn findAccount() {
		// TODO Auto-generated method stub
		List<TeAccount> list = accountDao.findAll();
		return JSONReturn.buildSuccess(list);
	}

	@Transactional
	@Override
	public JSONReturn modeifyAccount(TeAccount account) {
		// TODO Auto-generated method stub
		try {
			TeAccount acct = accountDao.findUniqueByProperty(
					TeAccountField.ACCT_ID, account.getAcctId());
			if (CompareUtil.isEmpty(acct) || acct.getAcctDeleteState())
				return JSONReturn.buildFailure("操作失败, 源数据不存在!");
			acct.setAcctState(account.getAcctState());
			acct.setAcctNickname(account.getAcctNickname());
			return JSONReturn.buildSuccess("操作成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JSONReturn.buildFailure("操作失败");
		}
	}
}
