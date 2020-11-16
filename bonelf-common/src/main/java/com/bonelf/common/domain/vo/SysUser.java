package com.bonelf.common.domain.vo;

import com.bonelf.common.domain.CommonUser;

/**
 * <p>
 * 系统用户
 * </p>
 * @author bonelf
 * @since 2020/10/12 16:57
 */
public class SysUser implements CommonUser {

	/**
	 * phone
	 */
	private String phone;
	/**
	 * userId
	 */
	private Long userId;
	/**
	 * status
	 */
	private Integer status;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String getUsername() {
		return phone;
	}
}
