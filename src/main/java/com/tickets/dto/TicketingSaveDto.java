package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketingSaveDto {
    @JsonProperty(value = "tsVaId")
    private String tsVaId;
    @JsonProperty(value = "tsIdentiycard")
    private String tsIdentiycard;
    @JsonProperty(value = "tsRealName")
    private String tsRealName;
    @JsonProperty(value = "phone")
    private String phone;

}
