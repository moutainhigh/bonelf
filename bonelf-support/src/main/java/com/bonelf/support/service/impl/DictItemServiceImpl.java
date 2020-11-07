package com.bonelf.support.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonelf.support.domain.entity.DictItem;
import com.bonelf.support.mapper.DictItemMapper;
import com.bonelf.support.service.DictItemService;
import org.springframework.stereotype.Service;
@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

}
