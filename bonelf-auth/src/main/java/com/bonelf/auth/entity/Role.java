package com.bonelf.auth.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
public class Role {
    private String code;
    private String name;
    private String description;
}
