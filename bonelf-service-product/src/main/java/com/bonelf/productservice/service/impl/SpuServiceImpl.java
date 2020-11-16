package com.bonelf.productservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonelf.productservice.domain.entity.Spu;
import com.bonelf.productservice.mapper.SpuMapper;
import com.bonelf.productservice.service.SpuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author caiyuan
 * @since 2020-07-31
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
}
