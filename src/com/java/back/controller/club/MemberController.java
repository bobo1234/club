package com.java.back.controller.club;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.back.controller.support.AbstractController;
import com.java.back.model.club.TeMember;
import com.java.back.service.MemberService;
import com.java.back.support.JSONReturn;

@Scope
@Controller
public class MemberController extends AbstractController {

	@Autowired
	private MemberService memberService;

	@ResponseBody
	@RequestMapping(value = "findMemberListInfo")
	public JSONReturn findMemberListInfo(@RequestParam int page,
			String serVal,int sex) {
		return memberService.findList(page, serVal,sex);
	}

	@ResponseBody
	@RequestMapping(value = "findMemberPage")
	public JSONReturn findMemberPage(@RequestParam int page, String serVal,int sex) {
		return memberService.findCount(page, serVal,sex);
	}
	
	/**
	 * 新增会员(是否办卡,消费)
	 * @param member
	 * @param cardtype 卡类id
	 * @param ctimes 消费次数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addMember")
	public JSONReturn addMember(TeMember member,long cardtype,int ctimes) {
		return memberService.addMember(member,cardtype,ctimes);
	}

	@ResponseBody
	@RequestMapping(value = "deleteMember")
	public JSONReturn deleteMember(@RequestParam long id) {
		return memberService.deleteMember(id);
	}

	@ResponseBody
	@RequestMapping(value = "findMemberById")
	public JSONReturn findMemberById(@RequestParam long id) {
		return memberService.findMById(id);
	}

	@ResponseBody
	@RequestMapping(value = "modifyMember")
	public JSONReturn modifyMember(TeMember member) {
		return memberService.modifyMember(member);
	}
	/**
	 * 联想查询的下拉框
	 * @param keyword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findListByserVal")
	public JSONObject findListByserVal(String keyword) {
		return memberService.findListByserVal(keyword);
	}
	
	/**
	 * 比赛报名的会员列表
	 * @param searchVal
	 * @param griptype 握拍方式
	 * @param matchid 比赛id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findMemberAll")
	public JSONReturn findMemberAll(String searchVal,int griptype,String matchid) {
		long startTime = System.currentTimeMillis();//获取当前时间
		JSONReturn findAll = memberService.findAll(searchVal,griptype,matchid);
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间："+(endTime-startTime)+"ms");
		return findAll;
	}
}
