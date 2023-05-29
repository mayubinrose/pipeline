package com.ctg.cicd.common.cache.redis;

import com.ctg.itrdc.cache.pool.CacheServiceException;
import com.ctg.itrdc.cache.pool.CtgJedisPool;
import com.ctg.itrdc.cache.pool.CtgJedisPoolException;
import com.ctg.itrdc.cache.pool.ProxyJedis;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.commands.MultiKeyCommands;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.util.SafeEncoder;

import javax.annotation.Resource;
import java.io.*;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtil {

    private String host;

    private int port;

    @Resource
    private  RedisTemplate<String, Object> redisTemplate;

    private static final String OK = "OK";

    //@Value("${spring.redis.password}")
    private String password;

    private CtgJedisPool ctgJedisPool;


    /* ----------- common --------- */

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key,long time){
        try {
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public  Collection<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(Collection<String> key) {
        redisTemplate.delete(key);
    }

    /* ----------- string --------- */
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object getObject(String key){
/*        if(StringUtils.isNotEmpty(password)){
            System.out.println("passwd ----"+password);
        }*/
        return key==null?null:redisTemplate.opsForValue().get(key);
    }



    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setObject(String key,Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setObject(String key,Object value,long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                setObject(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置redis缓存保存数据的天数
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean setObjectForDays(String key,Object value,int time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.DAYS);
            }else{
                setObject(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置默认保存多少分钟
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean setObjectForMins(String key,Object value,int time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
            }else{
                setObject(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("setObjectForMins error, key:{}", key, e);
            return false;
        }
    }

    /**
     * 往set数据类型添加value
     * @param key set的key
     * @param value set的值
     * @return 是否成功
     */
    public boolean sadd(String key, Object value) {
        try {
            redisTemplate.opsForSet().add(key, value);
            return true;
        } catch (Exception e) {
            log.error("sadd error, key:{}", key, e);
            return false;
        }
    }

    public boolean sisMember(String key,Object value){
        try{
            return redisTemplate.opsForSet().isMember(key,value);
        } catch (Exception e) {
            log.error("sismember error, key:{}", key, e);
            return false;
        }
    }

    /**
     * 统计set数据类型中value的个数，不存在或者一场就返回0
     * @param key 需要统计的key
     * @return value的个数
     */
    public long scard(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("scard error, key:{}", key, e);
            return 0;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递增
     * @param key 键
     * @param value 要增加几(大于0)
     * @return
     */
    public void setLongValue(String key,Long value){
            RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        counter.set(value);
        counter.expire(-1, TimeUnit.SECONDS);
        counter.getAndSet(value);
    }

    public void sendMessage(String channel,String msg){
        redisTemplate.convertAndSend(channel,msg);
    }

    /**
     * 分布式锁
     * @Description 若使用公司组建ctgCache，则改用ctgCache锁；否则自己创建jedis锁。
     * @param lockName key
     * @param lockValue value
     * @param expireTime 失效时间，单位毫秒
     * @param timeout 获取锁超时时间，单位毫秒
     * @return boolean：true获取成功；false：获取失败
     * @throws InterruptedException
     */
    public boolean lock(String lockName, String lockValue, long expireTime,long timeout)throws InterruptedException{
        return ctgJedisPool == null ? lockWithoutCtgJedis(lockName,lockValue,expireTime,timeout) :
                lockWithCtgJedis(lockName, lockValue, expireTime, timeout);
    }

    /**
     * 解锁分布式锁
     * @Description 若使用公司组件ctgCache，则改用ctgCache锁；否则自己创建jedis锁。
     * @param lockName key
     * @param lockValue value
     */
    public void unlock(String lockName, String lockValue){
        if (ctgJedisPool == null){
            unlockWithoutCtgJedis(lockName,lockValue);
        } else {
            unlockWithCtgJedis(lockName, lockValue);
        }
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    public  RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public  void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //保存文件方法
    public void setFile(String key,String path,String host ,int port){
        Jedis redis = new Jedis(host,port);
        if(StringUtils.isNotEmpty(password)){
            redis.auth(password);
        }
        File fr = new File(path);
        redis.set(key.getBytes(), object2Bytes(fr));
    }

    //读取文件对象方法
    public File getFile(String key,String host,int port){
        Jedis redis = new Jedis(host,port);
        if(StringUtils.isNotEmpty(password)){
            redis.auth(password);
        }
        File file = (File)byte2Object(redis.get(key.getBytes()));
        return file;
    }

    public  byte[] object2Bytes(Object value) {
        if (value == null){
            return null;
        }

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(arrayOutputStream);
            outputStream.writeObject(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                arrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arrayOutputStream.toByteArray();
    }
    //反序列化方法
    public  Object byte2Object(byte[] bytes) {
        if (bytes == null || bytes.length == 0){
            return null;
        }

        try {
            ObjectInputStream inputStream;
            inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object obj = inputStream.readObject();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
    public int getPort() {
        return port;
    }


    public Set<String> scan(String pattern) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = Sets.newHashSet();

            try{
                JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                MultiKeyCommands multiKeyCommands = (MultiKeyCommands) commands;

                ScanParams scanParams = new ScanParams();
                scanParams.match(pattern);
                scanParams.count(50);
                ScanResult<String> scan = multiKeyCommands.scan("0", scanParams);
                while (null != scan.getCursor()) {
                    keys.addAll(scan.getResult());
                    if (!StringUtils.equals("0", scan.getCursor())) {
                        scan = multiKeyCommands.scan(scan.getCursor(), scanParams);
                        continue;
                    } else {
                        break;
                    }
                }
                log.info("Scan Success" + pattern);
                return keys;
            }catch (Exception e){
                log.error("scan error" + pattern);
                throw e;
            }finally {
                connection.close();
            }
        });
    }

    private boolean lockWithoutCtgJedis(String lockName, String lockValue, long expireTime, long timeout) {
        log.debug("tryLockWithoutCtgRedis lockName:{} lockValue:{} expireTime:{} timeout:{}",
                lockName, lockValue, expireTime, timeout);
        if (lockValue != null && lockValue.length() != 0) {
            String rs = this.doLock(lockName, lockValue, expireTime, timeout);
            if (!lockValue.equals(rs)) {
                if (rs == null) {
                    throw new JedisDataException("errorCode:" + CacheServiceException.CacheServiceErrorInfo.e4.getErrorCode());
                } else {
                    throw new JedisDataException("errorCode:" + CacheServiceException.CacheServiceErrorInfo.e15.getErrorCode());
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private String doLock(String lockName, String lockValue, long expireTime, long timeout) {
        if (lockName == null) {
            JedisDataException jedisDataException = new JedisDataException("errorCode:" + CacheServiceException.CacheServiceErrorInfo.e15.getErrorCode());
            throw jedisDataException;
        } else {
            String key = com.ctg.itrdc.cache.pool.Constant.DEFAULT_LOCK_PRE + lockName;
            long startTime = System.currentTimeMillis();
            Jedis jedis = null;
            while(true) {
                try {
                    JedisConnection jedisConnection = (JedisConnection) redisTemplate.getConnectionFactory().getConnection();
                    jedis = jedisConnection.getNativeConnection();
                    String result = jedis.psetex(SafeEncoder.encode(key),expireTime, SafeEncoder.encode(lockValue));
                    if (OK.equals(result)) {
                        return lockValue;
                    }

                    String anObject = jedis.get(key);
                    if (lockValue.equals(anObject)) {
                        return lockValue;
                    }

                    if (System.currentTimeMillis() - startTime >= timeout) {
                        return anObject;
                    }

                    Thread.sleep(5L);
                } catch (Exception e) {
                    log.error("doLock error.", e);

                    try {
                        String anObject = jedis.get(key);
                        if (lockValue.equals(anObject)) {
                            return lockValue;
                        }
                    } catch (Exception tryAgainE) {
                        log.error("try lock exceptions", tryAgainE);
                        throw tryAgainE;
                    }
                } finally {
                    try{
                        if(jedis != null){
                            jedis.close();
                            jedis = null;
                        }
                    } catch (Exception e) {
                        log.error("try lock close exceptions", e);
                    }
                }
            }
        }
    }

    private boolean lockWithCtgJedis(String lockName, String lockValue, long expireTime, long timeout) {
        log.debug("LockWithCtgRedis lockName:{} lockValue:{} expireTime:{} timeout:{}",
                lockName, lockValue, expireTime, timeout);
        ProxyJedis jedis = null;
        try {
            jedis = ctgJedisPool.getResource();
            jedis.lock(lockName, lockValue, expireTime, timeout);
            return true;
        } catch (CtgJedisPoolException e) {
            log.error("LockWithCtgRedis lock fail ", e);
            return false;
        } finally {
            try{
                if(jedis != null){
                    jedis.close();
                }
            }catch (Throwable e){
                log.error("LockWithCtgRedis lock close error ", e);
            }
        }
    }

    private void unlockWithCtgJedis(String lockName, String lockValue) {
        ProxyJedis jedis = null;
        try {
            jedis = ctgJedisPool.getResource();
            jedis.unlock(lockName, lockValue);
        } catch (CtgJedisPoolException e) {
            log.error("release redis lock fail lockName:{} lockValue:{}", lockName, lockValue, e);
        }finally {
            try{
                if(jedis != null){
                    jedis.close();
                }
            } catch (Exception e){
                log.error("release redis lock close error lockName:{} lockValue:{}", lockName, lockValue, e);
            }
        }
    }

    private void unlockWithoutCtgJedis(String lockName, String lockValue) {
        if (lockName == null || lockValue == null) {
            throw new JedisDataException(String.format("errorCode:%s", CacheServiceException.CacheServiceErrorInfo.e10.getErrorCode() + "输入参数有误"));
        }

        Jedis jedis = null;
        try {
            String key = com.ctg.itrdc.cache.pool.Constant.DEFAULT_LOCK_PRE + lockName;
            JedisConnection jedisConnection = (JedisConnection) redisTemplate.getConnectionFactory().getConnection();
            jedis = jedisConnection.getNativeConnection();
            String getValue = jedis.get(key);
            if (getValue != null && lockValue != null && lockValue.equals(getValue)) {
                jedis.del(com.ctg.itrdc.cache.pool.Constant.DEFAULT_LOCK_PRE + lockName);
            }

        } catch (Exception e) {
            log.error("unlockWithoutCtgRedis exceptions", e);
            throw e;
        } finally {
            try{
                if(jedis != null){
                    jedis.close();
                    jedis = null;
                }
            } catch (Exception e){
                log.error("release redis lock close error lockName:{} lockValue:{}", lockName, lockValue, e);
            }
        }
    }

}


