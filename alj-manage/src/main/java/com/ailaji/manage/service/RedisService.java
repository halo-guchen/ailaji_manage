package com.ailaji.manage.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    private <T> T execute(Function<ShardedJedis, T> fun) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return fun.callBack(shardedJedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
    }

    /**
     * 执行set方法
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return this.execute(new Function<ShardedJedis, String>() {
            @Override
            public String callBack(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, value);
            }
        });
    }

    /**
     * 执行set方法,并且设置生存时间
     * 
     * @param key
     * @param value
     * @param seconds 时间，单位是：秒
     * @return
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.execute(new Function<ShardedJedis, String>() {
            @Override
            public String callBack(ShardedJedis shardedJedis) {
                String str = shardedJedis.set(key, value);
                shardedJedis.expire(key, seconds);
                return str;
            }
        });
    }

    /**
     * 执行get方法
     * 
     * @param key
     * @return
     */
    public String get(final String key) {
        return this.execute(new Function<ShardedJedis, String>() {
            @Override
            public String callBack(ShardedJedis e) {
                return e.get(key);
            }
        });
    }

    /**
     * 删除key
     * 
     * @param key
     * @return
     */
    public Long del(final String key) {
        return this.execute(new Function<ShardedJedis, Long>() {
            @Override
            public Long callBack(ShardedJedis e) {
                return e.del(key);
            }
        });
    }

    /**
     * 设置生存时间
     * 
     * @param key
     * @return
     */
    public Long expire(final String key, final Integer seconds) {
        return this.execute(new Function<ShardedJedis, Long>() {
            @Override
            public Long callBack(ShardedJedis e) {
                return e.expire(key, seconds);
            }
        });
    }

    /**
     * Hash结构的获取值
     * 
     * @param key redis中的值的key
     * @param field Hash结构中的字段
     * @return
     */
    public String hget(final String key, final String field) {
        return this.execute(new Function<ShardedJedis, String>() {
            @Override
            public String callBack(ShardedJedis e) {
                return e.hget(key, field);
            }
        });
    }

    /**
     * Hash结构的设置值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(final String key, final String field, final String value) {
        return this.execute(new Function<ShardedJedis, Long>() {
            @Override
            public Long callBack(ShardedJedis e) {
                return e.hset(key, field, value);
            }
        });
    }

    /**
     * Hash结构的设置值并且设置生存时间
     * 
     * @param key
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public Long hset(final String key, final String field, final String value, final Integer seconds) {
        return this.execute(new Function<ShardedJedis, Long>() {
            @Override
            public Long callBack(ShardedJedis e) {
                Long count = e.hset(key, field, value);
                e.expire(key, seconds);
                return count;
            }
        });
    }

    /**
     * Hash结构的数据删除
     * 
     * @param key
     * @param fields
     * @return
     */
    public Long hdel(final String key, final String... fields) {
        return this.execute(new Function<ShardedJedis, Long>() {
            @Override
            public Long callBack(ShardedJedis e) {
                return e.hdel(key, fields);
            }
        });
    }

    /**
     * Hash结构查询所有数据
     * 
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(final String key) {
        return this.execute(new Function<ShardedJedis, Map<String, String>>() {
            @Override
            public Map<String, String> callBack(ShardedJedis e) {
                return e.hgetAll(key);
            }
        });
    }

}
