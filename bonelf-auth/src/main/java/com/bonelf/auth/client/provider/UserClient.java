package com.bonelf.auth.client.provider;

import com.bonelf.auth.entity.Role;
import com.bonelf.auth.entity.User;
import com.bonelf.common.cloud.constant.ServiceNameConstant;
import com.bonelf.common.cloud.feign.FeignConfig;
import com.bonelf.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * <p>
 * 签权服务
 * </p>
 * @author bonelf
 * @since 2020/11/17 15:37
 */
@FeignClient(name = ServiceNameConstant.USER_SERVICE, configuration = FeignConfig.class, fallback = UserClientFallback.class)
public interface UserClient {

    /**
     * 查询用户
     * @param userId
     * @return
     */
    @GetMapping(value = "/user/v1/getUser")
    Result<User> getUserByUniqueId(@RequestParam("userId") Long userId);

    /**
     * 用户角色
     * @param userId
     * @return
     */
    @GetMapping(value = "/role/v1")
    Result<Set<Role>> queryRolesByUserId(@RequestParam("userId") Long userId);

    /**
     * 注册
     * @param phone
     * @return
     */
    @PostMapping(value = "/user/v1/registerByPhone")
    Result<User> registerByPhone(@RequestParam("phone") String phone);

    /**
     * 微信注册
     * @param openId
     * @param unionId
     * @return
     */
    @PostMapping(value = "/user/v1/registerByOpenId")
    Result<User> registerByOpenId(@NonNull @RequestParam("openId") String openId,@NonNull @RequestParam("unionId") String unionId);
}
