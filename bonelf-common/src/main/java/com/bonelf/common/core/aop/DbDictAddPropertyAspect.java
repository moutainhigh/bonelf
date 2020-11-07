package com.bonelf.common.core.aop;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonelf.common.core.aop.annotation.dict.DbDict;
import com.bonelf.common.domain.Result;
import com.bonelf.common.util.DbDictUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 字典aop类
 * 返回的实际是Fast JSONObject
 * FIXME 这么做全局配置的FastJson转换器是否失效？ √
 * FIXME 列表里的复杂对象无法解析 如果要转就需要 再循环转化，那如何判断是不是POJO？转成json使用JSON.isValidObject？
 * @author bonelf
 * @date 2020-10-27
 */
@Aspect
//@Component
@Slf4j
@Deprecated
public class DbDictAddPropertyAspect {
	/**
	 * queryDictTextByKey的@Cacheable的Aop生效需要注入自己
	 */
	@Autowired
	private DbDictUtil dbDictUtil;


	// 定义切点Pointcut
	@Pointcut("execution(public * com.bonelf..*.*Controller.*(..))")
	public void executeService() {
	}

	@Around("executeService()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long time1 = System.currentTimeMillis();
		Object result = pjp.proceed();
		long time2 = System.currentTimeMillis();
		log.debug("supportFeignClient获取JSON数据 耗时：" + (time2 - time1) + "ms");
		long start = System.currentTimeMillis();
		this.parseDictText(result);
		long end = System.currentTimeMillis();
		log.debug("DbDict解析注入JSON数据  耗时" + (end - start) + "ms");
		return result;
	}

	/**
	 * 本方法针对返回对象为Result 的IPage的分页列表数据进行动态字典注入
	 * 例输入当前返回值的就会多出一个sex_{nameSuffix}字段
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	private void parseDictText(Object result) {
		if (result instanceof Result) {
			//泛型类型发生转换
			if (((Result<?>)result).getResult() instanceof IPage) {
				List<Object> items = new ArrayList<>();
				for (Object record : ((IPage<?>)((Result<?>)result).getResult()).getRecords()) {
					items.add(parseObjectDictText(record));
				}
				((IPage<Object>)((Result<?>)result).getResult()).setRecords(items);
			} else if (((Result<?>)result).getResult() instanceof Collection) {
				List<Object> items = new ArrayList<>();
				for (Object record : ((Collection<?>)((Result<?>)result).getResult())) {
					items.add(parseObjectDictText(record));
				}
				((Result<Object>)result).setResult(items);
			} else if (((Result<?>)result).getResult().getClass().isArray()) {
				List<Object> items = new ArrayList<>();
				for (Object record : ((Object[])((Result<?>)result).getResult())) {
					items.add(parseObjectDictText(record));
				}
				((Result<Object>)result).setResult(items);
			} else {
				Object obj = ((Result<?>)result).getResult();
				((Result<Object>)result).setResult(parseObjectDictText(obj));
			}
		} else {
			log.warn("没有通过Result包装来返回的数据无法进行字典处理~");
			//result = parseObjectDictText(result);
		}
	}

	/**
	 * 当对象增加
	 * @param record
	 * @return
	 */
	private Object parseObjectDictText(Object record) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "{}";
		try {
			json = mapper.writeValueAsString(record);
		} catch (JsonProcessingException e) {
			log.error("json解析失败" + e.getMessage(), e);
		}
		if (!JSON.isValidObject(json) && !JSON.isValidArray(json)) {
			//非POJO
			return record;
		}
		JSONObject item = JSONObject.parseObject(json);
		for (Field field : ReflectUtil.getFields(record.getClass())) {
			DbDict annotation = field.getAnnotation(DbDict.class);
			if (annotation != null) {
				String code = annotation.value();
				String nameSuffix = annotation.nameSuffix();
				String key = String.valueOf(item.get(field.getName()));
				//翻译字典值对应的txt
				String textValue = translateDictValue(code, key, annotation.cached());
				log.debug("字典Val : " + textValue);
				log.debug("翻译字典字段： " + field.getName() + nameSuffix + ":" + textValue);
				item.put(field.getName() + nameSuffix, textValue);
			}
			//date类型默认转换string格式化日期 这个交给消息转化器处理
			//if (field.getType().getName().equals("java.util.Date")
			//		&& field.getAnnotation(JsonFormat.class) == null
			//		&& item.get(field.getName()) != null) {
			//	SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//	item.put(field.getName(), aDate.format(new Date((Long)item.get(field.getName()))));
			//}
		}
		return item;
	}

	/**
	 * 翻译字典文本
	 * @param code
	 * @param key
	 * @return
	 */
	private String translateDictValue(String code, String key, boolean cache) {
		if (!StringUtils.hasText(key)) {
			return null;
		}
		StringBuilder textValue = new StringBuilder();
		String[] keys = key.split(",");
		for (String k : keys) {
			String tmpValue;
			if (k.trim().length() == 0) {
				continue; //跳过循环
			}
			tmpValue = cache ? dbDictUtil.queryDictTextByKey(code, k.trim()) : dbDictUtil.queryDictTextByKeyNoCache(code, k.trim());
			if (tmpValue != null) {
				if (!"".equals(textValue.toString())) {
					textValue.append(",");
				}
				textValue.append(tmpValue);
			}
		}
		return textValue.toString();
	}

}
