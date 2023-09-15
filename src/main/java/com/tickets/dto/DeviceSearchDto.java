package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceSearchDto {
    /**
     * 当前页
     */
    private Long current = 1L;
    /**
     * 分页大小
     */
    private Long size = 10L;

    private String dId;
    private String eName;
    private String dIp;
    private String aId;
    private Date dCreationtime;

}
