package com.java.back.service;

import com.java.back.support.JSONReturn;

public abstract interface MemberConsumeService {

	/**
	 * 消费卡
	 * @param memberCard
	 * @return
	 */
	public abstract JSONReturn consumeCard(String mcdid,int ctimes);
	
	/**
	 * 该会员卡消费历史
	 * @param membercardid
	 * @return
	 */
	public abstract JSONReturn findConsumeList(String membercardid);

}
