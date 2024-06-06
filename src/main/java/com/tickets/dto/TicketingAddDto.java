package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketingAddDto {
    @JsonProperty(value = "tsIccard")
    private String tsIccard;
    @JsonProperty(value = "FLOOR_NAME")
    private String FLOOR_NAME;
    @JsonProperty(value = "BIND_CARD")
    private String BIND_CARD;
    @JsonProperty(value = "BIND_MZXX")
    private String BIND_MZXX;
    @JsonProperty(value = "BIND_PHONE")
    private String BIND_PHONE;
    @JsonProperty(value = "QR_CODE")
    private String QR_CODE;
    @JsonProperty(value = "BIND_NAME")
    private String BIND_NAME;
    @JsonProperty(value = "SRAT_ROW")
    private String SRAT_ROW;
    @JsonProperty(value = "SRAT_COL")
    private String SRAT_COL;
    @JsonProperty(value = "SRAND_NAME")
    private String SRAND_NAME;
    @JsonProperty(value = "aId")
    private String aId;
    @JsonProperty(value = "tId")
    private String tId;




}
