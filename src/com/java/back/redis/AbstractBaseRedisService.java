package com.java.back.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class AbstractBaseRedisService<K, V> implements BaseRedisService {

	@Autowired
	protected RedisTemplate<K, V> redisTemplate;

	/**
	 * 设置redisTemplate
	 * 
	 * @param redisTemplate
	 *            the redisTemplate to set
	 */
	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 获取 RedisSerializer <br>
	 * ------------------------------<br>
	 */
	protected RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}
	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final K key) {
		return redisTemplate.hasKey(key);
	}
	
	/**
	 * 新增 <br>
	 * ------------------------------<br>
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public boolean add(final String Key, final String value) {
		boolean result = false;
		if (Key != null && !"".equals(Key)) {
			result = redisTemplate.execute(new RedisCallback<Boolean>() {
				public Boolean doInRedis(RedisConnection connection)
						throws DataAccessException {
					RedisSerializer<String> serializer = getRedisSerializer();
					byte[] key = serializer.serialize(Key);
					byte[] name = serializer.serialize(value);
					return connection.setNX(key, name);
				}
			});
		}
		return result;
	}

	/**
	 * 批量新增
	 * ------------------------------<br>
	 *
	 * @param list
	 * @return
	 */
	@Override
	public boolean add(final List<Map<String, String>> list) {
		boolean result = false;
		if (list != null && list.size() > 0) {
			result = redisTemplate.execute(new RedisCallback<Boolean>() {
				public Boolean doInRedis(RedisConnection connection)
						throws DataAccessException {
					RedisSerializer<String> serializer = getRedisSerializer();
					for (Map<String, String> map : list) {
						for (Entry<String, String> m : map.entrySet()) {
							byte[] key = serializer.serialize(m.getKey());
							byte[] name = serializer.serialize(m.getValue());
							connection.setNX(key, name);
						}
					}
					return true;
				}
			}, false, true);
		}
		return result;
	}

	/**
	 * 删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 */
	@Override
	public void delete(final String key) {
		if (key != null && !"".equals(key)) {
			List<String> list = new ArrayList<String>();
			list.add(key);
			delete(list);
		}
	}

	/**
	 * 删除多个 <br>
	 * ------------------------------<br>
	 * 
	 * @param keys
	 */
	@Override
	public void delete(final List<String> keys) {
		if (keys != null && keys.size() > 0) {
			redisTemplate.execute(new RedisCallback<Boolean>() {
				public Boolean doInRedis(RedisConnection connection)
						throws DataAccessException {
					RedisSerializer<String> serializer = getRedisSerializer();
					for (String key : keys) {
						byte[] keyByte = serializer.serialize(key);
						connection.del(keyByte);
					}
					return true;
				}
			}, false, true);
		}
	}

	/**
	 * 修改 <br>
	 * ------------------------------<br>
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public boolean update(final String Key, final String value) {
		if (get(Key) == null) {
			throw new NullPointerException("数据行不存在, key = " + Key);
		}
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(Key);
				byte[] name = serializer.serialize(value);
				connection.set(key, name);
				return true;
			}
		});
		return result;
	}

	/**
	 * 通过key获取 <br>
	 * ------------------------------<br>
	 * 
	 * @param keyId
	 * @return
	 */
	@Override
	public Map<String, String> get(final String keyId) {
		Map<String, String> result = new HashMap<String, String>();
		if(keyId != null && !"".equals(keyId)) {
			result = redisTemplate.execute(new RedisCallback<Map<String, String>>() {
				public Map<String, String> doInRedis(RedisConnection connection)
						throws DataAccessException {
					RedisSerializer<String> serializer = getRedisSerializer();
					byte[] key = serializer.serialize(keyId);
					byte[] value = connection.get(key);
					if (value == null) {
						return null;
					}
					String valueS = serializer.deserialize(value);
					Map<String, String> map = new HashMap<String, String>();
					map.put(keyId, valueS);
					return map;
				}
			});
		}
		return result;
	}
}