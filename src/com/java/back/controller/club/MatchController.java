package com.java.back.controller.club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.back.controller.support.AbstractController;
import com.java.back.model.club.TeMatch;
import com.java.back.model.club.TeMember;
import com.java.back.model.club.TeMemberMatchLog;
import com.java.back.service.MatchService;
import com.java.back.service.MemberMatchService;
import com.java.back.support.JSONReturn;

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
		return matchService.stopMatch(id);
	}
	
	/**
	 * 恢复比赛,比赛未开始状态
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "recovery")
	public JSONReturn recovery(String id) {
		return matchService.recovery(id);
	}
	
	/**
	 * 参赛人员列表
	 * @param mid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findMeMatchList")
	public JSONReturn findMeMatchList(String mid) {
		return memberMatchService.findMeMatchList(mid);
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

}
