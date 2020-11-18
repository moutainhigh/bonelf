package com.bonelf.auth.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource {
    private String code;
    private String name;
    private String type;
    private String url;
    private String method;
    private String description;
}
