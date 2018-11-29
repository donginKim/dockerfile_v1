/*
 * Copyright www.crossent.com.,LTD.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of www.crossent.com.,LTD. ("Confidential Information").
 */
package sample.biz.impl; 

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sample.biz.RabbitMQService;

/**
 * <pre>
 * sample.biz.impl 
 *    |_ RabbitMQServiceImpl.java
 * 
 * </pre>
 * @date : 2016. 2. 14. 오전 10:19:53
 * @version : 
 * @author : csupshin
 */
@Service("rabbitMQService")
public class RabbitMQServiceImpl implements RabbitMQService{

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RabbitAdmin rabbitAdmin;
	
	@Resource(name = "dynamicExchange" )
	private volatile RabbitTemplate dynamicRabbitTemplate;
	
	/**
	 * DB 타입과 해당 조직의 id를 받아서 Queue를 생성한다.
	 * 
	 * @param id
	 * @param dbType
	 * 
	 */
	@Override
	public void createQueue(String type, String id) {
		
		String queueName = makeQueueName(type,id);
		rabbitAdmin.declareQueue(new Queue(queueName));
		rabbitAdmin.getRabbitTemplate().setRoutingKey(queueName);
		rabbitAdmin.getRabbitTemplate().afterPropertiesSet();
		LOGGER.info("Make : {}" +  queueName);
		
	}

	/**
	 * DB 타입과 조직의 id에 해당하는 Queue를 찾아 메시지를 삽입한다.
	 * 
 	 * @param id
	 * @param dbType
	 * 
	 */
	@Override
	public void inputQueue(String type, String id, String message) {
		String queueName = makeQueueName(type,id);
		LOGGER.info("Input : " +queueName);
		dynamicRabbitTemplate.convertAndSend(queueName, message);
	}

	/**
	 * DB 타입과 조직의 id에 해당하는 Queue의 내용을 받아온다.
	 *
	 * @param id
	 * @param dbType
	 * @return Queue에 들어 있는 상태값
	 * 
	 */
	@Override
	public String requestQueue(String type, String id) {
		String queueName = makeQueueName(type,id);
		
		LOGGER.info("Get : " +queueName);
		String result = (String) dynamicRabbitTemplate.receiveAndConvert(queueName);
		rabbitAdmin.purgeQueue(queueName, true);
		return result;
	}

	/**
	 *	DB 타입과 해당 조직의 id를 받아서 Queue 이름 생성
	 *
	 * @param id
	 * @param dbType
	 * @return Queue의 이름
	 * 
	 */
	String makeQueueName(String type, String id) {
		return id + "_" + type;
	}
}
