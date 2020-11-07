package com.bonelf.support.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value="com-bonelf-support-domain-entity-SysDict")
@Data
@NoArgsConstructor
@TableName(value = "sys_dict")
public class Dict {
    @TableId(value = "dict_id", type = IdType.INPUT)
    @ApiModelProperty(value="")
    private String dictId;

    /**
     * 字典名称
     */
    @TableField(value = "dict_name")
    @ApiModelProperty(value="字典名称")
    private String dictName;

    /**
     * 字典编码
     */
    @TableField(value = "dict_code")
    @ApiModelProperty(value="字典编码")
    private String dictCode;

    /**
     * 描述
     */
    @TableField(value = "description")
    @ApiModelProperty(value="描述")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
}