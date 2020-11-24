package com.bonelf.cicada.enums;

/**
 * 枚举工厂
 * @author bonelf
 * @since 2020/8/31 17:37
 */
public class EnumFactory {
    
    /**
     * 注释：通过code 获取value
     */
    public static <T extends CodeEnum> T getByCode(Object code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (each.getCode().toString().equals(code.toString())) {
                return each;
            }
        }
        throw new NullPointerException("invalid enum code");
    }

    public static <T extends CodeEnum> T getByCodeCanNull(Object code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (each.getCode().equals(code)) {
                return each;
            }
        }
        return null;
    }

    /**
     * <p>
     * 获得中文值
     * </p>
     * @author bonelf
     * @since 2020/8/31 17:37
     */
    public static <T extends CodeEnum> String getEnumString(Object code, Class<T> enumClass) {
        T enums = getByCode(code, enumClass);
        return enums == null ? "-" : enums.getValue();
    }
}
