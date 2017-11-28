package com.java.back.controller.forum;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.back.controller.support.AbstractController;
import com.java.back.model.forum.TeHeadpic;
import com.java.back.service.HeadPicService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.DateTimeUtil;
import com.java.back.utils.StringUtil;
import com.java.back.utils.fileUtils.FileOperate;

@Scope
@Controller
public class HeadpicController extends AbstractController {

	@Autowired
	private HeadPicService headPicService;

	/**
	 * 首页图片上传
	 * 
	 * @param request
	 * @param response
	 * @param img
	 * @return
	 */
	@RequestMapping("HeadPicUpload")
	@ResponseBody
	public JSONReturn HeadPicUpload(HttpServletRequest request,
			HttpServletResponse response, String img, String title) {
		if (StringUtil.isEmpty(img)) {
			return JSONReturn.buildFailure("文件不存在");
		}
		String rootPath = request.getSession().getServletContext()
				.getRealPath("");
		String bm = "upload/img/"
				+ DateTimeUtil.conversionTime(new Date(), "yyyyMMddHHmmss")
				+ ".jpg";
		String extensionName = rootPath + bm;
		boolean base64ToImage = FileOperate.base64ToImage(
				img.substring(img.indexOf(";") + 1, img.length()),
				extensionName);
		if (!base64ToImage) {
			return JSONReturn.buildFailure("上传失败");
		}
		TeHeadpic headpic = new TeHeadpic();
		headpic.setPicurl(bm);
		headpic.setTitle(title);
		return headPicService.addPic(headpic);
	}

	/**
	 * 未启用的图片列表
	 * 
	 * (0:用,1:废弃)
	 * @return
	 */
	@RequestMapping("findAllPic")
	@ResponseBody
	public JSONReturn findAllPic(String title) {
		return headPicService.findAllPic(title);
	}
	
	/**
	 * 启用的图片列表(按照顺序排序)
	 * @return
	 */
	@RequestMapping("findUsePic")
	@ResponseBody
	public JSONReturn findUsePic() {
		return headPicService.findUsePic();
	}

	/**
	 * 删除图片
	 * @param request
	 * @param picid
	 * @return
	 */
	@RequestMapping("delPic")
	@ResponseBody
	public JSONReturn delPic(HttpServletRequest request, Long picid) {
		return headPicService.deletePic(picid, request);
	}

	@RequestMapping("findPicById")
	@ResponseBody
	public JSONReturn findPicById(HttpServletRequest request, Long picid) {
		TeHeadpic teHeadpic = headPicService.findPicById(picid);
		if (teHeadpic == null)
			return JSONReturn.buildFailure("未获取到数据!");
		return JSONReturn.buildSuccess(teHeadpic);
	}
	
	/**
	 * 前移一位
	 * @param request
	 * @param picid
	 * @return
	 */
	@RequestMapping("moveHead")
	@ResponseBody
	public JSONReturn moveHead(HttpServletRequest request, Long picid) {
		return headPicService.moveHead(picid);
	}

	/**
	 * 启用或禁用  是否启用(0:可用,1:废弃)
	 * 
	 * @param request
	 * @param picid
	 * @param ifuss
	 * @return
	 */
	@RequestMapping("updatepic")
	@ResponseBody
	public JSONReturn updatepic(HttpServletRequest request, Long picid,
			int ifuss) {
		return headPicService.modefyPicById(picid, ifuss);
	}
}
