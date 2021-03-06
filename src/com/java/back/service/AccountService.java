package com.java.back.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.java.back.model.TeAccount;
import com.java.back.support.JSONReturn;

public abstract interface AccountService {

	public abstract JSONReturn login(String name, String pass, HttpServletRequest request);

	public abstract JSONReturn findAccountInfo(String acctName);

	public abstract JSONReturn exit(HttpSession httpSession,HttpServletRequest request);

	public abstract JSONReturn addAccount(String user, String nick, String pass, String acctName);

	public abstract JSONReturn findAccountList(int page, String searchValue, String acctName);

	public abstract JSONReturn findAccountPage(int page, String searchValue);

	public abstract JSONReturn delAccount(long id, String acctName);

	public abstract JSONReturn initPassword(long id, String acctName);

	public abstract JSONReturn findAccountById(long id);

	public abstract JSONReturn addAccountRole(long id, String account, boolean add, String acctName);

	public abstract JSONReturn findRole(String acctName);

	public abstract TeAccount findAccountByName(String userName);

	public abstract JSONReturn mdoifyPass(String password, String acctName);
	
	public abstract JSONReturn findAccount();
	/**
	 * 修改用户信息
	 * @param account
	 * @return
	 */
	public abstract JSONReturn modeifyAccount(TeAccount account);

}
