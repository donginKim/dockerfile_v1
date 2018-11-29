package sample.biz;

import java.util.Map;

public interface UserService {
	
	public Map login(Map model) throws Exception;
	public String logout(Map model);
	public Map getRedisValue(Map model) throws Exception;
    
}


