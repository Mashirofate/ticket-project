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
    private Long size = 10L;
    private String aId;
    private String aName;
    private String vName;
    private String eName;
    private String tIdentitycard;
    private String tQrcard;
    private String tId;
    private String dWorker;
    private Date eDate;
    private String temp;
    private String autonym;
    private String healthCode;
    private String eDate1;
    private String eDate2;
}
