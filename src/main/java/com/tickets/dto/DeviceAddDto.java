package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceAddDto {
    @JsonProperty(value = "dId")
    private String dId;
    @JsonProperty(value = "eName")
    private String eName;
    @JsonProperty(value = "dIp")
    private String dIp;
    @JsonProperty(value = "aId")
    private String aId;

}
