package com.java.back.redis;

import java.util.List;
import java.util.Map;

public interface BaseRedisService {

	/**
	 * 新增 <br>
	 * ------------------------------<br>
	 * 
	 * @param user
	 * @return
	 */
	public boolean add(final String Key, final String value);

	/**
	 * 批量新增 ------------------------------<br>
	 *
	 * @param list
	 * @return
	 */
	public boolean add(final List<Map<String, String>> list);

	/**
	 * 删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 */
	public void delete(final String key);

	/**
	 * 删除多个 <br>
	 * ------------------------------<br>
	 * 
	 * @param keys
	 */
	public void delete(final List<String> keys);

	/**
	 * 修改 <br>
	 * ------------------------------<br>
	 * 
	 * @param user
	 * @return
	 */
	public boolean update(final String Key, final String value);

	/**
	 * 通过key获取 <br>
	 * ------------------------------<br>
	 * 
	 * @param keyId
	 * @return
	 */
	public Map<String, String> get(final String keyId);
}