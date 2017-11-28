package com.java.back.controller.club;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.back.annotation.SecureValid;
import com.java.back.constant.MethodType;
import com.java.back.controller.support.AbstractController;
import com.java.back.model.club.TeCard;
import com.java.back.service.CardService;
import com.java.back.support.JSONReturn;

/**
 * 会员卡类型管理
 * 
 */
@Controller
public class CardController extends AbstractController {

	@Autowired
	private CardService cardService;

	/**
	 * 接收form表单序列化之后的数据
	 * <br/>
	 * springmvc自动转为对象,进行保存操作
	 * @param request
	 * @param response
	 * @param card
	 * @return
	 */
	@SecureValid(code = "", desc = "添加卡类信息", type = MethodType.ADD)
	@RequestMapping(value="addCard",method=RequestMethod.POST)
	@ResponseBody
	public JSONReturn addCard(HttpServletRequest request,
			HttpServletResponse response,TeCard card) {
		return cardService.addCard(card);
	}

	@RequestMapping("modifyCard")
	@ResponseBody
	@SecureValid(code = "", desc = "修改卡类信息", type = MethodType.MODIFY)
	public JSONReturn modifyCard(HttpServletRequest request,
			HttpServletResponse response, TeCard card) {
		return cardService.modifyCard(card);
	}

	@RequestMapping("cardList")
	@ResponseBody
	public JSONReturn cardList(HttpServletRequest request,
			HttpServletResponse response,String searchInput) {
		return cardService.findCardListInfo(searchInput);
	}

	@SecureValid(code = "", desc = "删除卡类信息", type = MethodType.DELETE)
	@RequestMapping("delCard")
	@ResponseBody
	public JSONReturn delCard(HttpServletRequest request,
			HttpServletResponse response, Long id) {
		return cardService.deleteCard(id);
	}

	@RequestMapping("findCardById")
	@ResponseBody
	public JSONReturn findCardById(HttpServletRequest request,
			HttpServletResponse response, Long id) {
		return cardService.findCardById(id);
	}
	
	/**
	 * 所有卡类的下拉框
	 * @return
	 */
	@RequestMapping("findAllCard")
	@ResponseBody
	public JSONReturn findAllCard() {
		return cardService.findAllCard();
	}

}
