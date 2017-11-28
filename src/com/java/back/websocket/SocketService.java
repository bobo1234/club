package com.java.back.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONObject;

/**
 * websocket
 * 
 * @author jyd
 * 
 */
@ServerEndpoint("/socketservice/{userName}")
public class SocketService {

	private static Map<String, Session> sessionMap = new HashMap<String, Session>();// 在线的客户端session集合，只在第一次new的时候初始化。

	/**
	 * 接收信息事件
	 * 
	 * @param message
	 *            客户端发来的消息
	 * @param session
	 *            当前会话
	 */
	@OnMessage
	public void onMessage(String message, Session session,
			@PathParam(value = "userName") String userName) throws Exception {
		System.out.println("用户" + userName + "说：" + message + "。");
		String mes = message.split("\\|\\|\\|\\|")[1];// 消息
		if (message.length() > 0) {
			if (message.startsWith("[join]")) {// 上线
				checkLogin(mes,session);
				sendAdminLogin(mes);
			} else if (message.startsWith("[logout]")) {// 下线
				sendAdminLoginOut(mes);
			} else if (message.startsWith("[getUserlist]")) {// 用户列表
				userList();
			} else if (message.startsWith("[ALL]")) {// 发送消息
				sendAll();
			}

		}

		// Session value = sessionMap.get(sd);
		// if (value == null) {
		// System.out.println("-------没有该用户-----");
		// session.getBasicRemote().sendText("没有用户");// 提示信息
		// } else {
		// if (value.isOpen()) {
		// value.getBasicRemote().sendText(userName + ":" + mes);
		// session.getBasicRemote().sendText(userName + ":" + mes);
		// }
		// }

		// else {
		// System.out.println("-----发送所有人------");
		// for (String key : sessionMap.keySet()) {
		// Session value = sessionMap.get(key);
		// if (value.isOpen()) {
		// value.getBasicRemote().sendText(userName + ":" + mes);
		// }
		// }
		// }

	}

	/**
	 * 打开连接事件
	 * 
	 * @throws Exception
	 */
	@OnOpen
	public void onOpen(Session session,
			@PathParam(value = "userName") String userName) throws Exception {
		 System.out.println("打开连接成功！");
		// sessionMap.put(userName, session);
		 System.out.println("用户" + userName + "进来了。。。");
		// System.out.println("当前在线人数：" + sessionMap.size());
	}

	/**
	 * 检查登录
	 * 
	 * @param username
	 * @throws IOException
	 */
	private void checkLogin(String username, Session session)
			throws IOException {
		Session value = sessionMap.get(username);
		if (value == null) {
			sessionMap.put(username, session);
		} else {//下线
			JSONObject result = new JSONObject();
			result.element("type", "goOut");
			result.element("mess", "goOut");
			session.getBasicRemote().sendText(result.toString());
		}
		System.out.println("当前在线人数：" + sessionMap.size());
	}

	/**
	 * 通知管理员有人下线
	 * 
	 * @throws IOException
	 */
	private void sendAdminLoginOut(String username) throws IOException {
		Session value = sessionMap.get("admin");
		if (value != null) {
			if (value.isOpen()) {
				JSONObject result = new JSONObject();
				result.element("type", "user_leave");
				result.element("mess", username);
				value.getBasicRemote().sendText(result.toString());
			}
		}
	}

	/**
	 * 通知管理员有人上线啦
	 * 
	 * @throws IOException
	 */
	private void sendAdminLogin(String username) throws IOException {
		Session value = sessionMap.get("admin");
		if (value != null) {
			if (value.isOpen()) {
				JSONObject result = new JSONObject();
				result.element("type", "user_join");
				result.element("mess", username);
				value.getBasicRemote().sendText(result.toString());
			}
		}
	}

	/**
	 * 获取用户列表
	 * 
	 * @throws IOException
	 */
	private void userList() throws IOException {
		Set<String> set = sessionMap.keySet();
		Session value = sessionMap.get("admin");
		if (value != null) {
			if (value.isOpen()) {
				JSONObject result = new JSONObject();
				result.element("type", "user_list");
				result.element("mess", set);
				value.getBasicRemote().sendText(result.toString());
			}
		}
	}

	/**
	 * 通知所有人
	 * 
	 * @throws IOException
	 */
	private void sendAll() throws IOException {
		Set<String> set = sessionMap.keySet();
		for (String key : sessionMap.keySet()) {
			Session value = sessionMap.get(key);
			if (value.isOpen()) {
				JSONObject result = new JSONObject();
				result.element("type", "user_join");
				result.element("mess", set);
				value.getBasicRemote().sendText(result.toString());
			}

		}
	}

	/**
	 * 关闭连接事件
	 */
	@OnClose
	public void onClose(Session session,
			@PathParam(value = "userName") String userName) {
		System.out.println("关闭连接成功！");
		System.out.println("用户" + userName + "离开了。。。");
		sessionMap.remove(userName);
		System.out.println("当前在线人数：" + sessionMap.size());
	}

	/**
	 * 错误信息响应事件
	 * 
	 * @param session
	 * @param throwable
	 */
	@OnError
	public void OnError(Session session, Throwable throwable,
			@PathParam(value = "userName") String userName) {
		System.out.println("异常：" + throwable.getMessage());
		System.out.println("用户" + userName + "的连接出现了错误。。。");
		System.out.println("当前在线人数：" + sessionMap.size());
	}

}