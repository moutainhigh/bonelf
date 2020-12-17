package com.bonelf.auth.client.provider;

import com.bonelf.auth.domain.entity.Role;
import com.bonelf.auth.domain.request.RegisterUserRequest;
import com.bonelf.auth.domain.response.UserResponse;
import com.bonelf.common.cloud.constant.ServiceNameConstant;
import com.bonelf.common.cloud.feign.FeignConfig;
import com.bonelf.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * <p>
 * 签权服务, fallback = UserClientFallback.class
 * </p>
 * @author bonelf
 * @since 2020/11/17 15:37
 */
@FeignClient(name = ServiceNameConstant.USER_SERVICE, configuration = FeignConfig.class, fallback = UserClientFallback.class)
public interface UserClient {

    /**
     * 查询用户
     * @param uniqueId
     * @return
     */
    @GetMapping(value = "/bonelf/user/v1/getUser")
    Result<UserResponse> getUserByUniqueId(@RequestParam("uniqueId") String uniqueId);

    /**
     * 用户角色
     * @param userId
     * @return
     */
    @GetMapping(value = "/bonelf/role/v1")
    Result<Set<Role>> queryRolesByUserId(@RequestParam("userId") Long userId);

    /**
     * 注册
     * @param phone
     * @return
     */
    @PostMapping(value = "/bonelf/user/v1/registerByPhone")
    Result<UserResponse> registerByPhone(@RequestParam("phone") String phone);

    /**
     * 微信注册
     * @param registerUser
     * @return
     */
    @PostMapping(value = "/bonelf/user/v1/registerByOpenId")
    Result<UserResponse> registerByOpenId(@RequestBody RegisterUserRequest registerUser);

    @PostMapping(value = "/bonelf/user/v1/registerByMail")
    Result<UserResponse> registerByMail(@RequestParam("mail") String mail);
}
