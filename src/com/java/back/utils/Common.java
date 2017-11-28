package com.java.back.utils;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Common {
	/**
	 * 新建map
	 * 
	 * @return
	 */
	public static Map<String, String> NewMap() {
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}

	public static List NewList() {
		List<String> L = new LinkedList();
		return L;
	}

	public static List AddList(List L, String str) {
		L.add(str);
		return L;
	}

	public String GetPropertiesValue(String Key, String P_Name) {
		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream(
					P_Name + ".properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String value = props.getProperty(Key);
		return value;
	}

	public static String[] ListToArray(List l) {
		String[] array = new String[l.size()];
		for (int i = 0; i < l.size(); i++) {
			array[i] = l.get(i).toString();
		}
		return array;
	}

	public static String SubStr(String str, int len) {
		if (str.equals("")) {
			return "";
		}
		if (str.length() > len) {
			return str.substring(0, len);
		}
		return str;
	}

	public static String EndStr(String str, int len) {
		String a = "";
		int b = 0;
		if (str.equals("")) {
			return "";
		}
		if (str.length() <= len) {
			return str;
		}
		b = str.length() / len;
		if (str.length() % len > 0) {
			b++;
		}
		for (int i = 0; i < b; i++) {
			if (str.length() >= len * (i + 1)) {
				a = a + str.substring(i * len, len * (i + 1)) + "#13#10";
			} else {
				a = a + str.substring(i * len, str.length()) + "#13#10";
			}
		}
		if (!a.equals("")) {
			a = a.substring(0, a.length() - 3);
		}
		return a;
	}

	public static int StrInArrExist(String str, String[] arr) {
		int flag = 0;
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				if (str.equals(arr[i])) {
					flag = 1;
					break;
				}
			}
		}
		return flag;
	}

	public static String ArrToStr(String[] arr, String symbol) {
		String str = "";
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				if (!symbol.equals("")) {
					if (!arr[i].trim().equals("")) {
						str = str + arr[i] + symbol;
					}
				} else if (!arr[i].trim().equals("")) {
					str = str + arr[i] + symbol;
				}
			}
		}
		if (!"".equals(str)) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	/**
	 * 获得全球唯一标识
	 * 
	 * @return
	 */
	public static String GetUUID() {
		String s = UUID.randomUUID().toString();
		s = s.replace("-", "");
		s = s.toUpperCase();
		return s;
	}

	/**
	 * 用户请求地址
	 * 
	 * @return
	 */
	public static String getReqUrl(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
		return uri;
	}

	/**
	 * 获取四舍五日的数据，默认保留小数点后两位
	 * 
	 * @param f
	 * @return
	 */
	public static double getFamatByNumber(double f) {
		try {
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}


	private static Logger logger = Logger.getLogger(Common.class);

	/**
	 * 获取本地服务器的ip
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getLinuxLocalIp() throws SocketException{
		String ip = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				String name = intf.getName();
				if (!name.contains("docker") && !name.contains("lo")) {
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							String ipaddress = inetAddress.getHostAddress()
									.toString();
							if (!ipaddress.contains("::")
									&& !ipaddress.contains("0:0:")
									&& !ipaddress.contains("fe80")) {
								if (!ipaddress.contains(":")) {
									ip = ipaddress;
								}
							}
						}
					}
				}
			}
		} catch (SocketException ex) {
			logger.info("获取ip地址异常");
			ex.printStackTrace();
		}
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if (os.contains("Windows")) {
			InetAddress inet;
			try {
				inet = InetAddress.getLocalHost();
				ip = inet.getHostAddress();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("本机的ip:" + ip);
		return ip;
	}
	/**
	 * 获取请求地址
	 * @param request
	 * @return
	 */
	public String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	/** 
	   * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
	   * 
	   * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
	   * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
	   * 
	   * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
	   * 192.168.1.100 
	   * 
	   * 用户真实IP为： 192.168.1.110 
	   * 
	   * @param request 
	   * @return 
	   */
	  public static String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	  } 
	
	
	public static void main(String[] args) throws SocketException {
		/**
		 * 参数1：服务的发布地址 参数2：服务的实现者 Endpoint 会重新启动一个线程
		 */
		// Endpoint.publish("http://test.cm/", new PoliceOuterServiceImpl());
		// System.out.println("Server ready...");
		 getLinuxLocalIp();

	}
	/**
	 * 获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) ra)
				.getRequest();
		return request;
	}
}
