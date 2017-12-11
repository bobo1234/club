package com.java.back.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.ClubConst;
import com.java.back.constant.PageConstant;
import com.java.back.dao.support.AbstractDao;
import com.java.back.model.club.TeMatch;
import com.java.back.service.MatchService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.DateUtil;
import com.java.back.utils.PageUtils;
import com.java.back.utils.StringUtil;

@Scope
@Service
@Transactional(readOnly = true)
public class MatchServiceImpl extends AbstractDao<TeMatch> implements
		MatchService {

	@Override
	public JSONReturn findMatchList(int page, String searchValue) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.* from ( select * from te_match where 1=1 ";
		if (StringUtil.isNotEmpty(searchValue)) {
			sql += " and name" + StringUtil.formatLike(searchValue);
			sql += " UNION SELECT  * from te_match where mark"
					+ StringUtil.formatLike(searchValue);
		}
		sql += " ) a  ORDER BY a.createtime desc  ";
		Query query = this.findSession().createSQLQuery(sql)
				.addEntity(getEntityClass());
		query.setFirstResult((page - 1) * PageConstant.PAGE_LIST);
		query.setMaxResults(PageConstant.PAGE_LIST);
		List<TeMatch> list = query.list();
		for (TeMatch teMatch : list) {
			teMatch.setMstate(ClubConst.findMVal(teMatch.getState()));// 状态
		}
		return JSONReturn.buildSuccess(list);
	}

	@Override
	public JSONReturn findMatchCount(int page, String searchValue) {
		// TODO Auto-generated method stub
		String sql = "SELECT count(*) from ( select 1 from te_match where 1=1 ";
		if (StringUtil.isNotEmpty(searchValue)) {
			sql += " and name" + StringUtil.formatLike(searchValue);
			sql += " UNION SELECT  1 from te_match where mark"
					+ StringUtil.formatLike(searchValue);
		}
		sql += " ) a ";
		int count = countAll(sql);
		return JSONReturn.buildSuccess(PageUtils.calculatePage(page, count,
				PageConstant.PAGE_LIST));
	}

	@Transactional
	@Override
	public JSONReturn modifyMatch(TeMatch match) {
		// TODO Auto-generated method stub
		try {
			if (match.getState() == ClubConst.M_ALREADYSTARTED)
				return JSONReturn.buildFailure("操作失败, 该比赛已经开始!");
			TeMatch match2 = get(match.getId());
			match2.setMark(match.getMark());
			match2.setMatchtime(match.getMatchtime());
			match2.setName(match.getName());
			this.update(match2);
			return JSONReturn.buildSuccess("修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailure();
	}

	@Transactional
	@Override
	public JSONReturn addMatch(TeMatch match) {
		// TODO Auto-generated method stub
		try {
			match.setState(ClubConst.M_NOTSTARTED);// 未开始
			match.setCreatetime(DateUtil.getAllDate());
			this.save(match);
			return JSONReturn.buildSuccess("保存成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailure();
	}

	@Transactional
	@Override
	public JSONReturn deleteMatch(String id) {
		// TODO Auto-generated method stub
		int i = this.deleteByProperty("id", id);
		if (i > 0) {
			return JSONReturn.buildSuccess("删除成功");
		}
		return JSONReturn.buildFailure();
	}

	@Override
	public JSONReturn findAllMatch() {
		// TODO Auto-generated method stub
		List<TeMatch> list = this.findAllByOrderAsc("matchtime");
		if (CollectionUtils.isEmpty(list)) {
			return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,
					JSONReturn.TYPE_WARNING, "没有数据");
		}
		return JSONReturn.buildSuccess(list);
	}

	@Override
	public Class<TeMatch> getEntityClass() {
		// TODO Auto-generated method stub
		return TeMatch.class;
	}

	@Override
	public JSONReturn findMatchById(String id) {
		// TODO Auto-generated method stub
		TeMatch teMatch = get(id);
		if (CompareUtil.isEmpty(teMatch)) {
			return JSONReturn.buildFailure("没有查询到数据");
		}
		return JSONReturn.buildSuccess(teMatch);
	}

	@Transactional
	@Override
	public JSONReturn modifyState(String id, int state) {
		// TODO Auto-generated method stub
		try {
			TeMatch teMatch = get(id);
			teMatch.setState(state);
			return JSONReturn.buildSuccess("操作成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailure();
	}
	
}
