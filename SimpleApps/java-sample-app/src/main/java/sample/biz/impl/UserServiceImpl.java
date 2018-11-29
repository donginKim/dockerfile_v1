package sample.biz.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sample.biz.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private JedisPool redisTemplate;

    final static String ADMIN_ID = "admin";
    final static String ADMIN_PWD = "admin";
    final static String REDIS_URL = "redis://127.0.0.1:6379";

    /**
     * <pre>
     * desc : DB 조회 없이 상수로 정의된  아이디와 비번으로 비교 후
     *  Catche 저장함
     *  key : "id".hashCode
     *  value : UUID.randomUUID
     * </pre>
     * 
     * @Method Name : login
     * @date : 2015. 11. 4.
     * @author : openPaaS
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public Map login(Map model) throws Exception {

        // DB 조회 없이 상수로 정의된 아이디와 비번으로 비교한다.

        if (!ADMIN_ID.equals(model.get("id").toString())) {
            throw new Exception("Unable to exist user id");
        }

        if (!ADMIN_PWD.equals(model.get("password").toString())) {
            throw new Exception("wrong password");
        }

        // Cache save
        Jedis redis = redisTemplate.getResource();

        Set<String> idKeys = redis.keys(model.get("id").toString());
        Set<String> passwordKeys = redis.keys(model.get("password").toString());
        if (idKeys.size() == 0) {
            //model.put("uuid", UUID.randomUUID().toString());

            model.put("idKey", UUID.randomUUID().toString());
            model.put("passwordKey", UUID.randomUUID().toString());

            redis.set(model.get("id").toString(), model.get("idKey").toString());
            redis.set(model.get("idKey").toString(), model.get("id").toString());
            
            redis.set(model.get("password").toString(), model.get("passwordKey").toString());
            redis.set(model.get("passwordKey").toString(), model.get("password").toString());
            
        } else{
            
            model.put("idKey", redis.get((String)idKeys.toArray()[0]));
            model.put("passwordKey", redis.get((String)passwordKeys.toArray()[0]));
        }
        
        redisTemplate.returnResource(redis);
        
        return model;
    }

    /**
     * <pre>
     * desc : Catche에서 삭제
     * </pre>
     * 
     * @Method Name : logout
     * @date : 2015. 11. 10.
     * @author : openPaaS
     * 
     * @param model
     * @return
     */
    @Override
    public String logout(Map model) {

        String returnStr = "FAIL";
        if (!model.containsKey("idKey"))
            return returnStr = "Not exist 'idKey'";

        // Cache delete
        Jedis redis = redisTemplate.getResource();

        Set<String> keys = redis.keys(model.get("idKey").toString());
        if (keys.size() == 1) {
            redis.del(model.get("idKey").toString());
            returnStr = "Jedis delete OK";
        }

        redisTemplate.returnResource(redis);
        
        return returnStr;
    }
    
    /** 
     *  <pre>
     * desc : Catche에서 정보 읽어오기
     * </pre>
     * @Method Name : getRedisValue
     * @date : 2015. 12. 14.
     * @author : mhlee
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public Map getRedisValue(Map model) throws Exception {

        // Cache save
        Jedis redis = redisTemplate.getResource();

        String id = redis.get(model.get("idKey").toString());
        String password = redis.get(model.get("passwordKey").toString());
        
        Map map = new HashMap();
        if(ADMIN_ID.equals(id) && ADMIN_PWD.equals(password)){
            map.put("id", id );
            map.put("password", password);
        }
                
        return map;
    }
}
