package com.bonelf.common.constant.enums;

import com.bonelf.cicada.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesOrNotEnum implements CodeEnum {
    /**
     * Yes Or Not
     */
    Y(true, "是", 1),
    N(false, "否", 0);

    private Boolean flag;
    private String value;
    private Integer code;

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            YesOrNotEnum[] var1 = values();
            int var2 = var1.length;

            for (YesOrNotEnum s : var1) {
                if (s.getCode().equals(status)) {
                    return s.getValue();
                }
            }

            return "";
        }
    }

    public Boolean getFlag() {
        return this.flag;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
