package com.bonelf.testservice.domain.vo;

import com.bonelf.common.constant.enums.YesOrNotEnum;
import com.bonelf.common.core.aop.annotation.dict.DbDict;
import com.bonelf.common.core.aop.annotation.dict.EnumDict;
import lombok.Data;

import java.util.List;

/**
 * example:{"cmdId":2,"message":"你好","data":{"hello":"你好","world","0"}}
 */
@Data
public class TestDictVO2 {
	@DbDict(value = "test", cached = true)
	private String hello = "123";

	private String helloName;

	@EnumDict(YesOrNotEnum.class)
	private Integer world = 1;

	private String worldName;

	private String nihao = null;

	private List<?> shijie = null;
}
