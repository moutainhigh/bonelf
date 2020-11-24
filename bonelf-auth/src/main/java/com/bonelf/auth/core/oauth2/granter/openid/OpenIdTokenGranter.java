/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.auth.core.oauth2.granter.openid;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.bonelf.auth.core.oauth2.granter.base.BaseApiTokenGranter;
import com.bonelf.auth.entity.User;
import com.bonelf.auth.service.UserService;
import com.bonelf.common.domain.Result;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * @author joe_chen
 * 短信验证码登录与用户名密码登录相似,密码为动态
 * 故继承ResourceOwnerPasswordTokenGranter
 */
public class OpenIdTokenGranter extends BaseApiTokenGranter {
    private final WxMaService wxMaService;
    private final UserService userService;
    protected static final String GRANT_TYPE = "openid";

    private String openId;
    private String unionId;

    public OpenIdTokenGranter(AuthenticationManager authenticationManager,
                              AuthorizationServerTokenServices tokenServices,
                              ClientDetailsService clientDetailsService,
                              OAuth2RequestFactory requestFactory,
                              WxMaService wxMaService,
                              UserService userService) {
        super(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.wxMaService = wxMaService;
        this.userService = userService;
    }

    @Override
    protected String getUsernameParam(Map<String, String> parameters) {
        String code = parameters.get("code");
        String encryptedData = parameters.get("encryptedData");
        String iv = parameters.get("iv");
        return code;
    }

    @Override
    protected String getPasswordParam(Map<String, String> parameters) {
        String code = parameters.get("code");
        String encryptedData = parameters.get("encryptedData");
        String iv = parameters.get("iv");
        return encryptedData;
    }

    @Override
    protected void handleBadCredentialsException(String username, String password, BadCredentialsException exp) {
        //TODO 用户不存在注册
        Result<User> userResult = userService.registerByOpenId(openId, unionId);
        throw new InvalidGrantException(exp.getMessage());
    }
}
