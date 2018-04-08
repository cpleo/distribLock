package cn.chen;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class redisManage {

    private static redisManage instance = null;

    private JedisPoolConfig poolConfig = new JedisPoolConfig();

    private String default_host = "127.0.0.1";

    private JedisPool pool =new JedisPool(poolConfig,default_host);

    public static redisManage getInstance(){
        if(instance==null){
            synchronized (redisManage.class){
                if(instance==null){
                    instance = new redisManage();
                    return instance;
                }else {
                    return instance;
                }
            }
        }else {
            return instance;
        }
    }

    private redisManage(){
    }

    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public JedisPool getPool() {
        return pool;
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }
}
