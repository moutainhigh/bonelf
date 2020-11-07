package com.bonelf.gateway.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口返回数据格式
 * @author bonelf
 * @date 2019年1月19日
 */
@Data
@NoArgsConstructor
public class GateWayResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	private Boolean success;

	/**
	 * 返回处理消息
	 */
	private String message;

	/**
	 * 返回代码
	 */
	private Integer code;

	/**
	 * 返回数据对象 data
	 */
	private T result;

	/**
	 * 时间戳
	 */
	private Long timestamp;

	/*===========================构造器===========================*/

	public GateWayResult<T> success(Integer code, String message) {
		if (code >= 300 || code < 200) {
			throw new RuntimeException("成功请求不应该返回非200-300的状态码");
		}
		this.message = message;
		this.code = code;
		this.success = true;
		this.timestamp = System.currentTimeMillis();
		return this;
	}

	public GateWayResult<T> success(String message) {
		return this.success(200, message);
	}

	public GateWayResult<T> error500(String message) {
		this.message = message;
		this.code = 500;
		this.success = false;
		this.timestamp = System.currentTimeMillis();
		return this;
	}

	/*===========================静态方法===========================*/

	public static GateWayResult<String> ok() {
		return ok(null);
	}

	public static <T> GateWayResult<T> okMsg(String msg) {
		GateWayResult<T> r = new GateWayResult<T>();
		r.setSuccess(true);
		r.setCode(200);
		r.setTimestamp(System.currentTimeMillis());
		r.setMessage(msg);
		return r;
	}

	public static <T> GateWayResult<T> ok(T data) {
		GateWayResult<T> r = new GateWayResult<T>();
		r.setSuccess(true);
		r.setCode(200);
		r.setTimestamp(System.currentTimeMillis());
		r.setResult(data);
		return r;
	}

	public static <T> GateWayResult<T> error(String msg) {
		return error(500, msg);
	}

	public static <T> GateWayResult<T> error() {
		return error(500, null);
	}

	public static <T> GateWayResult<T> error(int code, String msg) {
		GateWayResult<T> r = new GateWayResult<T>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		r.setTimestamp(System.currentTimeMillis());
		return r;
	}
}