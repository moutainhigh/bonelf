package com.bonelf.common.domain;

import com.bonelf.common.constant.BizConstants;
import com.bonelf.common.core.aop.annotation.dict.DictField;
import com.bonelf.common.core.exception.AbstractBaseExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * 接口返回数据格式
 * @author bonelf
 * @date 2019年1月19日
 */
@Data
@NoArgsConstructor
@ApiModel(value = "接口返回对象", description = "接口返回对象")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@ApiModelProperty(value = "成功标志")
	private Boolean success;

	/**
	 * 返回处理消息
	 */
	@ApiModelProperty(value = "返回处理消息")
	private String message;

	/**
	 * 返回代码
	 */
	@ApiModelProperty(value = "返回状态码")
	private String code;

	/**
	 * 返回数据对象 data
	 */
	@DictField
	@ApiModelProperty(value = "返回数据对象")
	private T result;

	/**
	 * 时间戳
	 */
	@ApiModelProperty(value = "时间戳")
	private Long timestamp;

	/*===========================构造器===========================*/

	public Result<T> success(String code, String message) {
		this.message = message;
		this.code = code;
		this.success = true;
		this.timestamp = System.currentTimeMillis();
		return this;
	}

	public Result<T> success(String message) {
		return this.success(BizConstants.CODE_200, message);
	}

	public Result<T> error500(String message) {
		this.message = message;
		this.code = BizConstants.CODE_500;
		this.success = false;
		this.timestamp = System.currentTimeMillis();
		return this;
	}

	/*===========================静态方法===========================*/

	public static <T> Result<T> ok() {
		return ok(null);
	}


	public static <T> Result<T> ok(String msg, T data) {
		Result<T> r = ok(data);
		r.setMessage(msg);
		return r;
	}

	/**
	 * 消息弹窗通知
	 * @param msg 弹窗消息
	 * @param <T> 消息类型
	 * @return
	 */
	public static <T> Result<T> okMsg(String msg) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(BizConstants.CODE_200);
		r.setTimestamp(System.currentTimeMillis());
		r.setMessage(msg);
		return r;
	}

	/**
	 * 消息弹窗通知
	 * @param msg 弹窗消息
	 * @param debugResult 错误消息
	 * @param <T> 消息类型
	 * @return
	 */
	public static <T> Result<T> okMsg(String msg, T debugResult) {
		Result<T> r = okMsg(msg);
		r.setResult(debugResult);
		return r;
	}

	public static <T> Result<T> ok(@NonNull T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(BizConstants.CODE_200);
		r.setTimestamp(System.currentTimeMillis());
		r.setResult(data);
		return r;
	}

	public static <T> Result<T> error(String msg) {
		return error(BizConstants.CODE_500, msg);
	}

	public static <T> Result<T> error() {
		return error(BizConstants.CODE_500, "fail");
	}

	public static <T> Result<T> error(String code, String msg) {
		Result<T> r = new Result<T>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		r.setTimestamp(System.currentTimeMillis());
		return r;
	}

	public static <T> Result<T> error(AbstractBaseExceptionEnum exception) {
		return error(exception.getStatus(), exception.getMessage());
	}

	public static <T> Result<T> error(AbstractBaseExceptionEnum exception, Object... format) {
		return error(exception.getStatus(), String.format(exception.getMessage(), (Object[])format));
	}
}