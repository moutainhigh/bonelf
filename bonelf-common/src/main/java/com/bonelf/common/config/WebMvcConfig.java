package com.bonelf.common.config;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.bonelf.common.core.interceptor.DebugInterceptor;
import com.bonelf.common.core.interceptor.FeignInterceptor;
import com.bonelf.common.core.serializer.RestObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * web服务配置
 * //@Autowired
 * //private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;
 * //@Autowired
 * //private RedisUtil redisUtil;
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 拦截器配置
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// feign请求拦截器
		registry.addInterceptor(new FeignInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/swagger-ui");
		// 接口token刷新拦截器
		//registry.addInterceptor(new RefreshTokenInterceptor(redisUtil))
		//		.addPathPatterns("/**")
		//		.excludePathPatterns("/swagger-ui");
		// 接口debug拦截器
		registry.addInterceptor(new DebugInterceptor())
				.addPathPatterns("/**");
	}

	/**
	 * 消息转换器 配置
	 * @param converters 转化你
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//converters.add(fastJsonHttpMessageConverter());
		converters.add(jacksonHttpMessageConverter());
		converters.add(stringHttpMessageConverter());
	}

	/**
	 * 防止中文乱码的转换器
	 */
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter(StandardCharsets.UTF_8);
	}

	/**
	 * 通过jackson解析请求
	 * 使用@JsonBackReference、@JsonManagedReference处理循环依赖
	 * @return 转换器
	 */
	@Bean
	public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		//ObjectMapper objectMapper = jackson2ObjectMapperBuilder.build();
		ObjectMapper objectMapper = new RestObjectMapper();
		//结果是否格式化
		objectMapper.writerWithDefaultPrettyPrinter();
		jackson2HttpMessageConverter.setObjectMapper(objectMapper);
		return jackson2HttpMessageConverter;
	}

	/**
	 * 反序列化
	 * LocalDateTime
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return builder -> {
			//空字符串转null RestObjectMapper.class 写了
			//JsonSerializer<String> serializer = new StdSerializer<String>(String.class) {
			//	@Override
			//	public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			//		if (value == null || "".equals(value.trim()) || "null".equals(value) || "undefined".equals(value)) {
			//			gen.writeNull();
			//		}
			//		gen.writeString(value);
			//	}
			//};
			//builder.serializerByType(String.class, serializer);
			builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
		};
	}

	/**
	 * 通过FastJson解析请求
	 * 不建议使用FastJson 因为据说bug多，而且大部分人和框架使用的Jackson更好兼容
	 * @return 转换器
	 */
	@Deprecated
	public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(
				/*禁止重复、循环引用 （你可以在返回的list中拿出一个或一些放在另一个字段返回），但是在循环引用时也会导致StackOverflowError异常
				  重复引用：
				  List<Object> list = new ArrayList<>();
				  Object obj = new Object();
				  list.add(obj);
			  	  list.add(obj);
				  循环引用:
				  1:自引用
				  Map<String,Object> map = new HashMap<>();
				  map.put("map",map);
				  2:map1引用了map2，而map2又引用map1，导致循环引用
				  Map<String,Object> map1 = new HashMap<>();
				  Map<String,Object> map2 = new HashMap<>();
				  map1.put("map",map2);
				  map2.put("map",map1);
				*/
				SerializerFeature.DisableCircularReferenceDetect,
				//List字段如果为null,输出为[],而非null
				SerializerFeature.WriteNullListAsEmpty,
				//是否输出值为null的字段,默认为false
				SerializerFeature.WriteMapNullValue,
				//字符串null返回空字符串
				SerializerFeature.WriteNullStringAsEmpty,
				//空布尔值返回false
				SerializerFeature.WriteNullBooleanAsFalse,
				//结果是否格式化,默认为false
				SerializerFeature.PrettyFormat);
		//格式化日期 已在String2DateConfig配置
		fastJsonConfig.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
		converter.setFastJsonConfig(fastJsonConfig);
		SerializeConfig serializeConfig = SerializeConfig.globalInstance;
		//Long转换为String，前端Js接收17位以上long会精度缺失
		serializeConfig.put(Long.class, com.alibaba.fastjson.serializer.ToStringSerializer.instance);
		serializeConfig.put(Long.TYPE, com.alibaba.fastjson.serializer.ToStringSerializer.instance);
		//BigDecimal转化为String
		serializeConfig.put(BigDecimal.class, com.alibaba.fastjson.serializer.ToStringSerializer.instance);
		fastJsonConfig.setSerializeConfig(serializeConfig);
		return converter;
	}
}
