package com.bonelf.support.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.support.domain.entity.Dict;
public interface DictService extends IService<Dict>{
	/**
	 * 查询字典值
	 * @param code
	 * @param value
	 * @return
	 */
	String getDictText(String code, String value);
}
