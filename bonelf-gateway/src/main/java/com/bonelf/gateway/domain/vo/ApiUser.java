package com.bonelf.gateway.domain.vo;

import com.bonelf.gateway.domain.CommonUser;

/**
 * <p>
 * Api用户
 * </p>
 * @author bonelf
 * @since 2020/10/12 16:57
 */
public class ApiUser implements CommonUser {
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

	@Override
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
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
