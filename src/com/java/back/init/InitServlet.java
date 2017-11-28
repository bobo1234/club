package com.java.back.init;

import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.java_websocket.WebSocketImpl;

import com.java.back.websocket.ChatServer;
import com.java.back.websocket.OnlineChatServer;

@SuppressWarnings("serial")
public class InitServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		System.out.println("系统初始化了============");
//		startWebsocketInstantMsg();
//		startWebsocketOnline();
//		
	}

	/**
	 * 启动即时聊天服务
	 */
	public void startWebsocketInstantMsg(){
		WebSocketImpl.DEBUG = false;
		ChatServer s;
		try {
			s = new ChatServer(8090);
			s.start();
			//System.out.println( "websocket服务器启动,端口" + s.getPort() );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 启动在线管理服务
	 */
	public void startWebsocketOnline(){
		WebSocketImpl.DEBUG = false;
		OnlineChatServer s;
		try {
			s = new OnlineChatServer(8091);
			s.start();
			//System.out.println( "websocket服务器启动,端口" + s.getPort() );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
}
