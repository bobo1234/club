package com.java.back.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.java.back.constant.ClubConst;
import com.java.back.constant.PageConstant;
import com.java.back.dao.support.AbstractDao;
import com.java.back.model.club.TeCard;
import com.java.back.model.club.TeMember;
import com.java.back.model.club.TeMemberCard;
import com.java.back.utils.DateUtil;
import com.java.back.utils.StringUtil;

@Repository
public class MemberCardDao extends AbstractDao<TeMemberCard> {

	@Override
	public Class<TeMemberCard> getEntityClass() {
		// TODO Auto-generated method stub
		return TeMemberCard.class;
	}
	
	/**
	 * 办理会员卡
	 * @param member 会员
	 * @param cardid 卡类id
	 * @param ctimes 消费次数
	 * @return
	 */
	public TeMemberCard addMemberCard(Long memberid,TeCard card,int ctimes) {
		try {
			String nextMonthDay = DateUtil.getNextMonthDay(DateUtil.getDate2(), card.getValidtime());//到期时间
			TeMemberCard memberCard=new TeMemberCard();
			memberCard.setPrice(card.getPrice());
			memberCard.setExpiredate(nextMonthDay);
			memberCard.setResiduetimes(card.getUsertimes());//设置剩余次数
			memberCard.setIfuseless(ClubConst.STATE_UNUSED);//1:未使用,
			if (ctimes>0) {//是否消费, 1:未使用,2:已使用,3:已过期
				memberCard.setIfuseless(ClubConst.STATE_USED);
				if (card.getUsertimes()>0) {//不是月卡的话,消费次数的处理
					int atimes=card.getUsertimes()-ctimes;
					memberCard.setResiduetimes(atimes);//设置剩余次数
					if (atimes==0) {
						memberCard.setIfuseless(ClubConst.STATE_ZERO);//次数用完
					}
				}
				
			}
			TeMember member = this.getModel(memberid, TeMember.class);
			member.setConsumeMoney(member.getConsumeMoney()+card.getPrice());
			this.update(member);
			memberCard.setCardid(card.getId());
			memberCard.setCardType(card.getTypename());
			memberCard.setMemberid(memberid);
			memberCard.setCreatetime(DateUtil.getAllDate());
			this.save(memberCard);
			return memberCard;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询已办理的会员卡列表
	 * @param page
	 * @param serVal
	 * @param cardid
	 * @return
	 */
	public List findList(int page, String serVal,long cardid,int ifuseless){
		String sql="select mcd.*,mb.realname,mb.phone from te_member_card mcd ,te_card cd ,te_member mb where mcd.cardid=cd.id and mb.id=mcd.memberid ";
		sql += StringUtil.isEmpty(serVal) ? "" : " and (mb.realname like '%"
				+ serVal + "%' or mb.phone like '%" + serVal + "%'"+" or mb.namecode " +StringUtil.formatLike(serVal) + ")";
		sql+=cardid>0?" and cd.id="+cardid:"";
		sql+=ifuseless>0?" and mcd.ifuseless="+ifuseless:"";
		sql+=" order by createtime desc";
		List list = findSession().createSQLQuery(sql).setFirstResult((page - 1) * PageConstant.PAGE_LIST)
		.setMaxResults(PageConstant.PAGE_LIST).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	/**
	 *  查询已办理的会员卡数量
	 * @param page
	 * @param serVal
	 * @param cardid
	 * @return
	 */
	public int findCount(int page, String serVal,long cardid,int ifuseless){
		String sql="select count(mcd.id) from te_member_card mcd ,te_card cd ,te_member mb where mcd.cardid=cd.id and mb.id=mcd.memberid ";
		sql += StringUtil.isEmpty(serVal) ? "" : " and (mb.realname like '%"
				+ serVal + "%' or mb.phone like '%" + serVal + "%'"+" or mb.namecode " +StringUtil.formatLike(serVal) + ")";
		sql+=cardid>0?" and cd.id="+cardid:"";
		sql+=ifuseless>0?" and mcd.ifuseless="+ifuseless:"";
		return this.countAll(sql);
	}

}
