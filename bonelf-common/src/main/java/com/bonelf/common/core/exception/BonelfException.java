package com.bonelf.common.core.exception;

import com.bonelf.common.constant.BizConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 服务异常
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:34
 */
@Getter
@Setter
@AllArgsConstructor
public class BonelfException extends RuntimeException {
	private String code;
	private String errorMessage;


	public BonelfException(Exception e) {
		this.code = BizConstants.CODE_500;
		this.errorMessage = e.getMessage();
	}

	public BonelfException(String messageFor500) {
		this.code = BizConstants.CODE_500;
		this.errorMessage = messageFor500;
	}

	public BonelfException(AbstractBaseExceptionEnum exception) {
		super(exception.getMessage());
		this.code = exception.getStatus();
		this.errorMessage = exception.getMessage();
	}

	public BonelfException(AbstractBaseExceptionEnum exception, Object... format) {
		super(String.format(exception.getMessage(), (Object[])format));
		this.code = exception.getStatus();
		this.errorMessage = String.format(exception.getMessage(), (Object[])format);
	}
}
