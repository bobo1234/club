package com.java.back.dao;

import org.springframework.stereotype.Repository;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.club.TeMemberCard;
import com.java.back.model.club.TeMemberConsume;
import com.java.back.utils.DateUtil;

@Repository
public class MemberConsumeDao extends AbstractDao<TeMemberConsume> {

	@Override
	public Class<TeMemberConsume> getEntityClass() {
		// TODO Auto-generated method stub
		return TeMemberConsume.class;
	}
	
	/**
	 * 消费会员卡
	 * @param memberCard
	 * @param ctimes 消费次数
	 * @return
	 */
	public boolean consumeCard(TeMemberCard memberCard,int ctimes) {
		try {
			TeMemberConsume consume=new TeMemberConsume();
			consume.setCreatetime(DateUtil.getAllDate());
			consume.setMembercardid(memberCard.getId());
			consume.setMemberid(memberCard.getMemberid());
			consume.setCtimes(ctimes);
			this.save(consume);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
