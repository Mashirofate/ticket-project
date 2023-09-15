package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TradeshowPartDto {
    @JsonProperty(value = "tQrcard")
    private String tQrcard ;
    @JsonProperty(value = "tType")
    private String tType  ;
    @JsonProperty(value = "aId")
    private String aId  ;
}
