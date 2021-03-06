package com.bonelf.userservice.controller.api;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.common.client.SupportFeignClient;
import com.bonelf.common.config.property.BonelfProperty;
import com.bonelf.common.constant.BonelfConstant;
import com.bonelf.common.controller.base.BaseApiController;
import com.bonelf.common.core.aop.annotation.MustFeignRequest;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.bonelf.common.domain.Result;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.userservice.constant.enums.UserStatusEnum;
import com.bonelf.userservice.core.exception.UserExceptionEnum;
import com.bonelf.userservice.domain.dto.AccountLoginDTO;
import com.bonelf.userservice.domain.dto.WechatLoginDTO;
import com.bonelf.userservice.domain.dto.WechatRegisterUserDTO;
import com.bonelf.userservice.domain.entity.User;
import com.bonelf.userservice.domain.response.UserResponse;
import com.bonelf.userservice.domain.vo.LoginVO;
import com.bonelf.userservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * yon接口
 * </p>
 * @author bonelf
 * @since 2020/10/30 9:29
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户接口")
public class UserController extends BaseApiController {
	@Autowired
	private UserService userService;
	@Autowired
	private BonelfProperty bonelfProperty;
	@Autowired
	private SupportFeignClient supportFeignClient;
	@Autowired
	private RedisUtil redisUtil;

	@Deprecated
	@ApiOperation("账号密码登录")
	@PostMapping(value = "/v1/loginByAccount")
	public Result<LoginVO> loginByAccount(@Validated @RequestBody AccountLoginDTO accountLoginDto) {
		return Result.ok(userService.loginByAccount(accountLoginDto));
	}

	@Deprecated
	@ApiOperation("微信登录")
	@PostMapping(value = "/v1/wxLogin")
	public Result<LoginVO> wxLogin(@Validated @RequestBody WechatLoginDTO wechatLoginDto) throws WxErrorException {
		LoginVO user = userService.login(wechatLoginDto);
		return Result.ok(user);
	}

	/*===========================Feign FIXME 为Feign新建模块 ===========================*/

	@GetMapping(value = "/v1/getUser")
	public Result<UserResponse> getUser(@RequestParam("uniqueId") String uniqueId) {
		User user = userService.getOne(Wrappers.<User>lambdaQuery()
				.eq(User::getUserId, uniqueId).or()
				.eq(User::getPhone, uniqueId).or()
				.eq(User::getOpenId, uniqueId).orderByDesc(User::getUpdateTime).last("limit 1"));
		if (user == null) {
			return Result.error(CommonBizExceptionEnum.DB_RESOURCE_NULL, "用户");
		}
		UserResponse resp = BeanUtil.copyProperties(user, UserResponse.class);
		return Result.ok(resp);
	}

	@MustFeignRequest
	@PostMapping(value = "/v1/registerByPhone")
	public Result<User> registerByPhone(@RequestParam("phone") String phone) {
		User past = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
		Result<User> errorCheckResult = registerUserCheck(past);
		if (errorCheckResult != null) {
			return errorCheckResult;
		}
		User user = new User();
		user.setAvatar(bonelfProperty.getBaseUrl() + BonelfConstant.DEFAULT_AVATAR_PATH);
		user.setPhone(phone);
		user.setLastLoginTime(LocalDateTime.now());
		userService.save(user);
		userService.update(Wrappers.<User>lambdaUpdate().set(User::getNickname, "手机用户").eq(User::getUserId, user.getUserId()).last("limit 1"));
		return Result.ok(user);
	}

	@MustFeignRequest
	@PostMapping(value = "/v1/registerByMail")
	public Result<User> registerByMail(@RequestParam("mail") String mail) {
		User past = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getMail, mail));
		Result<User> errorCheckResult = registerUserCheck(past);
		if (errorCheckResult != null) {
			return errorCheckResult;
		}
		User user = new User();
		user.setAvatar(bonelfProperty.getBaseUrl() + BonelfConstant.DEFAULT_AVATAR_PATH);
		user.setMail(mail);
		user.setLastLoginTime(LocalDateTime.now());
		userService.save(user);
		userService.update(Wrappers.<User>lambdaUpdate().set(User::getNickname, "邮箱用户").eq(User::getUserId, user.getUserId()).last("limit 1"));
		return Result.ok(user);
	}

	private Result<User> registerUserCheck(User past) {
		if (past != null) {
			if (UserStatusEnum.FREEZE.getCode().equals(past.getStatus())) {
				return Result.error(UserExceptionEnum.FREEZE_USER);
			}
			return Result.error(UserExceptionEnum.ALREADY_REGISTER);
		}
		return null;
	}

	@MustFeignRequest
	@PostMapping(value = "/v1/registerByOpenId")
	public Result<User> registerByOpenId(@RequestBody WechatRegisterUserDTO wechatRegisterUserDto) {
		User past = userService.getOne(Wrappers.<User>lambdaQuery()
				.eq(User::getOpenId, wechatRegisterUserDto.getOpenId()).or()
				.eq(User::getUnionId, wechatRegisterUserDto.getUnionId()).last("limit 1"));
		Result<User> errorCheckResult = registerUserCheck(past);
		if (errorCheckResult != null) {
			return errorCheckResult;
		}
		User user = BeanUtil.copyProperties(wechatRegisterUserDto, User.class);
		if (user.getAvatar() == null) {
			user.setAvatar(bonelfProperty.getBaseUrl() + BonelfConstant.DEFAULT_AVATAR_PATH);
		}
		user.setLastLoginTime(LocalDateTime.now());
		userService.save(user);
		if (user.getNickname() == null) {
			userService.update(Wrappers.<User>lambdaUpdate().set(User::getNickname, "手机用户").eq(User::getUserId, user.getUserId()));
		}
		return Result.ok(user);
	}

	@Deprecated
	@PostMapping(value = "/v1/getPermission")
	public Result<Map<String, Set<String>>> getPermission(@RequestParam Long userId) {
		return Result.ok(userService.getApiUserRolesAndPermission(userId));
	}

	/**
	 * 留待子类实现
	 * @return
	 */
	@Override
	protected IService<User> getCrudService() {
		return userService;
	}
}
