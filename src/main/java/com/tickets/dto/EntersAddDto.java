package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EntersAddDto {

    @JsonProperty(value = "eId")
    private String eId;
    @JsonProperty(value = "aId")
    private String aId;
    @JsonProperty(value = "aName")
    private String aName;
    @JsonProperty(value = "vName")
    private String vName;
    @JsonProperty(value = "eName")
    private String eName;
    @JsonProperty(value = "tIdentitycard")
    private String tIdentitycard;
    @JsonProperty(value = "tQrcard")
    private String tQrcard;
    @JsonProperty(value = "eDate")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date eDate;
    @JsonProperty(value = "tId")
    private String tId;
    @JsonProperty(value = "dWorker")
    private String dWorker;
    @JsonProperty(value = "temp")
    private String temp;
    @JsonProperty(value = "fImage")
    private String fImage;
    @JsonProperty(value = "fFeature")
    private String fFeature;
    @JsonProperty(value = "autonym")
    private String autonym;
    @JsonProperty(value = "healthCode")
    private String healthCode;
    @JsonProperty(value = "clientId")
    private String clientId;
}
