package com.java.back.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.back.annotation.SecureValid;
import com.java.back.constant.MethodType;
import com.java.back.controller.support.AbstractController;
import com.java.back.model.TeModule;
import com.java.back.service.ModuleService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.TreeList;

/**
 * 菜单操作
 * 
 * @author JYD
 * 
 */
@Scope
@Controller
public class ModuleController extends AbstractController {

	@Autowired
	private ModuleService moduleService;

	@ResponseBody
	@RequestMapping(value = "findMenu")
	public JSONReturn findMenu(HttpSession httpSession) {
		return moduleService.findMenu(acctName(httpSession));
	}

	@ResponseBody
	@RequestMapping(value = "findModuleParameter")
	public JSONReturn findModuleParameter(@RequestParam String moduleCode,
			HttpSession httpSession) {
		return moduleService.findModuleParameter(moduleCode,
				acctName(httpSession));
	}
	@ResponseBody
	@RequestMapping(value = "addModule")
	@SecureValid(code = "", desc = "添加菜单信息", type = MethodType.ADD)
	public JSONReturn addModule(TeModule module, HttpSession httpSession) {
		return moduleService.saveModule(module);
	}
	@ResponseBody
	@RequestMapping(value = "delModule")
	@SecureValid(code = "", desc = "删除菜单信息", type = MethodType.DELETE)
	public JSONReturn delModule(String id, HttpSession httpSession) {
		return moduleService.deleteModule(id);
	}
	@ResponseBody
	@RequestMapping(value = "updateModule")
	@SecureValid(code = "", desc = "修改菜单信息", type = MethodType.MODIFY)
	public JSONReturn updateModule(TeModule module, HttpSession httpSession) {
		System.out.println(module.toString());
		return moduleService.updateModule(module);
	}
	@ResponseBody
	@RequestMapping(value = "findByid")
	public JSONReturn findByid(Long id, HttpSession httpSession) {
		System.out.println(id);
		return moduleService.findByid(id);
	}
	
	/**
	 * 根据page查询code
	 * @param page
	 * @param httpSession
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findByPage")
	public JSONReturn findByPage(String page, HttpSession httpSession) {
		return moduleService.findByPage(page);
	}
	
	@ResponseBody
	@RequestMapping(value = " getAllMenu")
	@SecureValid(code = "", desc = "查询所有菜单信息", type = MethodType.FIND)
	public Map<String,Object> getAllMenu(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TeModule> list=moduleService.getAllMenu();
		TreeList tree = new TreeList(list);
		List<TeModule> listTree = tree.buildTree();
		map.put("rows", listTree);
		map.put("total", listTree.size());
		return map;
	}
}
