package sample.configs.web; 

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * <pre>
 * sample.configs.web 
 *    |_ WebConfig.java
 * 
 * </pre>
 * @date : 2015. 11. 4. 오후 1:13:45
 * @version : 
 * @author : csupshin
 */
@ComponentScan(basePackages = {"sample"})
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter{

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 * <pre>
	 * desc : view Resolver
	 * </pre>
	 * @Method Name : getViewResolver
	 * @date : 2015. 10. 13.
	 * @author : csupshin
	 * 
	 * @return
	 */
	@Bean
	public ViewResolver getViewResolver() {
		final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		return resolver;
	}
	
	/**
	 * 
	 * <pre>
	 * desc : Map Data를 json String 형태로 변환 
	 * </pre>
	 * @Method Name : getObjectMapper
	 * @date : 2015. 10. 13.
	 * @author : csupshin
	 * 
	 * @return
	 */
	@Bean
	public ObjectMapper getObjectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		return objectMapper;
	}
	
	/**
	 * 
	 * <pre>
	 * desc : UI로 전송할 메시지 Json 형태로 변환
	 * </pre>
	 * @Method Name : configureMessageConverters
	 * @date : 2015. 10. 13.
	 * @author : csupshin
	 * 
	 * @param converters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(getObjectMapper());
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SampleInterceptor());
	}
	
    
	/**
	 * 
	 * <pre>
	 * desc : Resolver 설정
	 * </pre>
	 * @Method Name : internalResourceViewResolver
	 * @date : 2015. 12. 14.
	 * @author : csupshin
	 * 
	 * @return
	 */
    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/views/");
        internalResourceViewResolver.setSuffix(".html");
        return internalResourceViewResolver;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/main/{id}").setViewName("main");
//        registry.addViewController("/tbd").setViewName("tbd");
//      registry.addViewController("/manage").setViewName("manage");
    }
    
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
		
	@Bean
	public MultipartResolver multipartResolver(){
		StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
		return multipartResolver;
	}
	
	@Bean
	public Gson gson(){
		return new Gson();
	}
	
//	@Override
//	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers){
//        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
//        simpleMappingExceptionResolver.setDefaultErrorView("/login.html");
//        //simpleMappingExceptionResolver.setDefaultStatusCode(404);
//        exceptionResolvers.add(simpleMappingExceptionResolver);
//	}
}
