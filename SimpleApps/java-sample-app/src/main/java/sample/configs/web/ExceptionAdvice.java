package sample.configs.web; 

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 * sample.configs.web 
 *    |_ ExceptionAdvice.java
 * 
 * </pre>
 * @date : 2015. 12. 15. 오후 7:08:59
 * @version : 
 * @author : csupshin
 */
@ControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(value=Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> handleException(Exception ex){
		ex.printStackTrace();
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("error", ex.getMessage());
		return response;
	}
}
