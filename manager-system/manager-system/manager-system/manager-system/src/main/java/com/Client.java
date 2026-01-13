package com;



import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class Client {

    private static final Long TIME_LOCKED = 50 * 1000l;
    private static final String KEY_LOCKED = "myLock";
    private static RedissonClient redissonClient = null;

    public static void main(String[] args) {
        initRedissonClient();
        lock();
    }

    private static void initRedissonClient() {
        // 1. Create config object
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // 2. Create Redisson instance
        Client.redissonClient = Redisson.create(config);
    }

    private static void lock() {
        RLock lock1 = redissonClient.getLock(KEY_LOCKED);
        lock1.lock();

        // 处理业务逻辑
        try {
            Thread.sleep(TIME_LOCKED);
            reLock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();

        }
    }

    /**
     * 测试锁的重入
     */
    private static void reLock() {
        RLock lock1 = redissonClient.getLock(KEY_LOCKED);
        lock1.lock();

        // 处理业务逻辑
        try {
            Thread.sleep(TIME_LOCKED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
        }
    }
}