package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class VenueManagementAddDto {
    @JsonProperty(value = "vmName")
    private String vmName;
    @JsonProperty(value = "vmEnable")
    private int vmEnable;
    @JsonProperty(value = "vmNote")
    private String vmNote;
    @JsonProperty(value = "vmId")
    private String vmId;
    @JsonProperty(value = "vmCreationtime")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date vmCreationtime;

}
