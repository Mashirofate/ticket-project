package com.tickets.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ClearCardSearchDto {
    /**
     * 当前页
     */
    private Long current = 1L;
    /**
     * 分页大小
     */
    private Long size = 10L;

    private String fid;
    private String fCertificate;
    private String fComment;

}
