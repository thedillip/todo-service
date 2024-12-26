package com.todo.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisCacheUtil<K, V> {
    private final RedissonClient redissonClient;

    public void saveOrUpdate(K key, V value, String mapName, long ttl, TimeUnit timeUnit) {
        RMapCache<K, V> rMapCache = redissonClient.getMapCache(mapName);
        rMapCache.put(key, value, ttl, timeUnit);
    }

    public V get(K key, String mapName) {
        RMapCache<K, V> rMapCache = redissonClient.getMapCache(mapName);
        return rMapCache.get(key);
    }

    public Collection<V> getAll(String mapName) {
        RMapCache<K, V> rMapCache = redissonClient.getMapCache(mapName);
        return rMapCache.values();
    }

    public void delete(K key, String mapName) {
        RMapCache<K, V> rMapCache = redissonClient.getMapCache(mapName);
        rMapCache.remove(key);
    }

    public boolean exists(K key, String mapName) {
        RMapCache<K, V> rMapCache = redissonClient.getMapCache(mapName);
        return rMapCache.containsKey(key);
    }

    public void clearRMap(String mapName) {
        RMapCache<K, V> rMapCache = redissonClient.getMapCache(mapName);
        rMapCache.clear();
    }
}
