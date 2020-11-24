package com.bonelf.auth.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Role implements Serializable {
	public static final Long serialVersionUID = -1L;
	private String code;
	private String name;
	private String description;
}
