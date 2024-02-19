package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class VenueActivieAddDto {
    @JsonProperty(value = "aaId")
    private String aaId;
    @JsonProperty(value = "aId")
    private String aId;
    @JsonProperty(value = "aName")
    private String aName;
    @JsonProperty(value = "vName")
    private String vName;
    @JsonProperty(value = "aEnable")
    private Integer aEnable;
    @JsonProperty(value = "aNote")
    private String aNote;
    @JsonProperty(value = "aTicketnumber")
    private Integer aTicketnumber;
    @JsonProperty(value = "aEmployeenumber")
    private Integer aEmployeenumber;
    @JsonProperty(value = "aImage")
    private String aImage;
    @JsonProperty(value = "aType")
    private String aType;
    @JsonProperty(value = "startTime")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonProperty(value = "endTime")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date endTime;
    @JsonProperty(value = "updateTime")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Date aCreationtime;

}
