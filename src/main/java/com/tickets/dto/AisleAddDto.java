package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AisleAddDto {
    @JsonProperty(value = "aId")
    private String aId;
    @JsonProperty(value = "aName")
    private String aName;

}
