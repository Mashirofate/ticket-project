package com.tickets.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Devices {
    private String dId;
    private String eName;
    private String dIp;
    private String aId;
    private Date dCreationtime;
}
