package com.tickets.entity;

import lombok.Data;

import java.util.Date;

@Data
public class EntranceManagement {
    private String emId;
    private String emName;
    private Integer emEnable;
    private String emNote;
    private String aId;
    private Date emCreationTime;
}
