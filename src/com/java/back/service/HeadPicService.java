package com.java.back.service;

import javax.servlet.http.HttpServletRequest;

import com.java.back.model.forum.TeHeadpic;
import com.java.back.support.JSONReturn;

public abstract interface HeadPicService {

	public abstract TeHeadpic findPicById(long picid);
	
	public abstract JSONReturn modefyPicById(long id,int ifuss);

	public abstract JSONReturn addPic(TeHeadpic headpic);

	/**
	 * 删除图片,对应文件也要删除
	 * @param picid
	 * @param request
	 * @return
	 */
	public abstract JSONReturn deletePic(long picid,HttpServletRequest request);

	/**
	 * 未启用的图片列表
	 * @return
	 */
	public abstract JSONReturn findAllPic(String title);
	/**
	 * 前移一位
	 * @param id
	 * @return
	 */
	public abstract JSONReturn moveHead(long id);
	/**
	 * 在使用的图片列表
	 * @return
	 */
	public abstract JSONReturn findUsePic();

}
