package com.bonelf.common.controller;

import cn.hutool.core.util.StrUtil;
import com.bonelf.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 一些譬如清除缓存等debug操作的接口
 * </p>
 * @author bonelf
 * @since 2020/10/11 23:20
 */
@RestController
@RequestMapping("/sys/error")
public class ErrorController {

	@ApiOperation("401")
	@GetMapping(value = "/401")
	public Result<String> err401(@RequestParam(required = false) String errMsg) {
		return Result.error("401", StrUtil.nullToDefault(errMsg, "please try login again"));
	}

}
