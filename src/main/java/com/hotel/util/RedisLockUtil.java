package com.hotel.util;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

public class RedisLockUtil {

	private final StringRedisTemplate redisTemplate;
	private static final String LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

	public RedisLockUtil(StringRedisTemplate stringRedisTemplate) {
		this.redisTemplate = stringRedisTemplate;
	}

	public boolean tryLock(String key, String value, long expireMs) {
		Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, expireMs, TimeUnit.MILLISECONDS);
		return Boolean.TRUE.equals(success);
	}

	public boolean unlock(String key, String value) {
		DefaultRedisScript<Long> script = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
		Long result = redisTemplate.execute(script, Collections.singletonList(key));
		return result != null && result > 0;
	}
}
