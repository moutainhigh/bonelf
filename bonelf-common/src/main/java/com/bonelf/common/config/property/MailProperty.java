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
@ConfigurationProperties(prefix = "bonelf.mail")
public class MailProperty {

    /**
     * 网易企业邮 smtp
     */
    private String smtp = "sftp.ym.163.com";
    /**
     * 网易企业邮 username（邮箱地址）
     */
    private String username;
    /**
     * 网易企业邮 密码
     */
    private String password;
}
