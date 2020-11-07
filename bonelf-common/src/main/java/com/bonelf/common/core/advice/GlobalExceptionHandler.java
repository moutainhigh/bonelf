package com.bonelf.common.core.advice;

import cn.hutool.core.util.StrUtil;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.BizExceptionEnum;
import com.bonelf.common.domain.Result;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

	/**
	 * ServiceException
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(value = BonelfException.class)
	public Result<?> errorHandler(BonelfException e) {
		//便于调试
		e.printStackTrace();
		return Result.error(e.getCode(), e.getErrorMessage());
	}

	/**
	 * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常BindException
	 * 处理@RequestParam上validate失败后抛出的异常是ConstraintViolationException
	 * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException
	 * 拦截自定义参数验证失败异常
	 */

	@ExceptionHandler({BindException.class,
			ConstraintViolationException.class,
			MethodArgumentNotValidException.class,
			MissingServletRequestParameterException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Result<?> handleApiConstraintViolationException(Exception e, HttpServletRequest request) {
		String message = null;
		if (e instanceof BindException) {
			message = ((BindException) e).getBindingResult().getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.joining(StrUtil.COMMA));
		}
		if (e instanceof MethodArgumentNotValidException) {
			// getAllErrors OR getFieldErrors
			message = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.joining(StrUtil.COMMA));
		}
		if (e instanceof ConstraintViolationException) {
			message = ((ConstraintViolationException) e).getConstraintViolations().stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining(StrUtil.COMMA));
		}
		if (e instanceof MissingServletRequestParameterException) {
			MissingServletRequestParameterException exp = ((MissingServletRequestParameterException)e);
			message = "未传" + exp.getParameterName() + "(" + exp.getParameterType() + ")";
		}
		return Result.error(BizExceptionEnum.REQUEST_INVALIDATE, message);
	}

	/**
	 * json parse error
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public Result<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
		//便于调试
		e.printStackTrace();
		return Result.error(BizExceptionEnum.JSON_SERIALIZE_EXCEPTION);
	}

	@ResponseBody
	@ExceptionHandler(value = MaxUploadSizeExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result<?> handleUploadException(MaxUploadSizeExceededException exp) {
		//便于调试
		exp.printStackTrace();
		if (exp.getCause() instanceof IllegalStateException) {
			IllegalStateException expIll = (IllegalStateException)exp.getCause();
			if (expIll.getCause() instanceof FileSizeLimitExceededException) {
				FileSizeLimitExceededException expFile = (FileSizeLimitExceededException)expIll.getCause();
				String message = "文件超出限制：最大" + BigDecimal.valueOf(expFile.getPermittedSize() / 1024D / 1024D).setScale(2, RoundingMode.HALF_UP) + "MB";
				return Result.error(BizExceptionEnum.REQUEST_INVALIDATE, message);
			}
		}
		if (exp.getMaxUploadSize() != -1) {
			String message = "文件超出限制：最大" + BigDecimal.valueOf(exp.getMaxUploadSize() / 1024D / 1024D).setScale(2, RoundingMode.HALF_UP) + "MB";
			return Result.error(BizExceptionEnum.REQUEST_INVALIDATE, message);
		}
		return Result.error(BizExceptionEnum.REQUEST_INVALIDATE, "文件超出限制");
	}

	/**
	 * 全局异常捕捉处理
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public Result<?> errorHandler(Exception e) {
		//便于调试
		e.printStackTrace();
		//return Result.error(BizExceptionEnum.SERVER_ERROR.getCode(), BizExceptionEnum.SERVER_ERROR.getMessage());
		return Result.error(BizExceptionEnum.SERVER_ERROR, e.getMessage());
	}

}
