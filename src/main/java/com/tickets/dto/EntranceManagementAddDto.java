package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EntranceManagementAddDto {
    @JsonProperty(value = "emName")
    private String emName;
    @JsonProperty(value = "emEnable")
    private Integer emEnable;
    @JsonProperty(value = "emNote")
    private String emNote;
    @JsonProperty(value = "aId")
    private String aId;
    @JsonProperty(value = "emId")
    private String emId;
    @JsonProperty(value = "aName")
    private String  aName;
    @JsonProperty(value = "eCreationtime")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date eCreationtime;

}
