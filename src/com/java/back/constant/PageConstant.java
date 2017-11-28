package com.java.back.constant;

import com.java.back.utils.Config;

public class PageConstant {

	/**
	 * 普通数据列表显示条数
	 */
	public static final int PAGE_LIST = Integer.parseInt(Config.getValue("pagesize"));

}
