package com.bonelf.support.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bonelf.support.domain.entity.DictItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictItemMapper extends BaseMapper<DictItem> {
}