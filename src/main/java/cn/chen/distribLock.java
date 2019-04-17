package cn.chen;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import org.apache.commons.io.FileUtils;

import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class distribLock{

    private static int DEFAULT_EXPIRE_TIME = 6;

    private static String lockScript ;

    private static String unlockScript ;

    private static String localIp ;
    /**
     * redis ip
     */
    public static String host = "127.0.0.1";
    /**
     * redis端口号
     */
    public static int port = 6379;
    {
        try {
            localIp = InetAddress.getLocalHost().getAddress().toString();
        }catch (UnknownHostException e){
            localIp = UUID.randomUUID().toString();
        }
    }
    public static boolean lock(String lockName){
        return lock(lockName,Thread.currentThread(),DEFAULT_EXPIRE_TIME);
    }

    public static boolean lock(String lockName,long expire){
        return lock(lockName,Thread.currentThread().hashCode(),expire);
    }

    public static boolean lock(String lockName,Object object,long expire){
        try {
            JedisPool pool = new JedisPool(host,port);
            Jedis redis = pool.getResource();
            File file = ResourceUtils.getFile("classpath:script/lock.lua");
            if(StringUtils.isEmpty(lockScript)) {
                lockScript = new String(FileUtils.readFileToByteArray(file));
            }
            while (true) {
                Object rs = redis.eval(lockScript, 1, lockName,localIp+object.toString(), expire + "");
                if((Long)rs==1) {break;}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void unlock(String lockName,Object object){
        try {
            Jedis redis = redisManage.getInstance().getPool().getResource();
            File file = ResourceUtils.getFile("classpath:script/unlock.lua");
            if(StringUtils.isEmpty(unlockScript)) {
                unlockScript = new String(FileUtils.readFileToByteArray(file));
            }
            Object rs = redis.eval(unlockScript, 1, lockName, object.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unlock(String lockName){
        unlock(lockName,Thread.currentThread().hashCode());
    }

}
