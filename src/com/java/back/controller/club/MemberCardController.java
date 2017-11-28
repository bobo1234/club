package com.java.back.controller.club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.back.controller.support.AbstractController;
import com.java.back.model.club.TeMemberCard;
import com.java.back.service.MemberCardService;
import com.java.back.service.MemberConsumeService;
import com.java.back.support.JSONReturn;

@Scope
@Controller
public class MemberCardController extends AbstractController {

	@Autowired
	private MemberCardService memberCardService;
	
	@Autowired
	private MemberConsumeService memberConsumeService;

	@ResponseBody
	@RequestMapping(value = "findMemberCardListInfo")
	public JSONReturn findMemberCardListInfo(@RequestParam int page,
			String serVal,long cardid,int ifuseless) {
		return memberCardService.findList(page, serVal,cardid,ifuseless);
	}

	@ResponseBody
	@RequestMapping(value = "findMemberCardPage")
	public JSONReturn findMemberCardPage(@RequestParam int page, String serVal,long cardid,int ifuseless) {
		return memberCardService.findCount(page, serVal,cardid,ifuseless);
	}
	
	/**
	 * 查询用户办卡历史
	 * @param memberid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findMemberCardByMid")
	public JSONReturn findMemberCardByMid(@RequestParam long memberid) {
		return memberCardService.findListByMid(memberid);
	}
	
	/**
	 * 查询会员卡的消费历史
	 * @param membercardid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findConsumeList")
	public JSONReturn findConsumeList(@RequestParam String membercardid) {
		return memberConsumeService.findConsumeList(membercardid);
	}

	/**
	 * 删除会员卡
	 * @param mcdid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleMCard")
	public JSONReturn deleMCard(String mcdid) {
		return memberCardService.deleCard(mcdid);
	}
	/**
	 * 消费会员卡
	 * @param memberCard
	 * @param ctimes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "consumeCard")
	public JSONReturn consumeCard(String mcdid,int ctimes) {
		return memberConsumeService.consumeCard(mcdid, ctimes);
	}
	
	/**
	 * 会员再次办理会员卡
	 * @param memberCard
	 * @param ctimes 消费次数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "transactCard")
	public JSONReturn transactCard(TeMemberCard memberCard,int ctimes) {
		return memberCardService.addMemberCard(memberCard, ctimes);
	}
}
