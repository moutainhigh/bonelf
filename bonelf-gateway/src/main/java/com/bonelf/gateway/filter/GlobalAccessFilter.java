package com.bonelf.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.bonelf.gateway.core.constant.AuthFeignConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 接口处理请求信息
 * 鉴权卸载JwtFilter中
 * 如果不使用Shiro可以写在这里就好
 * @author bonelf
 * @date 2020-10-12 11:53:08
 */
@Slf4j
@Component
public class GlobalAccessFilter implements GlobalFilter, Ordered {
	@Value("${server.servlet.context-path:}")
	private String ctxPath;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String url = exchange.getRequest().getURI().getPath();
		log.info("  access url :  " + url);
		// 1. 重写StripPrefix(获取真实的URL)
		addOriginalRequestUrl(exchange, exchange.getRequest().getURI());
		String rawPath = exchange.getRequest().getURI().getRawPath();
        //这段代码要求所有子服务不配置context-path 若配置和gateway一样的context-path 删除StringUtil.appearTimes(ctxPath, "/")
		long stripPrefix = StrUtil.count(ctxPath, "/") + 1L;
		String newPath = ctxPath + "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/")).skip(stripPrefix).collect(Collectors.joining("/"));
		ServerHttpRequest newRequest = exchange.getRequest().mutate()
				.path(newPath)
				//将现在的request，添加当前身份 (标识，可以存放redis加强严谨性) （网上原作是在下面，我修改的）
				.header(AuthFeignConstant.AUTH_HEADER, "-")
				.build();
		exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

		//原来newPath不生效的代码，我认为网上作者写错了的
		//ServerHttpRequest mutableReq = exchange.getRequest().mutate().header(AuthFeignConstant.AUTH_HEADER, "").build();
		//ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();

		ServerWebExchange mutableExchange = exchange.mutate().request(newRequest).build();
		return chain.filter(mutableExchange);
	}

	@Override
	public int getOrder() {
		return -200;
	}

}
