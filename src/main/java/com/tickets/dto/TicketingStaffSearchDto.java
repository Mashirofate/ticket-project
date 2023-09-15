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
    private Long size = 10L;

    private String tsId;
    private String tsVaName;
    private String tsIdentiycard;
    private String tsQrcard;
    private String tsIccard;
    private String tsSeatingArea;
    private String tsRownumber;
    private String tsSeat;
    private String tsRealName;
    private String tsPhone;
    private String tsNote;
    private String tsGrandstand;
}
