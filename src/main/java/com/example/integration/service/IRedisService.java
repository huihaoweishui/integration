package com.example.integration.service;

public interface IRedisService {
    /**
     * set存数据
     *
     * @param key
     * @param value
     * @return
     */
    void set(String key, String value);

    /**
     * get获取数据
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置有效天数
     *
     * @param key
     * @param expire
     * @return
     */
    boolean expire(String key, long expire);

    /**
     * 移除数据
     *
     * @param key
     * @return
     */
    boolean remove(String key);
}
