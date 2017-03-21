package com.nuctech.ls.model.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

/**
 * Jedis操作类，对redis的命令进行简单的封装
 *
 */
@Repository("redisClientTemplate")
public class RedisClientTemplate {
	private static final Logger log = LoggerFactory.getLogger(RedisClientTemplate.class);

	@Autowired
	private JedisDataSource redisDataSource;

	/**
	 * 断开连接
	 */
	public void disconnect() {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		shardedJedis.disconnect();
	}

	/**
	 * 设置单个值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {
		String result = null;

		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.set(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取单个值
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}

		boolean broken = false;
		try {
			result = shardedJedis.get(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public Boolean exists(String key) {
		Boolean result = false;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.exists(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回指定key的值的存储格式
	 * 
	 * @param key
	 * @return none：当key不存在时。<br>
	 *         string：当value为字符串时。<br>
	 *         list：当value为list集合时。<br>
	 *         set：当value为set集合时。<br>
	 *         zset：有序set集合时。<br>
	 *         hash：当value为hash时
	 */
	public String type(String key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.type(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 在某段时间后失效
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(String key, int seconds) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.expire(key, seconds);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 在某个时间点失效
	 * 
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public Long expireAt(String key, long unixTime) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.expireAt(key, unixTime);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 以秒为单位返回 key 的剩余过期时间。Redis2.8+版本，当 key 不存在时，返回 -2 。 当 key
	 * 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
	 * 
	 * @param key
	 * @return
	 */
	public Long ttl(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.ttl(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 设置或者清空key的value(字符串)在offset处的bit值。
	 * 
	 * @param key
	 * @param offset
	 * @param value
	 * @return
	 */
	public boolean setbit(String key, long offset, boolean value) {

		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		boolean result = false;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.setbit(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回字符串值指定偏移量上的位(bit)。 当偏移量 offset 比字符串值的长度大，或者 key 不存在时，返回 0 。
	 * 
	 * @param key
	 * @param offset
	 * @return
	 */
	public boolean getbit(String key, long offset) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		boolean result = false;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;

		try {
			result = shardedJedis.getbit(key, offset);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始。
	 * 
	 * @param key
	 * @param offset
	 * @param value
	 * @return
	 */
	public long setrange(String key, long offset, String value) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		long result = 0;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.setrange(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 startOffset 和 endOffset 两个偏移量决定(包括
	 * startOffset 和 endOffset 在内)。
	 * 
	 * @param key
	 * @param startOffset
	 * @param endOffset
	 * @return
	 */
	public String getrange(String key, long startOffset, long endOffset) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getrange(key, startOffset, endOffset);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public String getSet(String key, String value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getSet(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在。 若给定的 key 已经存在，则 SETNX 不做任何动作。
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long setnx(String key, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.setnx(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return
	 */
	public String setex(String key, int seconds, String value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.setex(key, seconds, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 将 key 所储存的值减去指定的减量值。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 
	 * @param key
	 * @param integer
	 * @return
	 */
	public Long decrBy(String key, long integer) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.decrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 将 key 中储存的数字值递减。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 
	 * @param key
	 * @return
	 */
	public Long decr(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.decr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 将 key 中储存的数字加上指定的增量值。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 
	 * @param key
	 * @param integer
	 * @return
	 */
	public Long incrBy(String key, long integer) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.incrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 将 key 中储存的数字递增。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 命令。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 
	 * @param key
	 * @return
	 */
	public Long incr(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.incr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 为指定的 key 追加值。
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long append(String key, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.append(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取存储在指定 key 中字符串的子字符串。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public String substr(String key, int start, int end) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.substr(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 为哈希表（Map）中的字段赋值 。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hset(String key, String field, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hset(key, field, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回哈希表中指定字段的值。
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hget(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 为哈希表中不存在的的字段赋值 。 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。 如果字段已经存在于哈希表中，操作无效。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hsetnx(String key, String field, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hsetnx(key, field, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 同时将多个 field-value (字段-值)对设置到哈希表中。 此命令会覆盖哈希表中已存在的字段。
	 * 
	 * @param key
	 * @param hash
	 * @return
	 */
	public String hmset(String key, Map<String, String> hash) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hmset(key, hash);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回哈希表中，一个或多个给定字段的值。
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<String> hmget(String key, String... fields) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hmget(key, fields);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 为哈希表中的字段值加上指定增量值。 增量也可以为负数，相当于对指定字段进行减法操作。 如果哈希表的 key 不存在，一个新的哈希表被创建并执行
	 * HINCRBY 命令。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hincrBy(String key, String field, long value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hincrBy(key, field, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 查看哈希表的指定字段是否存在。
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public Boolean hexists(String key, String field) {
		Boolean result = false;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hexists(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 删除已存在的键。不存在的 key 会被忽略
	 * 
	 * @param key
	 * @return
	 */
	public Long del(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.del(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 删除哈希表 key 中的指定字段，不存在的字段将被忽略。
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public Long hdel(String key, String... fields) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hdel(key, fields);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取哈希表(Map)中字段的数量。
	 * 
	 * @param key
	 * @return
	 */
	public Long hlen(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hlen(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取所有哈希表中的字段
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hkeys(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取哈希表中所有值
	 * 
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hvals(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取在哈希表中指定 key 的所有字段和值
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key) {
		Map<String, String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.hgetAll(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 在列表中添加一个或多个值
	 * 
	 * @param key
	 * @param strings
	 * @return
	 */
	public Long rpush(String key, String... strings) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.rpush(key, strings);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 将一个或多个值插入到列表头部。如果参数顺序为key1,key2,key3，则列表将为：key3,key2,key1...
	 * 
	 * @param key
	 * @param strings
	 * @return
	 */
	public Long lpush(String key, String... strings) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.lpush(key, strings);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取列表长度
	 * 
	 * @param key
	 * @return
	 */
	public Long llen(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.llen(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取列表指定范围内的元素
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, long start, long end) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.lrange(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public String ltrim(String key, long start, long end) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.ltrim(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 通过索引获取列表中的元素
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String lindex(String key, long index) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.lindex(key, index);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 通过索引设置列表元素的值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public String lset(String key, long index, String value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.lset(key, index, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素<br>
	 * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。<br>
	 * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。<br>
	 * count = 0 : 移除表中所有与 VALUE 相等的值。<br>
	 * 
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	public Long lrem(String key, long count, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.lrem(key, count, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 移出并获取列表的第一个元素
	 * 
	 * @param key
	 * @return
	 */
	public String lpop(String key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.lpop(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 移除并获取列表最后一个元素
	 * 
	 * @param key
	 * @return
	 */
	public String rpop(String key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.rpop(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * return 1 add a not exist value , return 0 add a exist value
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public Long sadd(String key, String... members) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.sadd(key, members);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回集合中的所有成员
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.smembers(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 移除集合中一个成员
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public Long srem(String key, String... members) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();

		Long result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.srem(key, members);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 * 
	 * @param key
	 * @return
	 */
	public String spop(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.spop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回集合中元素的数量
	 * 
	 * @param key
	 * @return
	 */
	public Long scard(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Long result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.scard(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 判断 member 元素是否是集合 key 的成员
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public Boolean sismember(String key, String member) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Boolean result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.sismember(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回集合中的一个随机元素。
	 * 
	 * @param key
	 * @return
	 */
	public String srandmember(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.srandmember(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
	 * 
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public Long zadd(String key, double score, String member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.zadd(key, score, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 通过索引区间返回有序集合成指定区间内的成员
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrange(String key, int start, int end) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.zrange(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 移除有序集合中的一个或多个成员
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public Long zrem(String key, String... members) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.zrem(key, members);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 有序集合中对指定成员的分数加上增量 increment
	 * 
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public Double zincrby(String key, double score, String member) {
		Double result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zincrby(key, score, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回有序集合中指定成员的索引
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zrank(String key, String member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrank(key, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zrevrank(String key, String member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrank(key, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回有序集中指定区间内的成员，通过索引，分数从高到底
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrevrange(String key, int start, int end) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrange(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 通过索引区间返回有序集合成指定区间内的成员
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeWithScores(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回有序集中指定分数区间内的成员，分数从高到低排序
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeWithScores(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 获取有序集合的成员数
	 * 
	 * @param key
	 * @return
	 */
	public Long zcard(String key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zcard(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回有序集中，成员的分数值
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public Double zscore(String key, String member) {
		Double result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zscore(key, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 对输入的列表、集合或者有序集合进行排序，默认按数值进行排序，然后返回排序的结果
	 * 
	 * @param key
	 * @return
	 */
	public List<String> sort(String key) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.sort(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 按指定的排序参数，对输入的列表、集合或者有序集合进行排序，然后返回排序的结果
	 * 
	 * @param key
	 * @param sortingParameters
	 * @return
	 */
	public List<String> sort(String key, SortingParams sortingParameters) {
		List<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.sort(key, sortingParameters);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 计算有序集合中指定分数区间的成员数量。
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long zcount(String key, double min, double max) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zcount(key, min, max);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回的有序集合在最小值和最大值(包括得分等于最小或最大元素)之间的分数键中的所有元素。该元素被认为是从低到高分。具有相同分数的元素字典顺序被返回(
	 * 这是从在Redis有序集合执行的属性，不涉及进一步的计算)。
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<String> zrangeByScore(String key, double min, double max) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeByScore(key, min, max);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。 具有相同分数值的成员按字典序的逆序(reverse
	 * lexicographical order )排列。
	 * 
	 * @param key
	 * @param max
	 * @param min
	 * @return
	 */
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeByScore(key, max, min);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeByScore(key, min, max, offset, count);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Set<String> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeByScoreWithScores(key, min, max);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zremrangeByRank(String key, int start, int end) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zremrangeByRank(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zremrangeByScore(String key, double start, double end) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zremrangeByScore(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	/**
	 * 用于在列表的元素前或者后插入元素。 当指定元素不存在于列表中时，不执行任何操作。 当列表不存在时，被视为空列表，不执行任何操作。
	 * 
	 * @param key
	 * @param where
	 * @param pivot
	 * @param value
	 * @return
	 */
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.linsert(key, where, pivot, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public String set(byte[] key, byte[] value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.set(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] get(byte[] key) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.get(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Boolean exists(byte[] key) {
		Boolean result = false;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.exists(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public String type(byte[] key) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.type(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long expire(byte[] key, int seconds) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.expire(key, seconds);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long expireAt(byte[] key, long unixTime) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.expireAt(key, unixTime);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long ttl(byte[] key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.ttl(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] getSet(byte[] key, byte[] value) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.getSet(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long setnx(byte[] key, byte[] value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.setnx(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public String setex(byte[] key, int seconds, byte[] value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.setex(key, seconds, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long decrBy(byte[] key, long integer) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.decrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long decr(byte[] key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.decr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long incrBy(byte[] key, long integer) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.incrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long incr(byte[] key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.incr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long append(byte[] key, byte[] value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.append(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] substr(byte[] key, int start, int end) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.substr(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long hset(byte[] key, byte[] field, byte[] value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hset(key, field, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] hget(byte[] key, byte[] field) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hget(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hsetnx(key, field, value);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hmset(key, hash);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		List<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hmget(key, fields);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long hincrBy(byte[] key, byte[] field, long value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hincrBy(key, field, value);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Boolean hexists(byte[] key, byte[] field) {
		Boolean result = false;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hexists(key, field);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long hdel(byte[] key, byte[] field) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hdel(key, field);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long hlen(byte[] key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hlen(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<byte[]> hkeys(byte[] key) {
		Set<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hkeys(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Collection<byte[]> hvals(byte[] key) {
		Collection<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hvals(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Map<byte[], byte[]> hgetAll(byte[] key) {
		Map<byte[], byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.hgetAll(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long rpush(byte[] key, byte[]... strings) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.rpush(key, strings);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long lpush(byte[] key, byte[]... strings) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.lpush(key, strings);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long llen(byte[] key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.llen(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public List<byte[]> lrange(byte[] key, int start, int end) {
		List<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.lrange(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public String ltrim(byte[] key, int start, int end) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.ltrim(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] lindex(byte[] key, int index) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.lindex(key, index);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public String lset(byte[] key, int index, byte[] value) {
		String result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.lset(key, index, value);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long lrem(byte[] key, int count, byte[] value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.lrem(key, count, value);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] lpop(byte[] key) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.lpop(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] rpop(byte[] key) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.rpop(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long sadd(byte[] key, byte[] member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.sadd(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<byte[]> smembers(byte[] key) {
		Set<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.smembers(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long srem(byte[] key, byte[] member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.srem(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] spop(byte[] key) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.spop(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long scard(byte[] key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.scard(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Boolean sismember(byte[] key, byte[] member) {
		Boolean result = false;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.sismember(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public byte[] srandmember(byte[] key) {
		byte[] result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.srandmember(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zadd(byte[] key, double score, byte[] member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zadd(key, score, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrange(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zrem(byte[] key, byte[] member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrem(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Double zincrby(byte[] key, double score, byte[] member) {
		Double result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zincrby(key, score, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zrank(byte[] key, byte[] member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrank(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zrevrank(byte[] key, byte[] member) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrank(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrange(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeWithScores(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeWithScores(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zcard(byte[] key) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zcard(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Double zscore(byte[] key, byte[] member) {
		Double result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zscore(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public List<byte[]> sort(byte[] key) {
		List<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.sort(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		List<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.sort(key, sortingParameters);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zcount(byte[] key, double min, double max) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zcount(key, min, max);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		Set<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeByScore(key, min, max);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		Set<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeByScore(key, min, max, offset, count);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeByScoreWithScores(key, min, max);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		Set<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeByScore(key, max, min);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		Set<byte[]> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zremrangeByRank(byte[] key, int start, int end) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zremrangeByRank(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long zremrangeByScore(byte[] key, double start, double end) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.zremrangeByScore(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
		Long result = null;
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = shardedJedis.linsert(key, where, pivot, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public List<Object> pipelined() {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		List<Object> result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			ShardedJedisPipeline shardedJedisPipeline = shardedJedis.pipelined();
			result = shardedJedisPipeline.getResults();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Jedis getShard(byte[] key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Jedis result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getShard(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Jedis getShard(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Jedis result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getShard(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public JedisShardInfo getShardInfo(byte[] key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		JedisShardInfo result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getShardInfo(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public JedisShardInfo getShardInfo(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		JedisShardInfo result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getShardInfo(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public String getKeyTag(String key) {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		String result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getKeyTag(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Collection<JedisShardInfo> getAllShardInfo() {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Collection<JedisShardInfo> result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getAllShardInfo();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

	public Collection<Jedis> getAllShards() {
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
		Collection<Jedis> result = null;
		if (shardedJedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = shardedJedis.getAllShards();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}

}