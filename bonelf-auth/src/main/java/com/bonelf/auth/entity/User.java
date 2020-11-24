package com.bonelf.auth.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
public class User implements Serializable {
    public static final Long serialVersionUID = -1L;
    /**
     * userId
     */
    private Long userId;
    /**
     * 手机 手机注册、微信登录获取
     */
    private String mobile;
    /**
     * 账号 自动生成、用户注册
     */
    private String username;
    /**
     * openId 微信登录
     */
    private String openId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码 账号、手机登录
     */
    private String password;
    /**
     * 短信、邮箱验证码 手机登录
     */
    private String verifyCode;
    /**
     * 是否可用
     */
    private Boolean enabled;

    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean accountNonLocked;
}