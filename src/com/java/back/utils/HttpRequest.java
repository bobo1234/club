package com.java.back.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class HttpRequest {
	/**
	 * 通过url请求获取网页数据
	 * 
	 * @param requestUrl
	 * @return
	 */
	public static String httpRequest(String requestUrl) {
		StringBuffer buffer = null;

		try {

			// String result=HttpRequest.sendGet(requestUrl, null);
			// System.out.println(result);
			// 建立连接
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");

			// 获取输入流
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			// 读取返回结果
			buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject HttpRequest(String request, String RequestMethod,
			String output) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 建立连接
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod(RequestMethod);
			if (output != null) {
				OutputStream out = connection.getOutputStream();
				out.write(output.getBytes("UTF-8"));
				out.close();
			}
			// 流处理
			InputStream input = connection.getInputStream();
			InputStreamReader inputReader = new InputStreamReader(input,
					"UTF-8");
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			// 关闭连接、释放资源
			reader.close();
			inputReader.close();
			input.close();
			input = null;
			connection.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (Exception e) {
		}
		return jsonObject;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();

			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection
					.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 调用webservice接口
	 * 
	 * @param method
	 * @param params
	 * @return
	 */
	public static String callWebService(String method, String[] params) {
		try {
			String nameSpace = Config.getValue("nameSpace");
			String wsUrl = Config.getValue("wsUrl");
			URL url = new URL(wsUrl);
			URLConnection conn = url.openConnection();
			HttpURLConnection con = (HttpURLConnection) conn;
			con.setDoInput(true); // 是否有入参
			con.setDoOutput(true); // 是否有出参
			con.setRequestMethod("POST"); // 设置请求方式
			con.setRequestProperty("content-type", "text/xml;charset=UTF-8");
			String arg = "";
			for (int i = 0; i < params.length; i++) {
				arg += "<arg" + i + ">" + params[i] + "</arg" + i + "> ";
			}
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
					+ " xmlns:q0='"
					+ nameSpace
					+ "' xmlns:xsd=\"http://www.w3.org/2001/XMLSchema \" "
					+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
					+ "<soapenv:Body><q0:"
					+ method
					+ ">"
					// + "<arg0>lisi</arg0><arg1>7892</arg1>"
					+ arg
					+ "</q0:"
					+ method
					+ "></soapenv:Body></soapenv:Envelope>";
			// 获得输出流
			OutputStream out = con.getOutputStream();
			out.write(requestBody.getBytes());
			StringBuffer sb = new StringBuffer();
			out.close();
			String mess = "";
			int code = con.getResponseCode();
			if (code == 200) {// 服务端返回正常
				InputStream is = con.getInputStream();
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = is.read(b)) != -1) {
					String str = new String(b, 0, len, "UTF-8");
					sb.append(str);
				}
				mess = sb.toString().substring(
						sb.toString().indexOf("<return>") + 8,
						sb.toString().indexOf("</return>"));
				is.close();
			} else {
				mess = "调用失败，请检查网络连接";
			}
			con.disconnect();
			return mess;
		} catch (Exception e) {
			// TODO: handle exception
			return "调用失败";
		}
	}

	/**
	 * 
	 * @param 方法
	 * @param params
	 * @param wsUrl
	 * @param nameSpace
	 * @return
	 */
	public static String callWebService(String method, Object[] params,
			String wsUrl, String nameSpace) {
		try {
			URL url = new URL(wsUrl);
			URLConnection conn = url.openConnection();
			HttpURLConnection con = (HttpURLConnection) conn;
			con.setDoInput(true); // 是否有入参
			con.setDoOutput(true); // 是否有出参
			con.setRequestMethod("POST"); // 设置请求方式
			con.setRequestProperty("content-type", "text/xml;charset=UTF-8");
			String arg = "";
			for (int i = 0; i < params.length; i++) {
				arg += "<arg" + i + ">" + params[i] + "</arg" + i + "> ";
			}
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
					+ " xmlns:q0='"
					+ nameSpace
					+ "' xmlns:xsd=\"http://www.w3.org/2001/XMLSchema \" "
					+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
					+ "<soapenv:Body><q0:"
					+ method
					+ ">"
					// + "<arg0>lisi</arg0><arg1>7892</arg1>"
					+ arg
					+ "</q0:"
					+ method
					+ "></soapenv:Body></soapenv:Envelope>";
			// 获得输出流
			OutputStream out = con.getOutputStream();
			out.write(requestBody.getBytes());
			StringBuffer sb = new StringBuffer();
			out.close();
			String mess = "";
			int code = con.getResponseCode();
			if (code == 200) {// 服务端返回正常
				InputStream is = con.getInputStream();
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = is.read(b)) != -1) {
					String str = new String(b, 0, len, "UTF-8");
					sb.append(str);
				}
				mess = sb.toString().substring(
						sb.toString().indexOf("<return>") + 8,
						sb.toString().indexOf("</return>"));
				is.close();
			} else {
				mess = "调用失败，请检查网络连接";
			}
			con.disconnect();
			return mess;
		} catch (Exception e) {
			return "调用失败";
		}
	}

	public static void main(String[] args) throws Exception {
//		String[] a = { "admin", "123456" };
//		String mess = callWebService("userLogin", a);
//		System.out.println(mess);
		long startTime = System.currentTimeMillis();//获取当前时间
		String[] params = { "121212" };
		String Namespace="AlarmInformationService";
		String method="getAlarm";
//		String wsdlurl="http://127.0.0.1:8080/WService/AlarmInformationService?wsdl";
		String wsdlurl="http://192.168.1.122:8080/WService/AlarmInformationService?wsdl";
		String mess = callWebService(method, params, wsdlurl, Namespace);
		System.out.println(mess);
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间："+(endTime-startTime)+"ms");
	}
}