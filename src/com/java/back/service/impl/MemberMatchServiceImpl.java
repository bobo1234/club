package com.java.back.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.ClubConst;
import com.java.back.dao.support.AbstractDao;
import com.java.back.model.club.TeMatch;
import com.java.back.model.club.TeMemberMatchLog;
import com.java.back.service.MemberMatchService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.Common;
import com.java.back.utils.DateUtil;
import com.java.back.utils.StringUtil;

@Scope
@Service
@Transactional(readOnly = true)
public class MemberMatchServiceImpl extends AbstractDao<TeMemberMatchLog>
		implements MemberMatchService {

	@Override
	public JSONReturn findMeMatchList(String mid, String searchVal) {
		// TODO Auto-generated method stub
		Criteria criteria = this.findSession().createCriteria(getEntityClass());
		criteria.createAlias("match", "m");
		criteria.createAlias("members", "mb");
		criteria.add(Restrictions.eq("m.id", mid));
		if (StringUtil.isNotEmpty(searchVal)) {
			criteria.add(Restrictions.or(
					Restrictions.eq("mb.namecode", searchVal),
					Restrictions.like("mb.phone", searchVal, MatchMode.END)));

		}
		TeMatch match = getModel(mid, TeMatch.class);
		if (match.getState()==ClubConst.M_HASENDED||match.getState()==ClubConst.M_ALREADYSTARTED) {
			criteria.addOrder(Order.asc("score"));
			List<TeMemberMatchLog> list = criteria.list();
			List<TeMemberMatchLog> list2 = list.subList(list.size()-4, list.size());
			List<TeMemberMatchLog> subList = list.subList(0, list.size()-4);
			list2.addAll(subList);
			return JSONReturn.buildSuccess(list2);
		}
		criteria.addOrder(Order.asc("createtime"));
		return JSONReturn.buildSuccess(criteria.list());
	}

	@Transactional
	@Override
	public JSONReturn addMemberMatch(TeMemberMatchLog memberMatchLog) {
		// TODO Auto-generated method stub
		try {
			memberMatchLog.setCreatetime(DateUtil.getAllDate());
			TeMatch match = this.getModel(memberMatchLog.getMatch().getId(),
					TeMatch.class);
			match.setInmembers(match.getInmembers() + 1);// 参赛人数加一
			memberMatchLog.setScore(0);
			this.save(memberMatchLog);
			return JSONReturn.buildSuccess("操作成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailureWithEmptyBody();
	}

	@Transactional
	@Override
	public JSONReturn deleteMemberMatch(String id) {
		// TODO Auto-generated method stub
		TeMemberMatchLog teMemberMatchLog = this.get(id);
		TeMatch match = this.getModel(teMemberMatchLog.getMatch().getId(),
				TeMatch.class);
		match.setInmembers(match.getInmembers() - 1);// 参赛人数减一
		boolean delete = delete(teMemberMatchLog);
		if (delete) {
			return JSONReturn.buildSuccess("操作成功");
		}
		return JSONReturn.buildFailureWithEmptyBody();
	}

	@Override
	public Class<TeMemberMatchLog> getEntityClass() {
		// TODO Auto-generated method stub
		return TeMemberMatchLog.class;
	}

	@Resource(name = "dataSource")
	private DataSource dataSource;

	@Transactional
	@Override
	public JSONReturn addMemberMatchAll(String matchid, long[] memberids) {
		// TODO Auto-generated method stub
		TeMatch match = this.getModel(matchid, TeMatch.class);
		match.setInmembers(match.getInmembers() + memberids.length);// 参赛人数
		this.save(match);
		try {
			Connection conn = dataSource.getConnection();
			String sql = "INSERT INTO TE_MEMBER_MATCH_LOG (ID,MATCH_ID,MEMBER_ID,CREATETIME,SCORE) VALUES (?,?,?,?,0) ";
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			for (int i = 0; i < memberids.length; i++) {
				ps.setString(1, Common.GetUUID());
				ps.setString(2, matchid);
				ps.setLong(3, memberids[i]);
				ps.setString(4, DateUtil.getAllDate());
				ps.addBatch();
			}
			try {
				ps.executeBatch();
				conn.commit();
				ps.close();
			} catch (Exception e) {
				// TODO: handle exception
				conn.rollback();
				ps.close();
			}
			return JSONReturn.buildSuccess("操作成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailureWithEmptyBody();
	}

	@Transactional
	@Override
	public JSONReturn matchScore(String id, int score) {
		// TODO Auto-generated method stub
		try {
			int countAll = this
					.countAll("SELECT count(mg.id) FROM te_member_match_log mg,te_member_match_log ml WHERE	mg.score > 0 AND mg.match_id = ml.match_id AND ml.id"
							+ StringUtil.formatEqual(id) +" and mg.id"+StringUtil.formatNotEqual(id));
			if (countAll <4) {
				TeMemberMatchLog teMemberMatchLog = get(id);
				teMemberMatchLog.setScore(score);
				return JSONReturn.buildSuccess("操作成功");
			}
			return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,
					JSONReturn.TYPE_WARNING, "只能设置前四名!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailureWithEmptyBody();
	}

}
