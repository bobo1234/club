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
import com.java.back.model.TeAccount;
import com.java.back.service.AccountService;
import com.java.back.support.JSONReturn;

@Scope
@Controller
@RequestMapping(value = "account")
public class AccountController extends AbstractController {

	@Autowired
	private AccountService accountService;

	@ResponseBody
	@RequestMapping(value = "findAccountInfo")
	public JSONReturn findAccountInfo(HttpSession httpSession) {
		return accountService.findAccountInfo(acctName(httpSession));
	}

	@ResponseBody
	@RequestMapping(value = "addAccount")
	@SecureValid(code = "04001", desc = "添加账户信息", type = MethodType.ADD)
	public JSONReturn addAccount(@RequestParam String user,
			@RequestParam String nick, @RequestParam String pass,
			HttpSession httpSession) {
		return accountService.addAccount(user, nick, pass,
				acctName(httpSession));
	}

	@ResponseBody
	@RequestMapping(value = "findAccountList")
	public JSONReturn findAccountList(@RequestParam int page,
			@RequestParam String searchValue, HttpSession httpSession) {
		return accountService.findAccountList(page, searchValue,
				acctName(httpSession));
	}

	/**
	 * 人员的下拉列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findAccount")
	public JSONReturn findAccount() {
		return accountService.findAccount();
	}

	@ResponseBody
	@RequestMapping(value = "findAccountPage")
	public JSONReturn findAccountPage(@RequestParam int page,
			@RequestParam String searchValue) {
		return accountService.findAccountPage(page, searchValue);
	}

	@ResponseBody
	@RequestMapping(value = "delAccount")
	@SecureValid(code = "04001", desc = "删除账户信息", type = MethodType.DELETE)
	public JSONReturn delAccount(@RequestParam long id, HttpSession httpSession) {
		return accountService.delAccount(id, acctName(httpSession));
	}

	@ResponseBody
	@RequestMapping(value = "initPassword")
	@SecureValid(code = "04001", desc = "重置密码", type = MethodType.MODIFY)
	public JSONReturn initPassword(@RequestParam long id,
			HttpSession httpSession) {
		return accountService.initPassword(id, acctName(httpSession));
	}

	@ResponseBody
	@RequestMapping(value = "modifyAccount")
	@SecureValid(code = "", desc = "修改用户信息", type = MethodType.MODIFY)
	public JSONReturn modifyAccount(@RequestParam long id,
			@RequestParam String nickname, Integer state,
			HttpSession httpSession) {
		TeAccount account = new TeAccount();
		account.setAcctId(id);
		account.setAcctState(state);
		account.setAcctNickname(nickname);
		return accountService.modeifyAccount(account);
	}

	@ResponseBody
	@RequestMapping(value = "findAccountById")
	public JSONReturn findAccountById(@RequestParam long id) {
		return accountService.findAccountById(id);
	}

	@ResponseBody
	@RequestMapping(value = "findRole")
	public JSONReturn findRole(@RequestParam String acctName) {
		return accountService.findRole(acctName);
	}

	@ResponseBody
	@RequestMapping(value = "addAccountRole")
	@SecureValid(code = "04001", desc = "修改角色与帐号之间的关联", type = MethodType.MODIFY)
	public JSONReturn addAccountRole(@RequestParam long id,
			@RequestParam String account, @RequestParam boolean add,
			HttpSession httpSession) {
		return accountService.addAccountRole(id, account, add,
				acctName(httpSession));
	}

}
