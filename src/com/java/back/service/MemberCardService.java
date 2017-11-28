package com.java.back.service;

import com.java.back.model.club.TeMemberCard;
import com.java.back.support.JSONReturn;

public abstract interface MemberCardService {
	/**
	 * 会员办理卡的记录列表
	 * @param page
	 * @param serVal 手机号,名字,名字首字母
	 * @param cardid 卡类id
	 * @param ifuseless 状态
	 * @return
	 */
	public abstract JSONReturn findList(int page, String serVal,long cardid,int ifuseless);
	/**
	 * 会员办理卡记录数量
	 * @param page
	 * @param serVal
	 * @param cardid
	 * @param ifuseless
	 * @return
	 */
	public abstract JSONReturn findCount(int page, String serVal,long cardid,int ifuseless);
	/**
	 * 根据会员id查询购买的会员卡记录
	 * @param id 会员id
	 * @return
	 */
	public abstract JSONReturn findListByMid(long memberid);
	
	/**
	 * 查看详情
	 * @param id 
	 * @return
	 */
	public abstract JSONReturn findMById(String id);
	
	/**
	 * 删除会员卡
	 * @param id 
	 * @return
	 */
	public abstract JSONReturn deleCard(String id);
	
	/**
	 * 办理会员卡
	 * @param memberCard
	 * @param ctimes 消费次数
	 * @return
	 */
	public abstract JSONReturn addMemberCard(TeMemberCard memberCard,int ctimes);
	
}
