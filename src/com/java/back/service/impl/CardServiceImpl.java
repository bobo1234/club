package com.java.back.service.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.club.TeCard;
import com.java.back.service.CardService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.DateUtil;
import com.java.back.utils.StringUtil;

@Service
@Transactional(readOnly = true)
public class CardServiceImpl extends AbstractDao<TeCard> implements CardService {

	@Override
	public JSONReturn findCardListInfo(String description) {
		// TODO Auto-generated method stub
		String hql = getHql;
		hql += StringUtil.isNotEmpty(description) ? " and ( description "
				+ StringUtil.formatLike(description) +" or typename"+StringUtil.formatLike(description)+") ": "";
		hql += " order by createtime desc ";
		List<TeCard> list = queryHqlForList(hql, null);
		for (TeCard teCard : list) {
			teCard.setCardamount(getNumber(teCard.getId()));
		}
		return JSONReturn.buildSuccess(list);
	}

	@CacheEvict(value = { "findAllCard" }, allEntries = true)
	@Transactional
	@Override
	public JSONReturn addCard(TeCard card) {
		// TODO Auto-generated method stub
		try {
			int i = findCountByProperty("typename", card.getTypename());
			if (i > 0) {
				return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,JSONReturn.TYPE_WARNING, "新增失败,存在同名的卡类信息");
			}
			card.setCreatetime(DateUtil.getDate());
			this.save(card);
			return JSONReturn.buildSuccess("新增成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailure("新增失败");
	}

	@CacheEvict(value = { "findAllCard" }, allEntries = true)
	@Transactional
	@Override
	public JSONReturn deleteCard(long id) {
		// TODO Auto-generated method stub
		if (getNumber(id)>0) {
			return JSONReturn.buildFailure("删除失败,该卡类下存在会员卡!");
		}
		int i = deleteByProperty("id", id);
		if (i > 0) {
			return JSONReturn.buildSuccess("删除成功");
		}
		return JSONReturn.buildFailure("删除失败");
	}
	
	/**
	 * 获取卡类下的会员卡数量
	 * @param id
	 * @return
	 */
	private long getNumber(long id){
		String sql = "select count(*) from te_member_card where cardid="
				+ id;
		long parseLong = Long.parseLong(this.findSession()
				.createSQLQuery(sql).uniqueResult().toString());
		return parseLong;
	}
	@Override
	public JSONReturn findCardById(long id) {
		// TODO Auto-generated method stub
		TeCard teCard = this.get(id);
		if (teCard != null) {
			return JSONReturn.buildSuccess(teCard);
		}
		return JSONReturn.buildFailure("数据不存在");
	}

	@CacheEvict(value = { "findAllCard" }, allEntries = true)
	@Transactional
	@Override
	public JSONReturn modifyCard(TeCard card) {
		// TODO Auto-generated method stub
		try {
			if (CompareUtil.isEmpty(card.getId()))
				return JSONReturn.buildFailure("操作失败, 源数据不存在!");
			String sql = "select count(*) from te_card where typename=? and id!=?";
			Query query = this.findSession().createSQLQuery(sql)
					.setString(0, card.getTypename()).setLong(1, card.getId());
			if (Integer.parseInt(query.uniqueResult().toString()) > 0) {
				return JSONReturn.buildFailure("操作失败, 卡类名称已存在!");
			}
			this.update(card);
			return JSONReturn.buildSuccess("操作成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JSONReturn.buildFailure("操作失败");
		}
	}

	@Override
	public Class<TeCard> getEntityClass() {
		// TODO Auto-generated method stub
		return TeCard.class;
	}

	//将数据放入缓存
	@Cacheable(value = "findAllCard",key="'AllCard'")
	@Override
	public JSONReturn findAllCard() {
		// TODO Auto-generated method stub
		System.out.println("进入redis缓存");
		List<TeCard> list = findAllByOrderDesc("createtime");
		return JSONReturn.buildSuccess(list);
	}

}
