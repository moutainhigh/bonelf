package com.bonelf.common.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.bonelf.common.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * controller层
 * 理论不用@Component
 */
@Component
public abstract class BaseApiController {
	//随着继承BaseApiController 即注入 可以的话试试private
	@Autowired
	protected RedisUtil redisUtil;
	@Autowired
	protected HttpServletRequest request;

	/**
	 * <p>
	 * 默认分页
	 * </p>
	 * @author bonelf
	 * @since 2020/9/15 15:39
	 */
	public <T> Page<T> defaultPage() {
		long page;
		long limit;
		String pageSizeStr = request.getParameter("page");
		String pageIndexStr = request.getParameter("limit");
		if (pageSizeStr == null || pageIndexStr == null) {
			throw new BonelfException(CommonBizExceptionEnum.REQUEST_INVALIDATE, "分页参数未传");
		}
		try {
			//页数
			page = Long.parseLong(pageSizeStr);
			//每页记录条数
			limit = Long.parseLong(pageIndexStr);
		} catch (NumberFormatException e) {
			throw new BonelfException(CommonBizExceptionEnum.REQUEST_INVALIDATE, "分页参数错误");
		}
		if (limit > 500) {
			throw new BonelfException(CommonBizExceptionEnum.REQUEST_INVALIDATE, "页数超出限制");
		}
		return new Page<>(page, limit);
	}


	public <T> Page<T> defaultPage(Long pageDefault, Long limitDefault) {
		long page;
		long limit;
		String pageSizeStr = request.getParameter("page");
		String pageIndexStr = request.getParameter("limit");
		//页数
		page = pageSizeStr == null ? pageDefault : Long.parseLong(pageSizeStr);
		//每页记录条数
		limit = pageIndexStr == null ? limitDefault : Long.parseLong(pageIndexStr);
		return new Page<T>(page, limit);
	}

	/**
	 * 获得用户编号
	 * @return
	 */
	protected Long getUserId() {
		System.out.println("User:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication()));
		return 1L;
	}

	/**
	 * 获得用户编号
	 * @return
	 */
	protected Long getUserIdCanNull() {
		System.out.println("User:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication()));
		return 1L;
	}

	/**
	 * 获得用户手机号
	 * @return
	 */
	protected String getPhone() {
		System.out.println("User:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication()));
		return "13758233011";
	}
}
