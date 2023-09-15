package com.tickets.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页
 */
@ApiModel
@Data
public class Page<T> {
    /**
     * 当前页
     */
    private Long current;
    /**
     * 分页大小
     */
    private Long size;
    /**
     * 分页总数
     */
    @ApiModelProperty(hidden = true)
    private Integer total;


    /**
     * 分页记录
     */
    @ApiModelProperty(hidden = true)
    private List<T> records;

}
