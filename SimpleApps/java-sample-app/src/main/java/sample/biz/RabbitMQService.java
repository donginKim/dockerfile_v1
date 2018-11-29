/*
 * Copyright www.crossent.com.,LTD.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of www.crossent.com.,LTD. ("Confidential Information").
 */
package sample.biz;

/**
 * <pre>
 * sample.biz 
 *    |_ RabbitMQService.java
 * 
 * </pre>
 * @date : 2016. 2. 14. 오전 10:19:10
 * @version : 
 * @author : csupshin
 */
public interface RabbitMQService {

	public void createQueue(String type, String id);
	
	public void inputQueue(String type, String id, String message);
	
	public String requestQueue(String type, String id);
	
}
