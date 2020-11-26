/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.auth.core.oauth2.granter.mail;

import com.bonelf.auth.core.oauth2.granter.base.BaseApiAuthenticationToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

/**
 * 手机验证码登录Token认证类
 */
@Getter
@Setter
public class MailAuthenticationToken extends BaseApiAuthenticationToken {
    /**
     * 在下面添加自定义内容
     * @see com.bonelf.auth.core.oauth2.enhancer.CustomTokenEnhancer
     */
    private String expPayload = "this is an example payload data";

    public MailAuthenticationToken(Authentication authenticationToken) {
        super(authenticationToken);
    }
}