package com.bonelf.testservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * example:{"cmdId":2,"message":"你好","data":{"hello":"你好","world","0"}}
 */
@Data
public class TestConverterDTO {
	private Long var1;
	private long var2;
	private BigDecimal var3;
	private String var4;
	private LocalDate var5;
	private LocalDateTime var6;
	private LocalTime var7;
	private Date var8;
	private Map<String, Object> var9;
	private Boolean var10;
	private List<TestDTO> var11;
	private Set<TestDTO> var12;
	private double var13;
	private Double var14;
}
