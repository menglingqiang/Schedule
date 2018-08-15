package com.menglingqiang.schedule.util;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mlq
 * @date 2018/8/15
 */
public class RedisClient<T> {

  @Autowired
  private JedisPool jedisPool;

  public void set(String key, String value) throws Exception {
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      jedis.set(key, value);
    } finally {
      //返还到连接池
      jedis.close();
    }
  }

  public String get(String key) throws Exception {

    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      return  jedis.get(key);
    } finally {
      //返还到连接池
      jedis.close();
    }
  }

  public void setobj(String key, T value) throws Exception {
    Jedis jedis = null;
    try {
      Set<T> set = new HashSet<T>();
      set.add(value);
      jedis = jedisPool.getResource();
      jedis.sadd(key, String.valueOf(set));
    } finally {
      //返还到连接池
      jedis.close();
    }
  }
  public void setExpire(String key,Integer expire)
  {
    Jedis jedis = null;
    try {
      jedis.expire(key,expire);
    }finally {
      jedis.close();
    }
  }
}
