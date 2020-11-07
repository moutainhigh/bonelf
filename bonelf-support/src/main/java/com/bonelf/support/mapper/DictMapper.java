package com.bonelf.support.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bonelf.support.domain.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {

	String selectDictTest(@Param("code") String code,@Param("value") String value);
}