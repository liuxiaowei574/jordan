package com.nuctech.ls.model.memcached;

import org.springframework.stereotype.Component;

import com.nuctech.util.MemcachedServerProperties;
import com.schooner.MemCached.SchoonerSockIOPool;
import com.whalin.MemCached.MemCachedClient;

/**
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * BSD license
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the BSD License along with this library.
 *
 * @author sunming
 */

@Component
public class MemcachedUtil {

    /**
     * memcached客户端单例
     */
    private static MemCachedClient cachedClient = new MemCachedClient();

    /**
     * 初始化连接池
     */
    static {
        // 获取连接池的实例
        SchoonerSockIOPool pool = SchoonerSockIOPool.getInstance();

        String serverprop = MemcachedServerProperties.getKeyValue(MemcachedServerProperties.SERVERS);

        // 服务器列表及其权重
        String[] servers = { serverprop };
        Integer[] weights = { 3 };

        // 设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);

        // 设置初始连接数、最小连接数、最大连接数、最大处理时间
        pool.setInitConn(10);
        pool.setMinConn(10);
        pool.setMaxConn(1000);
        pool.setMaxIdle(1000 * 60 * 60);

        // 设置连接池守护线程的睡眠时间
        pool.setMaintSleep(60);

        // 设置TCP参数，连接超时
        pool.setNagle(false);
        pool.setSocketTO(60);
        pool.setSocketConnectTO(0);

        // 初始化并启动连接池
        pool.initialize();

        // 压缩设置，超过指定大小的都压缩
        // cachedClient.setCompressEnable(true);
        // cachedClient.setCompressThreshold(1024*1024);
    }

    public MemcachedUtil() {

    }

    public boolean add(String key, Object value) {
        return cachedClient.add(key, value);
    }

    public boolean add(String key, Object value, Integer expire) {
        return cachedClient.add(key, value, expire);
    }

    public boolean put(String key, Object value) {
        return cachedClient.set(key, value);
    }

    public boolean put(String key, Object value, Integer expire) {
        return cachedClient.set(key, value, expire);
    }

    public boolean replace(String key, Object value) {
        return cachedClient.replace(key, value);
    }

    public boolean replace(String key, Object value, Integer expire) {
        return cachedClient.replace(key, value, expire);
    }

    public Object get(String key, Class<?> className) {
        cachedClient.setTransCoder(new MemcachedTransCoder(className));
        return cachedClient.get(key);
    }

    public boolean delete(String key) {
        return cachedClient.delete(key);
    }

}
