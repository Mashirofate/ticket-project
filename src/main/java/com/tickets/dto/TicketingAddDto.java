package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketingAddDto {
    @JsonProperty(value = "tsIccard")
    private String tsIccard;
    @JsonProperty(value = "tsGrandstand")
    private String tsGrandstand;
    @JsonProperty(value = "tsIdentiycard")
    private String tsIdentiycard;
    @JsonProperty(value = "tsNote")
    private String tsNote;
    @JsonProperty(value = "phone")
    private String phone;
    @JsonProperty(value = "tsQrcard")
    private String tsQrcard;
    @JsonProperty(value = "tsRealName")
    private String tsRealName;
    @JsonProperty(value = "tsRownumber")
    private String tsRownumber;
    @JsonProperty(value = "tsSeat")
    private String tsSeat;
    @JsonProperty(value = "tsSeatingArea")
    private String tsSeatingArea;
    @JsonProperty(value = "tsVaId")
    private String tsVaId;
    @JsonProperty(value = "tsId")
    private String tsId;

}
