package com.java.back.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtil {
	private static Logger log = Logger.getLogger(JsonUtil.class);

	public static String objectTojson(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (((obj instanceof String)) || ((obj instanceof Integer))
				|| ((obj instanceof Float)) || ((obj instanceof Boolean))
				|| ((obj instanceof Short)) || ((obj instanceof Double))
				|| ((obj instanceof Long)) || ((obj instanceof BigDecimal))
				|| ((obj instanceof BigInteger)) || ((obj instanceof Byte))) {
			json.append("\"").append(stringTojson(obj.toString())).append("\"");
		} else if ((obj instanceof Object[])) {
			json.append(arrayTojson((Object[]) obj));
		} else if ((obj instanceof List)) {
			json.append(listTojson((List) obj));
		} else if ((obj instanceof Map)) {
			json.append(mapTojson((Map) obj));
		} else if ((obj instanceof Set)) {
			json.append(setTojson((Set) obj));
		} else {
			json.append(beanTojson(obj));
		}
		return json.toString();
	}

	public static String beanTojson(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = (PropertyDescriptor[]) null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException localIntrospectionException) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = objectTojson(props[i].getName());
					String value = objectTojson(props[i].getReadMethod()
							.invoke(bean, new Object[0]));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception localException) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}
	public static <T> List<T> getObjectList(String jsonString,Class<T> cls){  
        List<T> list = new ArrayList<T>();  
        try {  
            Gson gson = new Gson();  
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();  
            for (JsonElement jsonElement : arry) {  
                list.add(gson.fromJson(jsonElement, cls));  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return list;  
    } 
	public static String listTojson(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((list != null) && (list.size() > 0)) {
			for (Object obj : list) {
				json.append(objectTojson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String arrayTojson(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((array != null) && (array.length > 0)) {
			Object[] arrayOfObject = array;
			int j = array.length;
			for (int i = 0; i < j; i++) {
				Object obj = arrayOfObject[i];
				json.append(objectTojson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String mapTojson(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if ((map != null) && (map.size() > 0)) {
			for (Object key : map.keySet()) {
				json.append(objectTojson(key));
				json.append(":");
				json.append(objectTojson(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String setTojson(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((set != null) && (set.size() > 0)) {
			for (Object obj : set) {
				json.append(objectTojson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String stringTojson(String s) {
		if (s == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if ((ch >= 0) && (ch <= '\037')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * json 2 bean
	 * 
	 * @param json
	 * @param c
	 * @return
	 */
	public static Object jsonToBean(String json, Class c) {
		Object obj = null;
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, c);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return obj;
	}

	/**
	 * json To bean
	 * 
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> T json2Bean(String json, Class<T> beanCalss) {
		try {
			Gson gson = new Gson();
			T bean = (T) gson.fromJson(json, beanCalss);
			return bean;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static Object jsonToList(String json, Class c) {
		JSONArray jsonarray = JSONArray.fromObject(json);
		List list = (List) JSONArray.toList(jsonarray, c);
		return list;
	}
}
