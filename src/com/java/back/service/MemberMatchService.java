package com.java.back.service;

import java.util.List;

import com.java.back.model.club.TeMemberMatchLog;
import com.java.back.support.JSONReturn;

public abstract interface MemberMatchService {
	
	/**
	 * 查询参赛记录
	 * @param mid
	 * @param searchVal 姓名,首字母,或者手机尾号
	 * @return
	 */
	public abstract JSONReturn findMeMatchList(String mid,String searchVal);

	/**
	 * 参加比赛
	 * @param memberMatchLog
	 * @return
	 */
	public abstract JSONReturn addMemberMatch(TeMemberMatchLog memberMatchLog);
	/**
	 * 多人参加
	 * @param matchid
	 * @param memberids
	 * @return
	 */
	public abstract JSONReturn addMemberMatchAll(String matchid,long [] memberids);
	/**
	 * 退出比赛
	 * @param id
	 * @return
	 */
	public abstract JSONReturn deleteMemberMatch(String id);
	/**
	 * 比赛名次
	 * @param id
	 * @param score
	 * @return
	 */
	public abstract JSONReturn matchScore(String id,int score);
	/**
	 * 上次比赛获胜,并且又参加了此次的比赛的人员
	 * @param matchId
	 * @return
	 */
	public List getLastTimeWinners(String matchId);

}
