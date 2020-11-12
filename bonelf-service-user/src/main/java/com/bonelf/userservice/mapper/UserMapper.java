package com.bonelf.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bonelf.userservice.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
	User selectOneByPhone(String username);
}