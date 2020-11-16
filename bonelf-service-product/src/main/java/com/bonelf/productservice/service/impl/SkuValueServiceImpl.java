package com.bonelf.productservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonelf.productservice.domain.entity.SkuValue;
import com.bonelf.productservice.mapper.SkuValueMapper;
import com.bonelf.productservice.service.SkuValueService;
import org.springframework.stereotype.Service;

@Service
public class SkuValueServiceImpl extends ServiceImpl<SkuValueMapper, SkuValue> implements SkuValueService {

	@Override
	public String getSpecsBySkuValueIds(long[] skuValueIds) {
		return this.baseMapper.selectSpecsBySkuValueIds(skuValueIds);
	}
}
