package com.java.back.controller.club;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.java.back.utils.ResponseUtil;
import com.java.back.utils.excel.ExcelExport;

/**
 * 比赛管理
 * 
 * @author JYD
 * 
 */
@Scope
@Controller
public class MatchController extends AbstractController {

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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
	 * @param mid
	 * @param searchVal
	 *            姓名,首字母,或者手机尾号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findMeMatchList")
	public JSONReturn findMeMatchList(String mid, String searchVal) {
		return memberMatchService.findMeMatchList(mid, searchVal);
	}

	/**
	 * 会员参加比赛
	 * 
	 * @param match
	 * @param members
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addMemberMatch")
	public JSONReturn addMemberMatch(String matchid, @RequestParam long memberId) {
		TeMatch match = new TeMatch();
		TeMember members = new TeMember();
		match.setId(matchid);
		members.setId(memberId);
		TeMemberMatchLog memberMatchLog = new TeMemberMatchLog();
		memberMatchLog.setMatch(match);
		memberMatchLog.setMembers(members);
		return memberMatchService.addMemberMatch(memberMatchLog);
	}

	/**
	 * 搜索的所有会员全部参加
	 * 
	 * @param matchid
	 * @param memberId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addMemberMatchAll")
	public JSONReturn addMemberMatchAll(String matchid,
			@RequestParam(value = "memberIds[]") long[] memberIds) {
		return memberMatchService.addMemberMatchAll(matchid, memberIds);
	}

	/**
	 * 会员退出比赛
	 * 
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
	 * 
	 * @param id
	 * @param score
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "matchScore")
	public JSONReturn matchScore(String id, int score) {
		return memberMatchService.matchScore(id, score);
	}

	/**
	 * 导出参赛人员 将参赛人员分成A B C D 四个组,A、B两组为上半区，C、D两组为下半区 小组赛：采取单循环赛，小组前两名选手进入下一轮
	 * 1/4决赛
	 * ：每个半区进行组间交叉淘汰赛，例如上半区中A组的第一对垒B组的第二，A组的第二对垒B组的第一，下半区采取同样的交叉淘汰方式，获胜方进入下一轮
	 * 半决赛：上半区获胜的两位选手进行对垒，上半区获胜的两位选手进行对垒，各半区获胜者进入决赛 决赛：获胜者获得本次比赛冠军
	 * 
	 * @param memhid
	 */
	@RequestMapping(value = "exportMember")
	public void exportMember(String ids, String mid,
			HttpServletResponse response) {
		List mlist = memberMatchService.getLastTimeWinners(mid);
		String cnames = "";
		for (int i = 0; i < mlist.size(); i++) {
			cnames += mlist.get(i) + ",";
		}
		String[] str = ids.split(",");
		try {
			if (str.length < 8) {
				ResponseUtil.write(response, "参赛人数太少,无法分组!");
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> list = Arrays.asList(str);
		List<String> linkList = new LinkedList<String>();
		linkList.addAll(list);
		System.out.println(cnames);
		
		/**
		 * 从所有参赛的与人员里移除之前获奖的人员
		 */
		for (int i = 0; i < linkList.size(); i++) {
			if (cnames.contains(linkList.get(i))) {
				linkList.remove(i);
				i--;
			}
		}
		
		/**
		 * 将所有人打散再分成四组
		 */
		List<List<String>> randomGroup = Common.randomGroup(linkList);
		
		/**
		 * 剩下的人员分组,再将之前移除的获奖人员分发到四组中(保证公平性)
		 */
		Collections.reverse(randomGroup);// 倒叙输出,人员少的在前
		for (int i = 0; i < mlist.size(); i++) {
			randomGroup.get(i).add(mlist.get(i).toString());
		}
		List list2 = new ArrayList();
		List<Object[]> dataset = null;
		for (int i = 0; i < randomGroup.size(); i++) {// 四组
			dataset = new ArrayList<Object[]>();
			for (int x = 0; x < randomGroup.get(i).size(); x++) {
				for (int y = 0; y <= x - 1; y++) {
					String[] obj = new String[1];
					System.out.print(randomGroup.get(i).get(y) + "--"
							+ randomGroup.get(i).get(x) + "\t");
					obj[0] = randomGroup.get(i).get(y) + "--"
							+ randomGroup.get(i).get(x);
					dataset.add(obj);
				}
				System.out.println();
			}
			list2.add(dataset);
		}
		String[] headers = { "对阵人员", "比分结果", "胜者签字", "备注" };
		String[] sheetsName = { "A组", "B组", "C组", "D组" };
		ExcelExport eu = new ExcelExport();
		eu.exportExcel(list2, headers, sheetsName, "比赛对阵分布表", response);

	}

}
