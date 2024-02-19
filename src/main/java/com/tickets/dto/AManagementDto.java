package com.tickets.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AManagementDto {
    private String eId;
    private String aId;
    private String eName;
    private String eNote;
    private int eEnable;
    private Date vmCreationtime;

}
