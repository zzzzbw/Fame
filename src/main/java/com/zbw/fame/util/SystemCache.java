package com.zbw.fame.util;

import com.zbw.fame.exception.TipException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据缓存
 *
 * @auther zbw
 * @create 2017/10/10 16:19
 */
public class SystemCache {

    private static final int MAX_CACHE = 2048;

    private static final SystemCache instance = new SystemCache();

    private Map<String, Object> cache;

    public static SystemCache instance() {
        return instance;
    }

    private SystemCache() {
        this(MAX_CACHE);
    }

    private SystemCache(int count) {
        this.cache = new ConcurrentHashMap<>(count);
    }


    public <T> T get(String key) {
        return (T) this.cache.get(key);
    }

    public <T> T get(String key, String field) {
        key = key + ":" + field;
        return (T) this.cache.get(key);
    }

    public void put(String key, Object value) {
        if (cache.size() > MAX_CACHE) {
            throw new TipException("The system cache is full");
        }

        this.cache.put(key, value);
    }

    public void put(String key, String field, Object value) {
        if (cache.size() > MAX_CACHE) {
            throw new TipException("The system cache is full");
        }
        key = key + ":" + field;
        this.cache.put(key,value);
    }

    public void del(String key) {
        cache.remove(key);
    }

    public void del(String key, String field) {
        key = key + ":" + field;
        cache.remove(key);
    }

    public void clean() {
        this.cache.clear();
    }

}
