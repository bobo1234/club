package com.java.back.constant;
/**
 * 用户状态
 * @author gyb
 *
 */
public class AccountState {

	/**
	 * 正常
	 */
	public static final int ACCOUNT_GENERAL = 0;
	/**
	 * 限制
	 */
	public static final int ACCOUNT_LIMIT = 1;
	/**
	 * 作废
	 */
	public static final int ACCOUNT_INVALID = 2;
	
	public static String getState(int s){
		if (s==ACCOUNT_LIMIT) {
			return "该帐号已被限制,请联系管理员";
		}
		if (s==ACCOUNT_INVALID) {
			return "该帐号已作废";
		}
		return "";
	}

}
