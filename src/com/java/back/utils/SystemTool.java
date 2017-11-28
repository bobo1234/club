package com.java.back.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Describtion Some tool function in common use of related to the system
 * @author Carol
 * @date 2016/08/18
 * @version 1.0
 */
public class SystemTool {
	private static Logger logger = LoggerFactory.getLogger(SystemTool.class);

	/**
	 * 知识的补充
	 * 
	 * InetAddress 继承自 java.lang.Object类 它有两个子类：Inet4Address 和 Inet6Address
	 * 此类表示互联网协议 (IP) 地址。
	 * 
	 * IP 地址是 IP 使用的 32 位或 128 位无符号数字， 它是一种低级协议，UDP 和 TCP 协议都是在它的基础上构建的。
	 * 
	 * ************************************************
	 * 主机名就是计算机的名字（计算机名），网上邻居就是根据主机名来识别的。 这个名字可以随时更改，从我的电脑属性的计算机名就可更改。
	 * 用户登陆时候用的是操作系统的个人用户帐号，这个也可以更改， 从控制面板的用户界面里改就可以了。这个用户名和计算机名无关。
	 */

	/**
	 * @Describtion Get local host ip
	 * 
	 * @return String
	 */
	public static String getLocalHostIP() {
		String ip;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} catch (Exception e) {
			logger.error("Get local host ip fail." + e.getMessage());
			ip = "";
		}

		return ip;
	}

	/**
	 * @Describtion Get local host name
	 * 
	 * @return host name
	 */
	public static String getLocalHostName() {
		String hostName;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception e) {
			logger.error("Get local host name fail." + e.getMessage());
			hostName = "";
		}

		return hostName;
	}

	/**
	 * @Describtion Get all local host ip
	 * 
	 * @return host ips
	 */
	public static String[] getAllLocalHostIP() {
		String[] ret = null;
		try {
			String hostName = getLocalHostName();
			if (hostName.length() > 0) {
				// Get all host ip by host name.
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if (addrs.length > 0) {
					ret = new String[addrs.length];
					for (int i = 0; i < addrs.length; i++) {
						ret[i] = addrs[i].getHostAddress();
					}
				}
			}

		} catch (Exception e) {
			logger.error("Get all local host ip fail." + e.getMessage());
			ret = null;
		}

		return ret;
	}

	/**
	 * @Describtion Get network card of windows mac address
	 * 
	 * @return mac address
	 */
	public static String getWindowsMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			// execute windows command for showing mac
			process = Runtime.getRuntime().exec("ipconfig /all");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				// search physical address
				index = line.toLowerCase().indexOf("physical address");

				if (index >= 0) {
					index = line.indexOf(":");
					if (index >= 0) {
						// get mac address
						mac = line.substring(index + 1).trim();
					}
					break;
				}
			}
		} catch (IOException e) {
			logger.error("Get mac address fail." + e.getMessage());
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				logger.error("close BufferedReader fail." + e1.getMessage());
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	/**
	 * @Describtion Get mac address of windows7
	 * 
	 * @return win7 mac address
	 */
	public static String getMACAddress() {
		byte[] mac = null;
		try {
			InetAddress ia = InetAddress.getLocalHost();
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (UnknownHostException e) {
			logger.error("Get win7 mac address unknown host." + e.getMessage());
		} catch (SocketException e) {
			logger.error("Get win7 mac address socket exception." + e.getMessage());
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// change mac address to hex
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}

		// change string of mac address lower case to upper case
		return sb.toString().toUpperCase();
	}

	public static void main(String[] args) throws UnknownHostException {
		// System.out.println("Local IP:" + getLocalHostIP());
		// System.out.println("Local host name:" + getLocalHostName());
		//
		// String[] localIP = getAllLocalHostIP();
		// for (int i = 0; i < localIP.length; i++) {
		// System.out.println(localIP[i]);
		// }
		//
		// InetAddress baidu = InetAddress.getByName("www.baidu.com");
		// System.out.println("baidu : " + baidu);
		// System.out.println("baidu IP: " + baidu.getHostAddress());
		// System.out.println("baidu HostName: " + baidu.getHostName());
	}

}