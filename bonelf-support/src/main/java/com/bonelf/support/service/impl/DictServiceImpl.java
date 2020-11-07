package com.bonelf.support.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonelf.support.domain.entity.Dict;
import com.bonelf.support.mapper.DictMapper;
import com.bonelf.support.service.DictService;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

	/**
	 * 查询字典值
	 * @param code
	 * @param value
	 * @return
	 */
	@Override
	public String getDictText(String code, String value) {
		String txt = this.baseMapper.selectDictTest(code, value);
		return txt == null ? "-" : txt;
	}
}
