package com.tickets.dto;

import lombok.Data;

@Data
public class AisleSearchDto {
    /**
     * 当前页
     */
    private Long current = 1L;
    /**
     * 分页大小
     */
    private Long size = 10L;

    private String aId;
    private String aName;

}
