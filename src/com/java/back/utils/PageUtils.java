package com.java.back.utils;

import java.io.Serializable;

import com.java.back.dto.PageDto;

public class PageUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 封装的分页方法
	 * @param nowPage
	 * @param count
	 * @param number
	 * @return
	 */
	public static PageDto calculatePage(int nowPage, int count, int number) {
		PageDto dto = new PageDto();
		dto.setNumber(number);
		dto.setCount(count);
		dto.setNowPage(nowPage < 1 ? 1 : nowPage);
		dto.setTotalPage(count % number > 0 ? ((count / number) + 1) : count / number);
		return dto;
	}

}
