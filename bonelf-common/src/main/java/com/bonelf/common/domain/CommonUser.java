package com.bonelf.common.domain;

/**
 * 用户
 */
public interface CommonUser {

	/**
	 * 用户id
	 * @return
	 */
	Long getUserId();

	/**
	 * 冻结状态
	 * @see com.bonelf.common.core.constant.enums.FreezeEnum
	 * @return
	 */
	Integer getStatus();

	/**
	 * 可以使 用户名、账号、邮箱等  唯一的用户凭据
	 * @return
	 */
	String getUsername();
}