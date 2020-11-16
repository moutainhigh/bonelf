package com.bonelf.userservice.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonelf.cicada.util.CipherCryptUtil;
import com.bonelf.cicada.util.Md5CryptUtil;
import com.bonelf.common.constant.AuthConstant;
import com.bonelf.common.constant.BonlfConstant;
import com.bonelf.common.constant.CacheConstant;
import com.bonelf.common.constant.ShiroRealmName;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.BizExceptionEnum;
import com.bonelf.common.util.JwtTokenUtil;
import com.bonelf.common.util.SmsUtil;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.userservice.domain.dto.AccountLoginDTO;
import com.bonelf.userservice.domain.dto.WechatLoginDTO;
import com.bonelf.userservice.domain.entity.User;
import com.bonelf.userservice.domain.vo.LoginVO;
import com.bonelf.userservice.mapper.UserMapper;
import com.bonelf.userservice.service.UserService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private SmsUtil smsUtil;
	@Resource
	private WxMaService wxMaService;

	@Value("${choujiang.base-url:http://localhost}")
	private String baseUrl;

	@Override
	public String sendVerify(String phone) {
		if (redisUtil.get(String.format(CacheConstant.LOGIN_VERIFY_CODE, phone)) != null) {
			throw new BonelfException(BizExceptionEnum.NO_REPEAT_SUBMIT, redisUtil.getExpire(CacheConstant.LOGIN_VERIFY_CODE));
		}
		String code = RandomUtil.randomNumbers(6);
		//smsUtil.sendVerify(phone, code);
		redisUtil.set(String.format(CacheConstant.LOGIN_VERIFY_CODE, phone), code, CacheConstant.VERIFY_CODE_EXPIRED_SECOND);
		return code;
	}

	@Override
	public Map<String, Set<String>> getApiUserRolesAndPermission(Long userId) {
		Map<String, Set<String>> permission = new HashMap<>(2);
		permission.put("roles", CollectionUtil.newHashSet("example"));
		permission.put("permissions", CollectionUtil.newHashSet("api:user:example"));
		return permission;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public LoginVO loginByAccount(AccountLoginDTO dto) {
		User user = this.baseMapper.selectOneByPhone(dto.getUsername());
		if (dto.getVerifyCode() == null) {
			if (user == null) {
				throw new BonelfException(BizExceptionEnum.DB_RESOURCE_NULL, "用户");
			}
			//密码登录
			String password;
			try {
				password = CipherCryptUtil.decrypt(dto.getPassword(), dto.getUsername(), AuthConstant.FRONTEND_SAIT_CRYPTO);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BonelfException(BizExceptionEnum.DECRYPT_ERROR);
			}
			//if (!Pattern.matches(RegexpConstant.NUMBERS_AND_LETTERS, password)) {
			//	throw new BonelfException(BizExceptionEnum.REQUEST_INVALIDATE_EMPTY, "请输入6-16位数字,字母组成的密码");
			//}
			if (!Md5CryptUtil.encrypt(password, AuthConstant.DATABASE_SALT_MD5).equals(Md5CryptUtil.encrypt(password, AuthConstant.DATABASE_SALT_MD5))) {
				throw new BonelfException(BizExceptionEnum.REQUEST_INVALIDATE_EMPTY, "密码不正确");
			}
			this.baseMapper.update(new User(), Wrappers.<User>lambdaUpdate().set(User::getLastLoginTime, LocalDateTime.now()).eq(User::getUserId, user.getUserId()));
		} else {
			//验证码登录
			String trueVerifyCode = (String)redisUtil.get(String.format(CacheConstant.LOGIN_VERIFY_CODE, dto.getUsername()));
			if (!dto.getVerifyCode().equals(trueVerifyCode)) {
				throw new BonelfException(BizExceptionEnum.REQUEST_INVALIDATE_EMPTY, "验证码错误");
			}
			if (user == null) {
				//注册
				user = new User();
				//if (Pattern.matches(RegexpConstant.VALIDATE_PHONE, dto.getUsername())) {
				user.setPhone(dto.getUsername());
				//}
				user.setPassword(Md5CryptUtil.encrypt(user.getPhone().substring(user.getPhone().length() - 6), AuthConstant.DATABASE_SALT_MD5));
				user.setLastLoginTime(LocalDateTime.now());
				user.setAvatar(baseUrl + BonlfConstant.DEFAULT_AVATAR_PATH);
				this.baseMapper.insert(user);
				user.setNickname("用户" + HexUtil.toHex(user.getUserId()));
				this.baseMapper.updateById(user);
			} else {
				this.baseMapper.update(new User(), Wrappers.<User>lambdaUpdate().set(User::getLastLoginTime, LocalDateTime.now()).eq(User::getUserId, user.getUserId()));
			}
			redisUtil.del(CacheConstant.LOGIN_VERIFY_CODE, user.getPhone());
		}
		String token = JwtTokenUtil.generateToken(user.getUserId(), user.getPhone(), ShiroRealmName.API_SHIRO_REALM);
		//存储token 刷新token用 初始的对应关系为 自己对自己
		redisUtil.set(String.format(com.bonelf.common.constant.CacheConstant.API_USER_TOKEN_PREFIX, user.getUserId()), token, AuthConstant.REFRESH_SECOND);

		return LoginVO.builder()
				.token(token)
				.tokenType(AuthConstant.TOKEN_PREFIX.trim())
				.expiresIn(AuthConstant.EXPIRATION_SECOND)
				.user(user)
				.build();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public LoginVO login(WechatLoginDTO wechatLoginDto) throws WxErrorException {
		// 获取微信用户session
		WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(wechatLoginDto.getCode());
		if (null == session) {
			throw new BonelfException(BizExceptionEnum.THIRD_FAIL, "session获取失败");
		}
		WxMaUserInfo wxUserInfo = wxMaService.getUserService().getUserInfo(session.getSessionKey(), wechatLoginDto.getEncryptedData(), wechatLoginDto.getIv());
		if (null == wxUserInfo) {
			throw new BonelfException(BizExceptionEnum.THIRD_FAIL, "无法找到用户信息");
		}
		String openId = wxUserInfo.getOpenId();
		String unionId = wxUserInfo.getUnionId();
		User user = this.baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getOpenId, openId).orderByDesc(User::getUpdateTime).last("limit 1"));
		if (user == null) {
			// 注册用户
			// 解密手机号码信息
			WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxMaService.getUserService().getPhoneNoInfo(session.getSessionKey(),
					wechatLoginDto.getEncryptedData(), wechatLoginDto.getIv());
			if (wxMaPhoneNumberInfo == null || !StringUtils.hasText(wxMaPhoneNumberInfo.getPhoneNumber())) {
				// 解密手机号码信息错误
				throw new BonelfException(BizExceptionEnum.THIRD_FAIL, "解析手机号失败");
			}
			String phone = wxMaPhoneNumberInfo.getPhoneNumber();
			user = new User();
			user.setPhone(phone);
			user.setOpenId(openId);
			user.setUnionId(unionId);
			user.setLastLoginTime(LocalDateTime.now());
			user.setCountry(wxUserInfo.getCountry());
			user.setCity(wxUserInfo.getCity());
			user.setProvince(wxUserInfo.getProvince());
			user.setAvatar(wxUserInfo.getAvatarUrl());
			user.setGender(Byte.parseByte(wxUserInfo.getGender()));
			user.setLanguage(wxUserInfo.getLanguage());
			user.setNickname(wxUserInfo.getNickName());
			this.baseMapper.insert(user);
		} else {
			this.baseMapper.update(new User(),
					Wrappers.<User>lambdaUpdate()
							.set(User::getLastLoginTime, LocalDateTime.now())
							.eq(User::getUserId, user.getUserId()));
		}
		String token = JwtTokenUtil.generateToken(user.getUserId(), user.getPhone(), ShiroRealmName.API_SHIRO_REALM);
		//存储token 刷新token用 初始的对应关系为 自己对自己
		redisUtil.set(String.format(CacheConstant.API_USER_TOKEN_PREFIX, user.getUserId()), token, AuthConstant.REFRESH_SECOND);
		return LoginVO.builder()
				.token(token)
				.tokenType(AuthConstant.TOKEN_PREFIX.trim())
				.expiresIn(AuthConstant.EXPIRATION_SECOND)
				.user(user)
				.build();
	}
}
