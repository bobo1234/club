package com.java.back.controller.club;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.back.constant.ClubConst;
import com.java.back.controller.support.AbstractController;
import com.java.back.model.club.TeMatch;
import com.java.back.model.club.TeMember;
import com.java.back.model.club.TeMemberMatchLog;
import com.java.back.service.MatchService;
import com.java.back.service.MemberMatchService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.Common;

/**
 * 比赛管理
 * @author JYD
 *
 */
@Scope
@Controller
public class MatchController extends AbstractController{

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private MemberMatchService memberMatchService;

	@ResponseBody
	@RequestMapping(value = "findMatchListInfo")
	public JSONReturn findMatchListInfo(@RequestParam int page,
			String searchValue) {
		return matchService.findMatchList(page, searchValue);
	}

	@ResponseBody
	@RequestMapping(value = "findMatchPage")
	public JSONReturn findMatchPage(@RequestParam int page, String searchValue) {
		return matchService.findMatchCount(page, searchValue);
	}

	/**
	 * 新建比赛
	 * @param match
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addMatch")
	public JSONReturn addMatch(TeMatch match) {
		return matchService.addMatch(match);
	}

	/**
	 * 删除比赛
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteMatch")
	public JSONReturn deleteMatch(@RequestParam String id) {
		return matchService.deleteMatch(id);
	}

	@ResponseBody
	@RequestMapping(value = "findMatchById")
	public JSONReturn findMatchById(@RequestParam String id) {
		return matchService.findMatchById(id);
	}

	/**
	 * 修改比赛
	 * @param Match
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "modifyMatch")
	public JSONReturn modifyMatch(TeMatch Match) {
		return matchService.modifyMatch(Match);
	}
	
	/**
	 * 取消比赛
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "stopMatch")
	public JSONReturn stopMatch(String id) {
		return matchService.modifyState(id, ClubConst.M_CANCEL);
	}
	
	/**
	 * 恢复比赛,比赛未开始状态
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "recovery")
	public JSONReturn recovery(String id) {
		return matchService.modifyState(id, ClubConst.M_NOTSTARTED);
	}
	
	/**
	 * 开始比赛
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "startMatch")
	public JSONReturn startMatch(String id) {
		return matchService.modifyState(id, ClubConst.M_ALREADYSTARTED);
	}
	
	/**
	 * 参赛人员列表
	 * @param mid
	 * @param searchVal 姓名,首字母,或者手机尾号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findMeMatchList")
	public JSONReturn findMeMatchList(String mid,String searchVal) {
		return memberMatchService.findMeMatchList(mid,searchVal);
	}
	
	/**
	 * 会员参加比赛
	 * @param match
	 * @param members
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addMemberMatch")
	public JSONReturn addMemberMatch(String matchid,@RequestParam long memberId) {
		TeMatch match=new TeMatch();
		TeMember members=new TeMember();
		match.setId(matchid);
		members.setId(memberId);
		TeMemberMatchLog memberMatchLog=new TeMemberMatchLog();
		memberMatchLog.setMatch(match);
		memberMatchLog.setMembers(members);
		return memberMatchService.addMemberMatch(memberMatchLog);
	}
	
	/**
	 * 搜索的所有会员全部参加
	 * @param matchid
	 * @param memberId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addMemberMatchAll")
	public JSONReturn addMemberMatchAll(String matchid,@RequestParam(value = "memberIds[]") long [] memberIds) {
		return memberMatchService.addMemberMatchAll(matchid, memberIds);
	}
	
	/**
	 * 会员退出比赛
	 * @param memhid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteMemberMatch")
	public JSONReturn deleteMemberMatch(String memhid) {
		return memberMatchService.deleteMemberMatch(memhid);
	}
	
	/**
	 * 填写比赛名次
	 * @param id
	 * @param score
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "matchScore")
	public JSONReturn matchScore(String id,int score) {
		return memberMatchService.matchScore(id, score);
	}
	
	/**
	 * 导出参赛人员
	 * 将参赛人员分成A B C D 四个组,A、B两组为上半区，C、D两组为下半区
	 * 小组赛：采取单循环赛，小组前两名选手进入下一轮
	 * 1/4决赛：每个半区进行组间交叉淘汰赛，例如上半区中A组的第一对垒B组的第二，A组的第二对垒B组的第一，下半区采取同样的交叉淘汰方式，获胜方进入下一轮 
	 * 半决赛：上半区获胜的两位选手进行对垒，上半区获胜的两位选手进行对垒，各半区获胜者进入决赛 
	 * 决赛：获胜者获得本次比赛冠军
	 * @param memhid
	 */
	@RequestMapping(value = "exportMember")
	public void exportMember(String ids) {
		String [] str=ids.split(",");
		List<String> list = Arrays.asList(str);
		List<List<String>> randomGroup = Common.randomGroup(list);
		for (int i = 0; i < randomGroup.size(); i++) {
			System.out.println(randomGroup.get(i));
		}
		
		
	}

}
