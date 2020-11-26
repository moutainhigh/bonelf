package com.bonelf.userservice.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.bonelf.common.domain.Result;
import com.bonelf.common.util.BaseApiController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * yon接口
 * </p>
 * @author bonelf
 * @since 2020/10/30 9:29
 */
@RestController
@RequestMapping("/role")
@Slf4j
@Api(tags = "角色接口")
public class RoleController extends BaseApiController {
	@GetMapping(value = "/v1")
	public Result<JSONArray> getUser(@RequestParam Long userId) {
		JSONArray json = new JSONArray();
		return Result.ok(json);
	}
}