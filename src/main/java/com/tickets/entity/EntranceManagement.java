package com.tickets.entity;

import com.alibaba.druid.sql.visitor.functions.Char;
import lombok.Data;

import java.util.Date;

@Data
public class EntranceManagement {
    private String emId;
    private String emName;
    private Integer emEnable;
    private String emNote;
    private String emVmId;
    private Date emCreationTime;
}
