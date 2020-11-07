package com.bonelf.support.controller;

import com.bonelf.common.domain.Result;
import com.bonelf.support.service.DictService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 定时任务在线管理
 * @author bonelf
 * @date  2019-01-02
 */
@RestController
@RequestMapping("/v1/sys/dbdict")
@Slf4j
@Api(tags = "数据库字典接口")
public class DbDictController {
	@Resource
	private DictService dictService;

	@GetMapping(value = "/getByCode")
	public Result<String> queryPageList(@RequestParam String code, @RequestParam String value) {
		return Result.ok(dictService.getDictText(code, value));
	}
}
