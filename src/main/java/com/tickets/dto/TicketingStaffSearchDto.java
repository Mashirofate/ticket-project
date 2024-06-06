package com.tickets.dto;

import lombok.Data;

@Data
public class TicketingStaffSearchDto {

    /**
     * 当前页
     */
    private Long current = 1L;
    /**
     * 分页大小
     */
    private Long size = 20L;

    private String tId;
    private String aName;
    private String SRAND_NAME;
    private String SRAT_ROW;
    private String SRAT_COL;
    private String FLOOR_NAME;
    private String BIND_CARD;
    private String BIND_NAME;
    private String QR_CODE;
    private String IC_CODE;
    private String BIND_PHONE;
    private String BIND_MZXX;
}
