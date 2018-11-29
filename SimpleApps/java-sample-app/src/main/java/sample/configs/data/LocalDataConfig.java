package sample.configs.data;

import java.net.UnknownHostException;

import javax.sql.DataSource;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.Mongo;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Profile("default")
@Configuration
public class LocalDataConfig {
	//
	@Value("${db.mongodb.url}")
	private String MONGO_DB_URL;

	@Value("${db.mongodb.dbname}")
	private String MONGO_DB_DBNAME;

	@Value("${redis.host}")
	private String REDIS_URL;

	@Value("${redis.port}")
	private int REDIS_PORT;

	@Value("${redis.password}")
	private String REDIS_PASSWORD;

	
	@Value("${rabbitmq.url}")
	private String RABBIT_URL;

	@Value("${rabbitmq.username}")
	private String RABBIT_USERNAME;

	@Value("${rabbitmq.password}")
	private String RABBIT_PASSWORD;
	
	@Value("${rabbitmq.virtualhost}")
	private String RABBIT_VIRTUAL_HOST;
	
	@Value("${rabbitmq.port}")
	private int RABBIT_PORT;
	
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
	@SuppressWarnings("unused")
	@Bean(name = "dsMysql")
	@Primary
	@ConfigurationProperties(prefix = "db.mysql")
	public DataSource mysqlDataSource() {

		DriverManagerDataSource dataSource;
		dataSource = new DriverManagerDataSource();
		return DataSourceBuilder.create().build();

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
	@ConfigurationProperties(prefix = "db.cubrid")
	public DataSource cubridDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcCubrid")
	@Autowired
	public JdbcTemplate cubridJdbcTemplate(@Qualifier("dsCubrid") DataSource dsSlave) {
		return new JdbcTemplate(dsSlave);
	}

	@SuppressWarnings("deprecation")
	public @Bean MongoDbFactory mongoDbFactory() throws UnknownHostException {

		// MongoClientURI uri = new
		// MongoClientURI("mongodb://root:qwer1234@10.30.60.31:27017,10.30.60.32:27017,10.30.60.33:27017/test?authSource=admin&authMechanism=SCRAM-SHA-1");
		// System.out.println(uri.getURI());

		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(new Mongo(MONGO_DB_URL), MONGO_DB_DBNAME);
		return simpleMongoDbFactory;

	}

	/**
	 * 
	 * <pre>
	 * desc : RabbitMq Connection Setting
	 * </pre>
	 * 
	 * @Method Name : mongoTemplate
	 * @date : 2015. 11. 10.
	 * @author : csupshin
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	@Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException {
		return new MongoTemplate(mongoDbFactory());
	}

	@Bean
	public JedisPool redisTemplate() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(128);
		return new JedisPool(poolConfig, REDIS_URL, REDIS_PORT);
	}

	@Bean(name = "rabbitmqConnectionFactory")
	@Autowired
	public CachingConnectionFactory rabbitmqConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

		connectionFactory.setHost(RABBIT_URL);
		connectionFactory.setUsername(RABBIT_USERNAME);
		connectionFactory.setPassword(RABBIT_PASSWORD);
		connectionFactory.setVirtualHost(RABBIT_VIRTUAL_HOST);
		connectionFactory.setPort(RABBIT_PORT);
		connectionFactory.setChannelCacheSize(50);
		return connectionFactory;
	}
	
	@Bean(name="dynamicExchange")
	public RabbitTemplate amqpTemplate(@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory connectionFactory) {

		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		//rabbitTemplate.setExchange("rmq.rube.exchange");
		//rabbitTemplate.setRoutingKey("test.cloud");
				
		return rabbitTemplate;
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin(@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory connectionFactory) {
		
		RabbitAdmin admin = new RabbitAdmin(connectionFactory);		
		return admin;
	}
	
	

} 