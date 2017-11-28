package com.java.back.quartz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.java.back.constant.ClubConst;
import com.java.back.utils.DBhepler;

@Component
public class TaskJob {
	static Logger logger=Logger.getLogger(TaskJob.class);
	
	
	/**
	 * 执行定时任务(会员卡过期)
	 * @throws SQLException
	 */
	public void CardJob() throws SQLException {
		Connection conn = DBhepler.getConnection();
		System.out.println("定时任务每晚11点=========");
		String sql = "SELECT ID FROM TE_MEMBER_CARD WHERE  DATE_FORMAT(CURDATE(),'%Y-%m-%d')=EXPIREDATE and ifuseless!="+ClubConst.STATE_OVERDUE;
		String upsql = "UPDATE TE_MEMBER_CARD SET IFUSELESS=? WHERE ID=?";
		ResultSet query = DBhepler.query(sql);
		conn.setAutoCommit(false);
		PreparedStatement ps = conn.prepareStatement(upsql);
		while (query.next()) {
			System.out.println(query.getString(1));
			ps.setInt(1, ClubConst.STATE_OVERDUE);//过期了
			ps.setString(2, query.getString(1));
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
	}
	
	/**
	 * 会员卡今天到期的提醒
	 */
	private void cardWarn(){
		String sql = "SELECT MEMBERID ,CARDTYPE,ID FROM TE_MEMBER_CARD WHERE  DATE_FORMAT(CURDATE(),'%Y-%m-%d')=EXPIREDATE and ifuseless!="+ClubConst.STATE_OVERDUE;
		ResultSet query = DBhepler.query(sql);
		try {
			while (query.next()) {
				System.out.println(query.getLong(1)+"==============="+query.getString(2)+"==============="+query.getString(3));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 比赛今天即将开始的提醒
	 */
	public void matchJob(){
		System.out.println("定时任务每天8点执行=========");
		cardWarn();
		String sql = "SELECT LOG.MEMBER_ID,M.NAME,M.MATCHTIME,M.ID FROM TE_MATCH M,TE_MEMBER_MATCH_LOG LOG WHERE LOG.MATCH_ID=M.ID AND DATE_FORMAT(CURDATE(),'%Y-%m-%d')=str_to_date(M.MATCHTIME, '%Y-%m-%d') and M.STATE="+ClubConst.M_NOTSTARTED;
		ResultSet query = DBhepler.query(sql);
		try {
			while (query.next()) {
				System.out.println("比赛:"+query.getLong(1)+"==="+query.getString(2)+"==="+query.getString(3)+"==="+query.getString(4));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
