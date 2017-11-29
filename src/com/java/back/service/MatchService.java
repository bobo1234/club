package com.java.back.service;

import com.java.back.model.club.TeMatch;
import com.java.back.support.JSONReturn;

public abstract interface MatchService {

	public abstract JSONReturn findMatchList(int page, String searchValue);

	public abstract JSONReturn findMatchCount(int page, String searchValue);

	public abstract JSONReturn modifyMatch(TeMatch match);

	public abstract JSONReturn addMatch(TeMatch match);

	public abstract JSONReturn deleteMatch(String id);
	/**
	 * 查询所有的比赛,根据开始时间排序
	 * @return
	 */
	public abstract JSONReturn findAllMatch();
	
	public abstract JSONReturn findMatchById(String id);
	
	/**
	 * 修改比赛状态
	 * @param id
	 * @param state
	 * @return
	 */
	public abstract JSONReturn modifyState(String id,int state);

}
