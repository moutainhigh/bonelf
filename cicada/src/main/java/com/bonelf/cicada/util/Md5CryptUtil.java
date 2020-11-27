/*
 * Copyright 2019-2029 bonelf(https://github.com/bonelf)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bonelf.cicada.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 密码加密工具类
 * @author bonelf
 * @date 2020-11-02
 */
public class Md5CryptUtil {

    /**
     * 密码加盐，再加密
     *
     * @param pwd
     * @param salt
     * @return
     */
    public static String encrypt(String pwd, String salt) {
        return DigestUtils.md5Hex(pwd + salt);
    }

}
