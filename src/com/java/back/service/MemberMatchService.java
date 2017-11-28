package com.java.back.service;

import com.java.back.model.club.TeMemberMatchLog;
import com.java.back.support.JSONReturn;

public abstract interface MemberMatchService {
	
	/**
	 * 查询参赛记录
	 * @param mid
	 * @return
	 */
	public abstract JSONReturn findMeMatchList(String mid);

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


}
