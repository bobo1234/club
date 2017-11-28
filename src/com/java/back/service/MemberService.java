package com.java.back.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.java.back.model.club.TeMember;
import com.java.back.support.JSONReturn;

public abstract interface MemberService {

	/**
	 * 查询所有的会员列表
	 * @param page
	 * @param serVal 手机号或者名字
	 * @param sex
	 * @return
	 */
	public abstract JSONReturn findList(int page, String serVal,int sex);

	/**
	 * 联想下拉框查询的接口
	 * @param serVal 可以是:名字首字母,名字,手机号
	 * @return
	 */
	public abstract JSONObject findListByserVal(String serVal);
	
	public abstract JSONReturn findCount(int page, String serVal,int sex);

	public abstract JSONReturn findMById(long id);
	
	/**
	 * 修改会员信息
	 * @param member
	 * @return
	 */
	public abstract JSONReturn modifyMember(TeMember member);
	/**
	 * 新增会员(保存是否办卡与消费)
	 * @param member
	 * @param cardtypeid 卡类id
	 * @param ctimes 消费次数
	 * @return
	 */
	public abstract JSONReturn addMember(TeMember member,long cardtypeid,int ctimes);
	
	/**
	 * 删除会员信息(级联删除办卡记录和消费记录)
	 * @param id
	 * @return
	 */
	public abstract JSONReturn deleteMember(long id);
	
	/**
	 * 查询所有的未参加此项比赛的会员
	 * @param serVal
	 * @param griptype
	 * @param matchid
	 * @return
	 */
	public abstract JSONReturn findAll(String serVal,int griptype,String matchid);

}
