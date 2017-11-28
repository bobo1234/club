package com.java.back.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.dao.support.AbstractDao;
import com.java.back.model.club.TeMatch;
import com.java.back.model.club.TeMemberMatchLog;
import com.java.back.service.MemberMatchService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.Common;
import com.java.back.utils.DateUtil;

@Scope
@Service
@Transactional(readOnly = true)
public class MemberMatchServiceImpl extends AbstractDao<TeMemberMatchLog>
		implements MemberMatchService {

	@Override
	public JSONReturn findMeMatchList(String mid) {
		// TODO Auto-generated method stub
		Criteria criteria = this.findSession().createCriteria(getEntityClass());
		criteria.createAlias("match", "m");
		criteria.add(Restrictions.eq("m.id", mid));
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
			match.setInmembers(match.getInmembers() + 1);//参赛人数加一
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
		match.setInmembers(match.getInmembers() - 1);//参赛人数减一
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
		TeMatch match = this.getModel(matchid,
				TeMatch.class);
		match.setInmembers(match.getInmembers()+memberids.length);//参赛人数
		this.save(match);
		try {
			Connection conn = dataSource.getConnection();
			String sql = "INSERT INTO TE_MEMBER_MATCH_LOG (ID,MATCH_ID,MEMBER_ID,CREATETIME) VALUES (?,?,?,?) ";
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			for (int i = 0; i < memberids.length; i++) {
				ps.setString(1,Common.GetUUID());
				ps.setString(2,matchid);
				ps.setLong(3,memberids[i]);
				ps.setString(4,DateUtil.getAllDate());
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

}
