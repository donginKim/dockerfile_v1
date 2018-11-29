package sample.configs.data;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.sql.DataSource;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.MongoException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Profile("cloud")
@Configuration
@ServiceScan
public class CloudDataConfig extends AbstractCloudConfig {

	@Value("${db.mysql.servicename}")
	private String mysqlServiceName;

	private String cubridJdbcUrl;

//	@Value("${mongodb.service.name}")
//	private String mongoServiceName;

	@Value("${redis.service.name}")
	private String redisServiceName;

	@Value("${rabbitmq.service.name}")
	private String rabbitServiceName;

	
	/**
	 * 
	 * <pre>
	 * desc : Mysql DB Connection Settings
	 * </pre>
	 * 
	 * @Method Name : mysqlDataSource
	 * @date : 2015. 11. 5.
	 * @author : csupshin
	 * 
	 * @return
	 */
	@Bean(name = "dsMysql")
	@Primary
	DataSource mysqlDataSource() {

		CloudFactory cloudFactory = new CloudFactory();
		Cloud cloud = cloudFactory.getCloud();
		ServiceInfo serviceInfo = cloud.getServiceInfo(mysqlServiceName);
		String serviceId = serviceInfo.getId();
		return cloud.getServiceConnector(serviceId, DataSource.class, null);

	}

	@Bean(name = "jdbcMysql")
	@Autowired
	public JdbcTemplate mysqlJdbcTemplate(@Qualifier("dsMysql") DataSource dsSlave) {
		return new JdbcTemplate(dsSlave);
	}

	/**
	 * 
	 * <pre>
	 * desc : Cubrid DB Connection Settings
	 * </pre>
	 * 
	 * @Method Name : cubridDataSource
	 * @date : 2015. 11. 5.
	 * @author : csupshin
	 * 
	 * @return
	 */
	@Bean(name = "dsCubrid")
	public DataSource cubridDataSource() {
		try {

			String vcap_services = System.getenv("VCAP_SERVICES");
			JSONObject jsonObj = JSONObject.fromObject(vcap_services);
			JSONArray userPro = jsonObj.getJSONArray("CubridDB");
			jsonObj = JSONObject.fromObject(userPro.get(0));
			jsonObj = jsonObj.getJSONObject("credentials");
			cubridJdbcUrl = jsonObj.getString("jdbcurl");

			return new SimpleDriverDataSource(cubrid.jdbc.driver.CUBRIDDriver.class.newInstance(), cubridJdbcUrl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean(name = "jdbcCubrid")
	@Autowired
	public JdbcTemplate cubridJdbcTemplate(@Qualifier("dsCubrid") DataSource dsSlave) {
		return new JdbcTemplate(dsSlave);
	}

	/**
	 * MongoDBFactory
	 */
	@Autowired
	MongoDbFactory mongoDbFactory;

	/**
	 * 
	 * <pre>
	 * desc :  MongoDB Connection Settings
	 * </pre>
	 * 
	 * @Method Name : mongoTemplate
	 * @date : 2015. 11. 11.
	 * @author : csupshin
	 * 
	 * @return
	 * @throws MongoException
	 * @throws UnknownHostException
	 */
//	@SuppressWarnings("deprecation")
//	@Bean(name = "mongoTemplate")
//	public MongoTemplate mongoTemplate() throws UnknownHostException {
//
//		CloudFactory cloudFactory = new CloudFactory();
//		Cloud cloud = cloudFactory.getCloud();
//		MongoServiceInfo serviceInfo = (MongoServiceInfo) cloud.getServiceInfo(mongoServiceName);
//
//		// MongoDB 인증 처리
//		mongoDbFactory.getDb().authenticate(serviceInfo.getUserName(), serviceInfo.getPassword().toCharArray());
//
//		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
//
//		return mongoTemplate;
//	}

	/**
	 * 
	 * <pre>
	 * desc :  Reddis Connection Settings
	 * </pre>
	 * 
	 * @Method Name : mongoTemplate
	 * @date : 2015. 11. 11.
	 * @author : csupshin
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	@Bean
	public JedisPool redisTemplate() {

		CloudFactory cloudFactory = new CloudFactory();
		Cloud cloud = cloudFactory.getCloud();
		RedisServiceInfo serviceInfo = (RedisServiceInfo) cloud.getServiceInfo(redisServiceName);

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(128);
		return new JedisPool(poolConfig, serviceInfo.getHost(), serviceInfo.getPort(), 5, serviceInfo.getPassword());
	}

	@Bean
	@Primary
	public CachingConnectionFactory rabbitmqConnectionFactory() throws IOException {

		CloudFactory cloudFactory = new CloudFactory();
		Cloud cloud = cloudFactory.getCloud();

		AmqpServiceInfo serviceInfo = (AmqpServiceInfo) cloud.getServiceInfo(rabbitServiceName);

		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(serviceInfo.getHost());
		connectionFactory.setPort(serviceInfo.getPort());
		connectionFactory.setUsername(serviceInfo.getUserName());
		connectionFactory.setPassword(serviceInfo.getPassword());
		connectionFactory.setVirtualHost(serviceInfo.getVirtualHost());
		
		try {
			// SslProtocol 사용 설정
			connectionFactory.useSslProtocol("TLS");

		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);

		return cachingConnectionFactory;
	}

	/**
	 * 
	 * <pre>
	 * desc : RabbitMq Template Setting
	 * </pre>
	 * 
	 * @Method Name : amqpTemplate
	 * @date : 2015. 11. 17.
	 * @author : csupshin
	 * 
	 * @return
	 */
	@Bean(name="dynamicExchange")
	public RabbitTemplate amqpTemplate(
			@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory connectionFactory) {

		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

//		rabbitTemplate.setQueue("sampleQueue");
//		rabbitTemplate.setRoutingKey("sampleQueue");
		rabbitTemplate.setConnectionFactory(connectionFactory);
		return rabbitTemplate;
	}

	/**
	 * 
	 * <pre>
	 * desc :  RabbitMq Admin Setting
	 * </pre>
	 * 
	 * @Method Name : rabbitAdmin
	 * @date : 2015. 11. 17.
	 * @author : csupshin
	 * 
	 * @return
	 */
	@Bean
	public RabbitAdmin rabbitAdmin(@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory connectionFactory) {

		RabbitAdmin admin = new RabbitAdmin(connectionFactory);
		return admin;
	}
	
	@Autowired
	Gson gson;
	
	@Bean
	public AccountConfig accountConfig(){

		String vcap_services = System.getenv("VCAP_SERVICES");
        JsonObject jsonObj = gson.fromJson(vcap_services, JsonElement.class).getAsJsonObject();
        
        JsonArray userPro = jsonObj.getAsJsonArray("glusterfs");
        jsonObj = userPro.get(0).getAsJsonObject().getAsJsonObject("credentials");
//        String provider = jsonObj.get("provider").getAsString();
        String tenantName = jsonObj.get("tenantname").getAsString();
        String username = jsonObj.get("username").getAsString();
        String password = jsonObj.get("password").getAsString();
        String authUrl = jsonObj.get("auth_url").getAsString();
		
		AccountConfig config = new AccountConfig();
		config.setUsername(username);
		config.setTenantName(tenantName);
		config.setPassword(password);
		config.setAuthUrl(authUrl + "/tokens");
		config.setAuthenticationMethod(AuthenticationMethod.KEYSTONE);
		return config;
	}

	@Bean
	public AccountFactory accountFactory(AccountConfig accountConfig){
		return new AccountFactory(accountConfig);
	}
	
	@Bean
	public Account account(AccountFactory factory){
		return factory.createAccount();
	}
	
}