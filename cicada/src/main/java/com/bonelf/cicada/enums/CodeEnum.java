package com.bonelf.cicada.enums;

/**
 * 注释：枚举接口 （规范）
 *
 * @Author: caiyuan
 * @Date: 2020/8/6 0006 9:22
 * @Version: v1.FreezeEnum
 */
public interface CodeEnum {

    /**
     * 注释：获取code
     */
    <T> T getCode();

    /**
     * 获取code
     *
     * @return 泛型
     */
    <T> T getValue();
}
