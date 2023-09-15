package com.tickets.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EntranceManagementSearchDto {
    /**
     * 当前页
     */
    private Long current = 1L;
    /**
     * 分页大小
     */
    private Long size = 10L;

    private String emName;
    private Integer emEnable;
    private String vmName;
}
