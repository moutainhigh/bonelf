package com.bonelf.auth.client.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Deprecated
//@Component
public class SmsCodeProviderFallback implements SmsCodeProvider {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String getSmsCode(String mobile, String businessType) {
        // 该类为mock, 目前暂时没有sms的服务
        return passwordEncoder.encode("980826");
    }
}
