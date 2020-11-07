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

@ApiModel(value="com-bonelf-support-domain-entity-SysDictItem")
@Data
@NoArgsConstructor
@TableName(value = "sys_dict_item")
public class DictItem {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="")
    private String id;

    /**
     * 字典id
     */
    @TableField(value = "dict_id")
    @ApiModelProperty(value="字典id")
    private String dictId;

    /**
     * 字典项文本
     */
    @TableField(value = "item_text")
    @ApiModelProperty(value="字典项文本")
    private String itemText;

    /**
     * 字典项值
     */
    @TableField(value = "item_value")
    @ApiModelProperty(value="字典项值")
    private String itemValue;

    /**
     * 描述
     */
    @TableField(value = "description")
    @ApiModelProperty(value="描述")
    private String description;

    /**
     * 排序
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value="排序")
    private Integer sortOrder;

    /**
     * 状态（0启用 1不启用）
     */
    @TableField(value = "item_status")
    @ApiModelProperty(value="状态（0启用 1不启用）")
    private Integer itemStatus;

    @TableField(value = "create_time")
    @ApiModelProperty(value="")
    private Date createTime;

    @TableField(value = "update_time")
    @ApiModelProperty(value="")
    private Date updateTime;
}