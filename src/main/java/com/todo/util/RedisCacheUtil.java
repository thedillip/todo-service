package com.todo.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class RedisCacheUtil<T> {
    private final RedissonClient redissonClient;

    public void saveOrUpdate(String key, T value, String mapName) {
        RMap<String, T> rMap = redissonClient.getMap(mapName);
        rMap.put(key, value);
    }

    public T get(String key, String mapName) {
        RMap<String, T> rMap = redissonClient.getMap(mapName);
        return rMap.get(key);
    }

    public Collection<T> getAll(String mapName) {
        RMap<String, T> rMap = redissonClient.getMap(mapName);
        return rMap.values();
    }

    public void delete(String key, String mapName) {
        RMap<String, T> rMap = redissonClient.getMap(mapName);
        rMap.remove(key);
    }

    public boolean exists(String key, String mapName) {
        RMap<String, T> rMap = redissonClient.getMap(mapName);
        return rMap.containsKey(key);
    }

    public void clearRMap(String mapName) {
        RMap<Object, Object> rMap = redissonClient.getMap(mapName);
        rMap.clear();
    }
}
