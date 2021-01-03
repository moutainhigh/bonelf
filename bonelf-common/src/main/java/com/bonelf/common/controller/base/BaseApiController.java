/*
 * Copyright (c) 2020. Bonelf.
 */

package com.bonelf.common.controller.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.bonelf.common.domain.Result;
import com.bonelf.common.util.redis.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * controller层
 * 理论不用@Component
 */
@Component
public abstract class BaseApiController<T> {
	//随着继承BaseApiController 即注入 可以的话试试private
	@Autowired
	protected RedisUtil redisUtil;
	@Autowired
	protected HttpServletRequest request;

	/**
	 * 留待子类实现
	 * @return
	 */
	protected abstract IService<T> getCrudService();


	@ApiOperation("保存")
	@PostMapping("")
	public Result<T> save(@RequestBody T entity) {
		getCrudService().save(entity);
		return Result.ok(entity);
	}

	@ApiOperation("全量更新")
	@PutMapping("")
	public Result<Boolean> update(@RequestBody T entity) {
		return Result.ok(getCrudService().updateById(entity));
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public <ID extends Serializable> Result<Boolean> remove(@PathVariable ID id) {
		return Result.ok(getCrudService().removeById(id));
	}

	@ApiOperation("详情")
	@GetMapping("/{id}")
	public <ID extends Serializable> Result<T> load(@PathVariable ID id) {
		return Result.ok(getCrudService().getById(id));
	}

	@ApiOperation("全量列表")
	@GetMapping("/list")
	public Result<List<T>> find(T data) {
		return Result.ok(getCrudService().list(new QueryWrapper<T>(data)));
	}

	@ApiOperation("分页查询")
	@GetMapping("/page")
	public Result<Page<T>> findPage(T entity) {
		return Result.ok(getCrudService().page(defaultPage(), new QueryWrapper<T>(entity)));
	}

	@ApiOperation("批量更新、添加")
	@RequestMapping(path = "/batchUpdate", method = {RequestMethod.POST, RequestMethod.PUT})
	public Result<Boolean> batchUpdate(@RequestBody List<T> entities) {
		return Result.ok(getCrudService().saveOrUpdateBatch(entities));
	}

	@ApiOperation("批量删除")
	@DeleteMapping(path = "/batchRemove")
	public <ID extends Serializable> Result<Boolean> batchRemove(@RequestBody List<ID> entities) {
		return Result.ok(getCrudService().removeByIds(entities));
	}

	@ApiOperation("按id批量查")
	@GetMapping("/loadByKeys")
	public Result<List<T>> loadByPrimaryKeys(@RequestParam("ids") List<? extends Serializable> values) {
		return Result.ok(getCrudService().listByIds(values));
	}


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
}
