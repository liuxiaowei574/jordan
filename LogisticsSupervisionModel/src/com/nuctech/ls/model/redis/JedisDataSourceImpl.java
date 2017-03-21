package com.nuctech.ls.model.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Repository("jedisDataSource")
public class JedisDataSourceImpl implements JedisDataSource {
	private static final Logger LOG = LoggerFactory.getLogger(JedisDataSourceImpl.class);

	@Autowired
	private ShardedJedisPool shardedJedisPool;

	@Override
	public ShardedJedis getRedisClient() {
		ShardedJedis shardJedis = null;
		try {
			shardJedis = shardedJedisPool.getResource();
			return shardJedis;
		} catch (Exception e) {
			LOG.error("[JedisDS] getRedisClent error:" + e.getMessage());
			if (null != shardJedis)
				shardJedis.close();
		}
		return null;
	}

	@Override
	public void returnResource(ShardedJedis shardedJedis) {
		shardedJedis.close();
	}

	@Override
	public void returnResource(ShardedJedis shardedJedis, boolean broken) {
		shardedJedis.close();
	}

}