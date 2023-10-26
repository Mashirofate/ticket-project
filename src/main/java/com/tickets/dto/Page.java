package com.tickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分页
 */
@Schema
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
    @Schema(hidden = true)
    private Integer total;


    /**
     * 分页记录
     */
    @Schema(hidden = true)
    private List<T> records;

}
