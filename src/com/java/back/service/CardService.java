package com.java.back.service;

import com.java.back.model.club.TeCard;
import com.java.back.support.JSONReturn;

public interface CardService {
	/**
	 * 查询所有的卡类列表
	 * @param description
	 * @return
	 */
	public JSONReturn findCardListInfo(String description);
	
	/**
	 * 新增卡类信息
	 * @param card
	 * @return
	 */
	public JSONReturn addCard(TeCard card);
	
	/**
	 * 删除卡类
	 * @param id
	 * @return
	 */
	public JSONReturn deleteCard(long id);
	
	public JSONReturn findCardById(long id);
	/**
	 * 修改会员卡类
	 * @param card
	 * @return
	 */
	public JSONReturn modifyCard(TeCard card);

	/**
	 * 查询所有的卡类信息
	 * @return
	 */
	public JSONReturn findAllCard();
	
}
