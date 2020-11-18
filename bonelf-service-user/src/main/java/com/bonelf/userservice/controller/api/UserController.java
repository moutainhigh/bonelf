package com.bonelf.userservice.controller.api;

import com.bonelf.common.domain.Result;
import com.bonelf.common.util.BaseApiController;
import com.bonelf.userservice.domain.dto.AccountLoginDTO;
import com.bonelf.userservice.domain.dto.VerifyCodeDTO;
import com.bonelf.userservice.domain.dto.WechatLoginDTO;
import com.bonelf.userservice.domain.entity.User;
import com.bonelf.userservice.domain.vo.LoginVO;
import com.bonelf.userservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * yon接口
 * </p>
 * @author guaishou
 * @since 2020/10/30 9:29
 */
@RestController
@RequestMapping("/v1/user")
@Slf4j
@Api(tags = "用户接口")
public class UserController extends BaseApiController {
	@Autowired
	private UserService userService;

	@ApiOperation("验证码")
	@PostMapping(value = "/sendVerify")
	public Result<String> sendVerify(@Validated @RequestBody VerifyCodeDTO accountLoginDto) {
		// FIXME: 2020/11/2 投入使用后删除此返回值
		return Result.ok(userService.sendVerify(accountLoginDto.getPhone()));
	}

	@ApiOperation("账号密码登录")
	@PostMapping(value = "/loginByAccount")
	public Result<LoginVO> loginByAccount(@Validated @RequestBody AccountLoginDTO accountLoginDto) {
		return Result.ok(userService.loginByAccount(accountLoginDto));
	}

	@ApiOperation("微信登录")
	@PostMapping(value = "/wxLogin")
	public Result<LoginVO> wxLogin(@Validated @RequestBody WechatLoginDTO wechatLoginDto) throws WxErrorException {
		LoginVO user = userService.login(wechatLoginDto);
		return Result.ok(user);
	}

	/*===========================Feign===========================*/

	@GetMapping(value = "/getUser")
	public Result<User> getUser(@RequestParam Long userId) {
		return Result.ok(userService.getById(userId));
	}

	@PostMapping(value = "/getPermission")
	public Result<Map<String, Set<String>>> getPermission(@RequestParam Long userId) {
		return Result.ok(userService.getApiUserRolesAndPermission(userId));
	}
}
