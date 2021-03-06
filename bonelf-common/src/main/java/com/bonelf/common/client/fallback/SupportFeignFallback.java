package com.bonelf.common.client.fallback;

import com.bonelf.common.client.SupportFeignClient;
import com.bonelf.common.core.websocket.SocketRespMessage;
import com.bonelf.common.domain.Result;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * feign请求失败返回
 * </p>
 * @author bonelf
 * @since 2020/10/6 0:21
 */
@Slf4j
public class SupportFeignFallback implements SupportFeignClient {

	@Setter
	private Throwable cause;

	@Override
	public Result<String> sendMessage(SocketRespMessage message) {
		return Result.error("消息发送失败");
	}

	@Override
	public Result<String> queryDictTextByKey(String code, String value) {
		log.warn("获取字典值失败code:{},value:{}", code, value);
		return Result.ok("-");
	}

	@Override
	public Result<String> sendVerify(String phone, String businessType) {
		// FIXME: 2020/11/19 取消返回结果
		return Result.ok("980826");
	}

	@Override
	public Result<String> getVerify(String phone, String businessType) {
		// FIXME: 2020/11/19 超时报错返回error
		return Result.ok("980826");
	}
}
