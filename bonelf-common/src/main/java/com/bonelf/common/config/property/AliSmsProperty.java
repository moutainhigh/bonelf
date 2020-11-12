package com.bonelf.common.config.property;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 短信验证码
 * @author:qingcong
 * @date:2019/11/5
 * @ver:1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "ali.sms")
public class AliSmsProperty {

    private String accessKeyId;

    private String accessSecret;

    private String signName;
}
