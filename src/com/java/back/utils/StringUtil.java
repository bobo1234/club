package com.java.back.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 字符串工具类
 * 
 * @author
 * 
 */
public class StringUtil {
	public static String fznull(String str) {
		String message = (str == null) ? "" : str;
		return message;
	}

	/**
	 * @Description: 判断对象是否为空
	 * @author LiHaoyang
	 * @date 2015-9-12 下午02:16:17
	 * @param object
	 * @return true=空,false=不为空;
	 */
	public static boolean isEmpty(Object object) {
		if (object == null
				|| "".equals(object)
				|| "undefined".equals(object)
				|| "null".equals(object)
				|| (object instanceof java.util.List && (((List<?>) object)
						.size() == 0))
				|| (object instanceof java.util.Map && (((Map<?, ?>) object)
						.size() == 0))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否不是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if ((str != null) && !"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 组装时间sql
	 * 
	 * @param field
	 *            时间字段的名称
	 * @param beginTime
	 *            (格式为:2017-10-20)
	 * @param endTime
	 *            (格式为:2017-10-22)
	 * @return
	 */
	public static String formatInterDate(String field, String beginTime,
			String endTime) {
		String sql = " and " + field + " >'" + beginTime + " 00:00:00' and "
				+ field + "<'" + endTime + " 24:59:59'";
		return sql;
	}

	/**
	 * 格式化模糊查询
	 * 
	 * @param str
	 * @return
	 */
	public static String formatLike(String str) {
		if (isNotEmpty(str)) {
			return " like '%" + str + "%' ";
		} else {
			return null;
		}
	}
	/**
	 * end格式化模糊查询
	 * 
	 * @param str
	 * @return
	 */
	public static String formatEndLike(String str) {
		if (isNotEmpty(str)) {
			return " like '%" + str + "' ";
		} else {
			return null;
		}
	}

	/**
	 * 相等查询
	 * 
	 * @param str
	 * @return
	 */
	public static String formatEqual(String str) {
		if (isNotEmpty(str)) {
			return " ='" + str + "' ";
		} else {
			return null;
		}
	}

	/**
	 * 不等查询
	 * 
	 * @param str
	 * @return
	 */
	public static String formatNotEqual(String str) {
		if (isNotEmpty(str)) {
			return " !='" + str + "' ";
		} else {
			return null;
		}
	}

	/**
	 * in查询
	 * 
	 * @param str
	 * @return
	 */
	public static String formatIn(String str) {
		if (isNotEmpty(str)) {
			String[] strs = str.split(",");
			String temp = " in ";
			temp += "(";
			for (int i = 0; i < strs.length; i++) {
				if (i > 0) {
					temp += ", ";
				}
				temp += "'" + strs[i].toString() + "'";
			}
			temp += ") ";
			return temp;
		} else {
			return null;
		}
	}

	/**
	 * Like in查询
	 * 
	 * @param str
	 * @return
	 */
	public static String formatLikeIn(String str, String field) {
		if (isNotEmpty(str)) {
			String[] strs = str.split(",");
			String temp = "( ";
			for (int i = 0; i < strs.length; i++) {
				temp += field + " like '%" + strs[i] + "%' or ";
			}
			return temp.substring(0, temp.length() - 3) + ")";
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		String sql = formatLikeIn("1,2,3,4", "name");
		System.out.println(sql);
	}

	/**
	 * 组装手机端接收的json格式数据
	 * 
	 * @param status
	 *            0：表示成功； 1：表示失败； 2：表示空结果
	 * @param size
	 * @param result
	 * @return
	 */
	public static String makeJsonData(int status, int size, String result) {
		String postData = "{\"STATUS\":\"%s\",\"total\":\"%s\",\"result\":%s }";
		if (size == 0) {
			postData = "{\"STATUS\":\"2\"}";// 空数据
			return postData;
		}
		String json = String.format(postData, status, size, result);
		return json;
	}

	/**
	 * 组装json格式数据
	 * 
	 * @param status
	 * @return
	 */
	public static String makeJsonData(int status, String ERRCODE, String ERRMSG) {
		String postData = "{\"STATUS\":\"%s\",\"ERRCODE\":\"%s\",\"ERRMSG\":%s }";
		String json = String.format(postData, status, ERRCODE, ERRMSG);
		return json;
	}

	/**
	 * 转换ids 传入list 返回 ('','id1','id2')的形式
	 * 
	 * @return
	 */
	public static String convertIds(List ids) {
		String temp = "";
		if (ids == null || ids.size() == 0) {
			return "('no value')";// 如果没有提供任何数据，就返回这个固定字符串。解决in 和 not in 的情况。
		}
		temp += "(";
		for (int i = 0; i < ids.size(); i++) {
			if (i > 0) {
				temp += ", ";
			}
			temp += "'" + ids.get(i).toString() + "'";
		}
		temp += ")";

		return temp;
	}

	/**
	 * BASE64解密
	 * 
	 * @param s
	 * @return
	 */
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * BASE64加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	/**
	 * @Description 获取32位主键id
	 * @date 2017年6月16日
	 * @return
	 */
	public static String getPrimaryKeyUuid() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 前台get请求中文乱码
	 */
	public static String encodeStr(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern
					.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 随机生成六位数验证码
	 * 
	 * @return
	 */
	public static int getRandomNum() {
		Random r = new Random();
		return r.nextInt(900000) + 100000;// (Math.random()*(999999-100000)+100000)
	}
}
