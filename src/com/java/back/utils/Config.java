package com.java.back.utils;

import java.io.InputStream;
import java.util.Properties;
  
public class Config {  

	/**
	 * 默认配置文件(config.properties)
	 * @param key
	 * @return
	 */
    public static String getValue(String key){
    	String result = "";
    	try{
    		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    		InputStream is = classLoader.getResourceAsStream("config.properties");
    		Properties p = new Properties(); 
    		p.load(is);
    		result = p.getProperty(key);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * 
     * @param name 配置文件的名称
     * @param key
     * @return
     */
    public static String getValue(String name,String key){
    	String result = "";
    	try{
    		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    		InputStream is = classLoader.getResourceAsStream(name+".properties");
    		Properties p = new Properties(); 
    		p.load(is);
    		result = p.getProperty(key);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    public static void main(String args[]){
    	
    	System.out.println(getValue("redis", "redis.host"));
    }
} 