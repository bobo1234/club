package com.java.back.constant;

import com.java.back.utils.Config;

/**
 * 俱乐部相关常量
 * 
 * @author gyb
 */
public class ClubConst {

	/**
	 * 根据 消费情况判断会员等级(配置文件)
	 * 
	 * @param money
	 * @return
	 */
	public static String getRank(int money) {
		String rangmess = Config.getValue("rank");
		if (money > Integer.parseInt(rangmess.split(",")[2])) {
			return "钻石会员";
		} else if (money > Integer.parseInt(rangmess.split(",")[1])) {
			return "黄金会员";
		} else if (money > Integer.parseInt(rangmess.split(",")[0])) {
			return "白银会员";
		}
		return "黄铜会员";
	}

	/**
	 * 女
	 */
	public static final int GIRL = 2;
	/**
	 * 男
	 */
	public static final int BOY = 1;

	/**
	 * 直板
	 */
	public static final int ZHI = 1;
	/**
	 * 横板
	 */
	public static final int HENG = 2;

	
	
	/**
	 * 会员卡未使用
	 */
	public static final int STATE_UNUSED = 1;

	/**
	 * 会员卡已使用
	 */
	public static final int STATE_USED = 2;
	/**
	 * 会员卡已经用完
	 */
	public static final int STATE_ZERO = 3;

	/**
	 * 会员卡已过期即已作废
	 */
	public static final int STATE_OVERDUE = 4;

	
	
	/**
	 * 有效 or 可用
	 */
	public static final int VALID = 1;

	/**
	 * 无效 or 作废
	 */
	public static final int INVALID = 2;
	
	
	/**
	 * 比赛未开始
	 */
	public static final int M_NOTSTARTED = 1;
	/**
	 * 比赛已开始
	 */
	public static final int M_ALREADYSTARTED = 2;
	/**
	 * 比赛已经结束
	 */
	public static final int M_HASENDED = 3;
	/**
	 * 比赛已经取消
	 */
	public static final int M_CANCEL = 4;
	/**
	 * 获取比赛状态
	 * @param key
	 * @return
	 */
	public static String findMVal(int key) {
		switch (key) {
		case 1:
			return "未开始";
		case 2:
			return "已开始";
		case 3:
			return "已结束";
		case 4:
			return "已取消";
		}
		return "";
	}
	
}
