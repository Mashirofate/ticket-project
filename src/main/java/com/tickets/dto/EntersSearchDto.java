package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class EntersSearchDto {
    /**
     * 当前页
     */
    private Long current = 1L;
    /**
     * 分页大小
     */
    private Long size = 20L;
    private String eDate1;
    private String eDate2;


    private String eId;
    private String aId;
    private String aName;
    private String vName;
    private String eName;
    private String tId;
    private String CHECK_CERT_TYPE;
    private String BIND_NAME;
    private String BIND_CARD;
    private String BIND_GENDER;
    private String BIND_MZXX;
    private String BIND_HJDXX;
    private String QR_CODE;
    private String ENTRY_TIME;
    private String Device_CODE;
    private String Device_NAME;
    private String stamp;
    private String standby;
    private String SRAND_NAME;
    private String SRAT_ROW;
    private String SRAT_COL;
}
