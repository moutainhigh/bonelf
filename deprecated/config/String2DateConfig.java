package com.bonelf.common.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * java.time、date转换器
 * 参数接收
 */
//@Configuration
@Deprecated
public class String2DateConfig {

    private final RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    public String2DateConfig(RequestMappingHandlerAdapter handlerAdapter) {
        this.handlerAdapter = handlerAdapter;
    }

    /**
     * 默认时间转化器
     */
    @PostConstruct
    public void addConversionConfig() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        if ((initializer != null ? initializer.getConversionService() : null) != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(new StringToDateConverter());
            genericConversionService.addConverter(new StringToLocalDateConverter());
            genericConversionService.addConverter(new StringToLocalDateTimeConverter());
            genericConversionService.addConverter(new StringToLocalTimeConverter());
        }

    }

    /**
     * String转换Date
     *
     * @author chuanfu
     * @version v1.0
     * @date 2019-11-22
     */
    private static class StringToDateConverter implements Converter<String, Date> {
        @Override
        public Date convert(String dateString) {
            return DateUtil.parse(dateString);
        }
    }

    /**
     * String转换LocalDate
     */
    private static class StringToLocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String dateString) {
            if (StringUtils.hasText(dateString)) {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
            }
            return null;
        }
    }


    /**
     * String转换LocalDateTime
     */
    private static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String dateString) {
            if (StringUtils.hasText(dateString)) {
                return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
            }
            return null;
        }
    }

    /**
     * String转换LocalDate
     *
     * @author chuanfu
     * @version v1.0
     * @date 2019-11-22
     */
    private static class StringToLocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String dateString) {
            if (StringUtils.hasText(dateString)) {
                return LocalTime.parse(dateString, DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
            }
            return null;
        }
    }

}
