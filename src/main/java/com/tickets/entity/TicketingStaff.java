package com.tickets.entity;


import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class TicketingStaff {
    private String tsId;
    private String tsVaId;
    /**
     * 身份证
     */
    private String tsIdentiycard;
    /**
     * IC卡
     */
    private String tsIccard;
    /**
     * 二维码
     */
    private String tsQrcard;
    private String tsSeatingArea;
    private String tsRownumber;
    private String tsSeat;
    private String tsGrandstand;
    private String tsRealName;
    private String tsNote;
    private String tWId;
    private String phone;
}
