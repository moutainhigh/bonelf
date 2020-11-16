package com.bonelf.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
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

	//@ApiOperation("401")
	//@GetMapping(value = "/error/401")
	//public Result<String> err401(@RequestParam(required = false) String errMsg) {
	//	return Result.error(401, StrUtil.nullToDefault(errMsg, "please try login again"));
	//}

}
