package com.bonelf.testservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.common.controller.base.BaseApiController;
import com.bonelf.common.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = {"测试接口"})
@RestController
@RequestMapping("/test")
public class RestTestController extends BaseApiController {

	@PreAuthorize("hasRole('test:role') or hasRole('test:role')")
	@ApiOperation(value = "testAuth")
	@GetMapping("/testAuth")
	public Result<?> testAuth() {
		return Result.ok();
	}

	@ApiOperation(value = "testUser")
	@GetMapping("/testUser")
	public Result<?> testUser() {
		log.info("\nall:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication()));
		log.info("\nusername:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
		log.info("\npsw:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getCredentials()));
		log.info("\ntokenInfo:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getDetails()));
		log.info("\npermission:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
		return Result.ok();
	}

	/**
	 * 留待子类实现
	 * @return
	 */
	@Override
	protected IService getCrudService() {
		return null;
	}
}
