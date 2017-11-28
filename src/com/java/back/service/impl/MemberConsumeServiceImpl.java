package com.java.back.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.ClubConst;
import com.java.back.dao.MemberCardDao;
import com.java.back.dao.MemberConsumeDao;
import com.java.back.model.club.TeMemberCard;
import com.java.back.model.club.TeMemberConsume;
import com.java.back.service.MemberConsumeService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.CompareUtil;

@Scope
@Service
@Transactional(readOnly = true)
public class MemberConsumeServiceImpl implements MemberConsumeService {

	@Autowired
	private MemberConsumeDao memberConsumeDao;
	
	@Autowired
	private MemberCardDao memberCardDao;
	
	@Transactional
	@Override
	public JSONReturn consumeCard(String mcdid,int ctimes) {
		// TODO Auto-generated method stub
		try {
			TeMemberCard teMemberCard =memberCardDao.get(mcdid);
			if (CompareUtil.isEmpty(teMemberCard)) {
				return JSONReturn.buildFailure("数据不存在");
			}
			teMemberCard.setIfuseless(ClubConst.STATE_USED);
			if (teMemberCard.getResiduetimes()>0) {
				int minus=teMemberCard.getResiduetimes()-ctimes;
				if (minus<0) {
					return JSONReturn.buildFailure("该卡剩余次数不足!");
				}
				teMemberCard.setResiduetimes(minus);//计算剩余次数
				if (minus==0) {
					teMemberCard.setIfuseless(ClubConst.STATE_ZERO);//次数用完
					memberCardDao.update(teMemberCard);
					boolean consumeCard = memberConsumeDao.consumeCard(teMemberCard, ctimes);
					if (consumeCard) {
						return JSONReturn.buildOther(JSONReturn.HEAD_SUCCESS,JSONReturn.TYPE_WARNING, "操作成功<br/><strong>该卡剩余次数已经为零!</strong>");
					}
				}
			}
			memberCardDao.update(teMemberCard);//普通卡的正常消费
			boolean consumeCard = memberConsumeDao.consumeCard(teMemberCard, ctimes);
			if (consumeCard) {
				return JSONReturn.buildSuccess("消费成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailure();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONReturn findConsumeList(String membercardid) {
		// TODO Auto-generated method stub
		Criteria criteria = memberConsumeDao.findSession().createCriteria(memberConsumeDao.getEntityClass()).add(Restrictions.eq("membercardid", membercardid)).addOrder(Order.desc("createtime"));
		List<TeMemberConsume> list = criteria.list();
		if (CollectionUtils.isEmpty(list)) {
			return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,JSONReturn.TYPE_INFO, "没有查询到消费历史");
		}
		return JSONReturn.buildSuccess(list);
	}

}
