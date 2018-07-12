package com.ray.server.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {


    protected static Logger logger = Logger.getLogger(RedisUtil.class);
    //Redis服务器IP
    @Value("spring.redis.host")
    private static String ADDR0;

    @Value("spring.redis.bkhost")
    private static String ADDR1;

    //Redis的端口号
    @Value("spring.redis.port")
    private static int PORT;

    //访问密码
//    private static String AUTH = FileUtil.getPropertyValue("/properties/redis.properties", "auth");

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    @Value("spring.redis.lettuce.pool.max-active")
    private static int MAX_ACTIVE;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    @Value("spring.redis.lettuce.pool.max-idle")
    private static int MAX_IDLE;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    @Value("spring.redis.lettuce.pool.max-wait")
    private static int MAX_WAIT;

    //超时时间
    @Value("spring.redis.timeout")
    private static int TIMEOUT;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * redis过期时间,以秒为单位
     */
    public final static int EXRP_HOUR = 60*60;          //一小时
    public final static int EXRP_DAY = 60*60*24;        //一天
    public final static int EXRP_MONTH = 60*60*24*30;   //一个月

    /**
     * 初始化Redis连接池
     */
    private static void initialPool(){
        try {

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR0, PORT, TIMEOUT);
        } catch (Exception e) {
            logger.error("First create JedisPool error : "+e);
            try{
                //如果第一个IP异常，则访问第二个IP
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(MAX_ACTIVE);
                config.setMaxIdle(MAX_IDLE);
                config.setMaxWaitMillis(MAX_WAIT);
                config.setTestOnBorrow(TEST_ON_BORROW);
                jedisPool = new JedisPool(config, ADDR1, PORT, TIMEOUT);
            }catch(Exception e2){
                logger.error("Second create JedisPool error : "+e2);
            }
        }
    }


    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (jedisPool == null) {
            initialPool();
        }
    }


    /**
     * 同步获取Jedis实例
     * @return Jedis
     */
    public synchronized static Jedis getJedis() {
        if (jedisPool == null) {
            poolInit();
        }
        Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            }
        } catch (Exception e) {
            logger.error("Get jedis error : "+e);
        }finally{
            returnResource(jedis);
        }
        return jedis;
    }


    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null && jedisPool !=null) {
//            jedisPool.returnResource(jedis);
            jedis.close();
        }

    }


    /**
     * 设置 String
     * @param key
     * @param value
     */
    public static void setString(String key ,String value){
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            getJedis().set(key,value);
        } catch (Exception e) {
            logger.error("Set key error : "+e);
        }
    }

    /**
     * 设置 过期时间
     * @param key
     * @param seconds 以秒为单位
     * @param value
     */
    public static void setString(String key ,int seconds,String value){
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            getJedis().setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("Set keyex error : "+e);
        }
    }

    /**
     * 获取String值
     * @param key
     * @return value
     */
    public static String getString(String key){
        if(getJedis() == null || !getJedis().exists(key)){
            return null;
        }
        return getJedis().get(key);
    }

    private static RedisUtil _instance;

    public static synchronized RedisUtil getInstance(){
        if(_instance != null){
            return _instance;
        }
        _instance = new RedisUtil();
        return _instance;
    }
}
