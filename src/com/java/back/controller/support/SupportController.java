package com.java.back.controller.support;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.java.back.annotation.SecureValid;
import com.java.back.constant.MethodType;
import com.java.back.constant.SessionKey;
import com.java.back.service.AccountService;
import com.java.back.service.DepartmentService;
import com.java.back.service.EmployeesService;
import com.java.back.service.ModuleService;
import com.java.back.service.PositionService;
import com.java.back.support.JSONReturn;
import com.java.back.support.RandomValidateCode;
import com.java.back.utils.StringUtil;
import com.java.back.utils.fileUtils.FileOperate;

@Scope
@Controller
public class SupportController extends AbstractController {

	@Autowired
	private ModuleService moduleService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private EmployeesService employeesService;

	/**
	 * 验证码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "0/findVerifydCode")
	public void findVerifydCode(HttpServletRequest request,
			HttpServletResponse response) {
		RandomValidateCode.getRandcode(request, response);
	}

	/**
	 * 用户登录
	 * 
	 * @param name
	 * @param pass
	 * @param verify
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "0/acctLogin")
	public JSONReturn login(@RequestParam String name,
			@RequestParam String pass, @RequestParam String verify,
			HttpServletRequest request) {
		String sessionVerify = (String) request.getSession().getAttribute(
				SessionKey.VALIDATE_CODE);
		if (StringUtils.isEmpty(sessionVerify)
				|| !verify.equalsIgnoreCase(sessionVerify))
			return JSONReturn.buildFailure("登录失败, 验证码出错!");
		return accountService.login(name, pass, request);
	}

	@ResponseBody
	@RequestMapping(value = "findBreadcrumb")
	public JSONReturn findBreadcrumb(String moduleCode) {
		return moduleService.findBreadcrumb(moduleCode);
	}

	/**
	 * 用户退出
	 * 
	 * @param httpSession
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "exit")
	public JSONReturn exit(HttpSession httpSession, HttpServletRequest request) {
		return accountService.exit(httpSession, request);
	}

	@SecureValid(code = "", desc = "用户修改密码", type = MethodType.MODIFY)
	@ResponseBody
	@RequestMapping(value = "mdoifyPass")
	public JSONReturn mdoifyPass(@RequestParam String password,
			HttpSession httpSession) {
		return accountService.mdoifyPass(password, acctName(httpSession));
	}

	/**
	 * 获取所有部门列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findDepartment")
	public JSONReturn findDepartment() {
		return departmentService.findAllDepartment();
	}

	/**
	 * 根据部门ID号获取职位列表
	 * 
	 * @param deptId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findPositionByDeptId")
	public JSONReturn findPositionByDeptId(@RequestParam long deptId) {
		return positionService.findPositionByDeptId(deptId);
	}

	/**
	 * 上传图片接口
	 * 
	 * @param imgFile
	 * @param request
	 * @param response
	 * @return rootPath + "/upload/img/" + imgName
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "uploadImg")
	public Map<String, Object> uploadImg(MultipartFile imgFile,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONReturn jsonReturn = employeesService.uploadImg(imgFile, request,
				response);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", jsonReturn.isHead() ? 0 : 1);
		map.put(jsonReturn.isHead() ? "url" : "message", jsonReturn.getBody());
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "findEmployeesRecord")
	@SecureValid(type = MethodType.FIND, desc = "获取员工相关记录", code = { "01001",
			"01002", "01004" })
	public JSONReturn findEmployeesRecord(@RequestParam long emplId) {
		return employeesService.findEmployeesRecord(emplId);
	}

	/**
	 * 获取当前session里的用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSessionName")
	public JSONReturn getSessionUser(HttpServletRequest request,
			HttpServletResponse response) {
		String userName = (String) request.getSession().getAttribute(
				SessionKey.MODULEACCTNAME);
		return StringUtils.isNotBlank(userName) || !userName.equals("UNLOGIN") ? JSONReturn
				.buildSuccess(userName) : JSONReturn
				.buildFailureWithEmptyBody();
	}

	/***
	 * 上传文件 用@RequestParam注解来指定表单上的file为MultipartFile
	 * 
	 * @param file
	 * @return 成功:返回根据时间戳重新命名的文件路径信息
	 *         <p/>
	 *         失败:返回上传失败
	 */
	@RequestMapping("fileUpload")
	@ResponseBody
	public JSONReturn fileUpload(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
		// 判断文件是否为空
		if (!file.isEmpty()) {
			String uploadFile = FileOperate.UploadFile(file, request);
			if (StringUtil.isNotEmpty(uploadFile)) {
				return JSONReturn.buildSuccess(uploadFile);
			}
		}
		return JSONReturn.buildFailure("上传失败");
	}

	@ResponseBody
	@RequestMapping(value = "getSessionFlag")
	public String getSessionFlag(HttpServletRequest request,
			HttpServletResponse response) {
		String flag = (String) request.getSession().getAttribute("flag");
		return StringUtils.isEmpty(flag) ? "" : flag;
	}

	@RequestMapping(value = "setSessionFlag")
	public void setSessionFlag(HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().setAttribute("flag", "1");
	}

}
