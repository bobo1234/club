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

import com.java.back.constant.PageConstant;
import com.java.back.dao.MemberCardDao;
import com.java.back.dao.MemberConsumeDao;
import com.java.back.model.club.TeCard;
import com.java.back.model.club.TeMemberCard;
import com.java.back.service.MemberCardService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.PageUtils;

@Scope
@Service
@Transactional(readOnly = true)
public class MemberCardServiceImpl implements MemberCardService {

	@Autowired
	private MemberCardDao memberCardDao;
	/**
	 * 消费
	 */
	@Autowired
	private MemberConsumeDao memberConsumeDao;

	@Override
	public JSONReturn findList(int page, String serVal, long cardid,
			int ifuseless) {
		// TODO Auto-generated method stub
		List findList = memberCardDao.findList(page, serVal, cardid, ifuseless);
		return JSONReturn.buildSuccess(findList);
	}

	@Override
	public JSONReturn findCount(int page, String serVal, long cardid,
			int ifuseless) {
		// TODO Auto-generated method stub
		int count = memberCardDao.findCount(page, serVal, cardid, ifuseless);
		return JSONReturn.buildSuccess(PageUtils.calculatePage(page, count,
				PageConstant.PAGE_LIST));
	}

	@Override
	public JSONReturn findMById(String id) {
		// TODO Auto-generated method stub
		TeMemberCard teMemberCard = memberCardDao.get(id);
		if (CompareUtil.isEmpty(teMemberCard)) {
			return JSONReturn.buildFailure("数据不存在!");
		}
		return JSONReturn.buildSuccess(teMemberCard);
	}

	@Transactional
	@Override
	public JSONReturn addMemberCard(TeMemberCard memberCard, int ctimes) {
		// TODO Auto-generated method stub
		TeCard card = memberCardDao.getModel(memberCard.getCardid(),
				TeCard.class);
		if (CompareUtil.isEmpty(card)) {
			return JSONReturn.buildFailure("不存在此卡类!");
		}
		if (ctimes > 0) {// 是否消费
			if (card.getUsertimes() == 0) {
				if (ctimes > 1) {
					return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,
							JSONReturn.TYPE_WARNING, "操作失败,月卡只能本人消费,每次只能消费一次!");
				}
			} else {// 普通卡
				if ((card.getUsertimes() - ctimes) < 0) {
					return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,
							JSONReturn.TYPE_WARNING, "操作失败,次数不足!");
				}
			}
		}
		TeMemberCard teMemberCard = memberCardDao.addMemberCard(
				memberCard.getMemberid(), card, ctimes);
		if (!CompareUtil.isEmpty(teMemberCard)) {
			if (ctimes > 0) {// 是否消费
				memberConsumeDao.consumeCard(teMemberCard, ctimes);
			}
			return JSONReturn.buildSuccess("办理成功!");
		}
		return JSONReturn.buildFailure("办理失败!");

	}

	@Override
	public JSONReturn findListByMid(long memberid) {
		// TODO Auto-generated method stub
		Criteria criteria = memberCardDao.findSession()
				.createCriteria(memberCardDao.getEntityClass())
				.add(Restrictions.eq("memberid", memberid))
				.addOrder(Order.desc("createtime"));
		List list = criteria.list();
		if (CollectionUtils.isEmpty(list)) {
			return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,
					JSONReturn.TYPE_INFO, "没有查询到办卡历史");
		}
		return JSONReturn.buildSuccess(list);
	}

	@Transactional
	@Override
	public JSONReturn deleCard(String id) {
		// TODO Auto-generated method stub
		int i = memberCardDao.deleteByPropertyString("id", id);
		if (i > 0) {
			return JSONReturn.buildSuccess("删除成功");
		}
		return JSONReturn.buildFailure("删除失败");
	}

}
