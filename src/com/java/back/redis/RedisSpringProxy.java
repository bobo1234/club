package com.java.back.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * spring 代理jedis客户端功能与redis server通信, 依赖spring-data-redis-1.0.2.jar包.
 * 
 */
@Component("redisSpringProxy")
public class RedisSpringProxy {

	@Resource
	public RedisTemplate<Serializable, Serializable> redisTemplate;

	/**
	 * 将指定的value 与此映射中的指定key 关联, 保存到redis 中
	 */
	public void save(final String key, final Object value) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(serializeKey(key), serializeValue(value));
				return null;
			}
		});
	}
	
	/**
	 * 将对象放进redis
	 * @param key
	 * @param value
	 */
	public void saveObject(final String key, final Object value) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(serializeKey(key), SerializeUtil.serialize(value));
				return null;
			}
		});
	}
	
	/**
	 * 通过key获取对象
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(final String key,Class<T> beanCalss) {
		return redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] keyBytes = serializeKey(key);
				if (connection.exists(keyBytes)) {
					byte[] valueBytes = connection.get(keyBytes);
					return (T) SerializeUtil.unserialize(valueBytes);
				}
				return null;
			}
		});
	}

	/**
	 * 将redis里指定的key的value加一
	 * 
	 * @param key
	 * @param value
	 */
	public Long incr(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.incr(serializeKey(key));
			}
		});
	}

	/**
	 * 写入缓存，设置失效时间
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 *            单位:秒
	 */
	public void saveEx(final String key, final Object value,
			final Long expireTime) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.setEx(serializeKey(key), expireTime,
						serializeValue(value));
				return null;
			}
		});
		// try {
		// ValueOperations<Serializable, Serializable> operations =
		// redisTemplate
		// .opsForValue();
		// operations.set(key, value);
		// redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	// 删除当前数据库中的所有Key
	// flushdb
	// 删除所有数据库中的key
	// flushall
	/**
	 * 清空redis 中的所有数据
	 */
	public void flushAll() {
		redisTemplate.getConnectionFactory().getConnection().flushAll();
	}

	/**
	 * 清空redis 中的所有数据
	 */
	public void flushDB() {
		redisTemplate.getConnectionFactory().getConnection().flushDb();
	}

	/**
	 * 根据 pattern 获取 redis 中的键
	 * */
	public Set<Serializable> getKeysByPattern(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 获取 redis 中所有的键
	 * */
	public Set<Serializable> getAllKeys() {
		return getKeysByPattern("*");
	}

	/**
	 * 返回指定key 所映射的值. 如果redis 中不包含该key 的映射关系，则返回 null
	 */
	public Object read(final String key) {
		return redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] keyBytes = serializeKey(key);
				if (connection.exists(keyBytes)) {
					byte[] valueBytes = connection.get(keyBytes);
					return deserializeValue(valueBytes);
				}
				return null;
			}
		});
	}

	/**
	 * 如果存在一个key 的映射关系，则将其从redis 中移除
	 * 
	 * @return 被删除 key 的数量
	 */
	public Long delete(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.del(serializeKey(key));
			}
		});
	}

	/**
	 * <p>
	 * 将哈希表 key 中的域 field(map键) 的值设为 value
	 * </p>
	 * <p>
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作
	 * </p>
	 * <p>
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖
	 * </p>
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @param field
	 *            与指定值关联的field(map键)
	 * @param value
	 *            与指定field(map键)关联的值
	 * @return 如果 field 是哈希表中的一个新建域，并且值设置成功, 返回 true, <br>
	 *         如果哈希表中域 field(map键) 已经存在且旧值已被新值覆盖，返回false , 操作失败返回null 。
	 */
	public Boolean hSet(final String key, final byte[] field, final byte[] value) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.hSet(serializeKey(key), field, value);
			}
		});
	}

	/**
	 * 返回redis哈希表 key 中给定域 field(map键) 的值
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @param field
	 *            与指定值关联的field(map键)
	 * @return 返回指定 key 和指定 field(map键) 所映射的值,当给定 field(map键) 不存在或是给定 key
	 *         不存在时，返回 nil 。
	 */
	public Object hGet(final String key, final byte[] field) {
		return redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] value = connection.hGet(serializeKey(key), field);
				return deserializeValue(value);
			}

		});
	}

	/**
	 * redis 存储 map
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @param mapObject
	 *            map对象
	 */
	public void hmSet(final String key, final Map<Object, Object> mapObject) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {

				Map<byte[], byte[]> mapByte = new HashMap<byte[], byte[]>(
						mapObject.size());

				for (final Entry<Object, Object> entry : mapObject.entrySet()) {
					byte[] mapKey = serializeValue(entry.getKey());
					byte[] mapValue = serializeValue(entry.getValue());
					mapByte.put(mapKey, mapValue);
				}

				connection.hMSet(serializeKey(key), mapByte);

				mapByte = null;// release

				return null;
			}

		});
	}

	/**
	 * 返回reids 哈希表 key 中, 一个或多个给定域的值
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @param field
	 *            与指定值关联的field(map键)
	 * @return 返回指定 key 和指定 field(map键) 所映射的值,当给定 field(map键) 不存在或是给定 key
	 *         不存在时，返回 nil 。
	 */
	public List<Object> hmGet(final String key, final Object... field) {
		return redisTemplate.execute(new RedisCallback<List<Object>>() {
			@Override
			public List<Object> doInRedis(RedisConnection connection)
					throws DataAccessException {

				List<byte[]> redisRet = null;
				List<Object> ret = new ArrayList<Object>();
				if (field.length > 1) {
					byte[][] fields = new byte[field.length][];
					int i = 0;
					for (Object object : field) {
						fields[i] = serializeValue(object);
						i++;
					}
					redisRet = connection.hMGet(serializeKey(key), fields);
				}

				else {
					redisRet = connection.hMGet(serializeKey(key),
							serializeValue(field[0]));
				}

				if (null != redisRet) {
					for (byte[] value : redisRet) {
						if (value != null)
							ret.add(deserializeValue(value));
					}
				}

				return ret.size() > 0 ? ret : null;
			}
		});
	}

	/**
	 * 返回redis 哈希表 key 中,所有的域和值
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<Object, Object> hGetAll(final String key) {
		return (Map<Object, Object>) redisTemplate
				.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						Map<byte[], byte[]> mapByte = connection
								.hGetAll(serializeKey(key));
						Map<Object, Object> mapObject = new HashMap<Object, Object>(
								mapByte.size());

						for (final Entry<byte[], byte[]> entry : mapByte
								.entrySet()) {
							Object mapKey = deserializeValue(entry.getKey());
							Object mapValue = deserializeValue(entry.getValue());
							mapObject.put(mapKey, mapValue);
						}

						mapByte = null;// release

						return mapObject;
					}

				});
	}

	/**
	 * 以<code>List</code> 数据结构存储到redis 中（从头添加）, 允许添加重复值
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @param values
	 *            存储一个值或多个值
	 * @return 返回列表长度
	 */
	public Long lPush(final String key, final Object... values) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long count = new Long(0);
				for (Object value : values) {
					byte[] keyByte = serializeKey(key);
					byte[] val = serializeValue(value);
					count = connection.lPush(keyByte, val);
				}
				return count;
			}

		});
	}
	
	/**
	 * 获取List长度
	 * @param key
	 * @return
	 */
	public Long LLen(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.lLen(serializeKey(key));
			}
		});
	}

	/**
	 * 根据索引取出数据
	 * @param key
	 * @param index
	 * @return
	 */
	public Object lIndex(final String key, final long index) {
		return redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] keyBytes = serializeKey(key);
				if (connection.exists(keyBytes)) {
					byte[] valueBytes = connection.lIndex(keyBytes, index);
					return deserializeValue(valueBytes);
				}
				return null;
			}
		});
	}

	/**
	 * 数据结构存储到redis 中(最后添加), 允许添加重复值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long rPush(final String key, final String value) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long count = new Long(0);
				byte[] keyByte = serializeKey(key);
				byte[] val = serializeValue(value);
				count = connection.rPush(keyByte, val);
				return count;
			}

		});
	}

	/**
	 * 从redis 中取出 <code>List</code>结构数据, 参数中指定取值范围, 假如你有一个包含一百个元素的列表, 对该列表执行
	 * LRANGE list 0 10, 结果是一个包含11个元素的列表.如果指定0 -1那么返回全部元素.
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @param begin
	 *            起始位置
	 * @param end
	 *            结束位置
	 * @return List结果集或null
	 */
	public List<Object> lRange(final String key, final long begin,
			final long end) {
		return (List<Object>) redisTemplate
				.execute(new RedisCallback<List<Object>>() {

					@Override
					public List<Object> doInRedis(RedisConnection connection)
							throws DataAccessException {
						List<byte[]> retByteLst = connection.lRange(
								serializeKey(key), begin, end);
						if (null != retByteLst) {
							List<Object> retValLst = new ArrayList<Object>(
									retByteLst.size());
							for (byte[] retValByte : retByteLst) {
								Object val = deserializeValue(retValByte);
								retValLst.add(val);
							}
							return retValLst;
						}
						return null;
					}

				});
	}

	/**
	 * 移除并返回列表 key 的头元素
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @return value
	 */
	public Object lPop(final String key) {
		return redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] value = connection.lPop(serializeKey(key));
				return deserializeValue(value);
			}

		});
	}

	/**
	 * 命令用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
	 * 
	 * @param key
	 * @param key2
	 * @return
	 */
	public Object rpoplpush(final String key, final String key2) {
		return redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] value = connection.rPopLPush(serializeKey(key),
						serializeKey(key2));
				return deserializeValue(value);
			}

		});
	}

	/**
	 * 移除并返回列表 key 的尾元素
	 * 
	 * @param key
	 *            redis哈希表 key
	 * @return value
	 */
	public Object rPop(final String key) {
		return redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] value = connection.rPop(serializeKey(key));
				return deserializeValue(value);
			}

		});
	}

	protected byte[] serializeKey(final String key) {
		return redisTemplate.getStringSerializer().serialize(key);
	}

	protected byte[] serializeValue(final Object value) {
		RedisSerializer<String> serializer = getRedisSerializer();
		return serializer.serialize(value.toString());
	}

	protected Object deserializeValue(final byte[] value) {
		return getRedisSerializer().deserialize(value);
	}

	/**
	 * 获取 RedisSerializer <br>
	 * ------------------------------<br>
	 */
	protected RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}
}