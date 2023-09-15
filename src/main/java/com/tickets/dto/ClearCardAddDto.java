package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ClearCardAddDto {
    @JsonProperty(value = "fId")
    private String fId;
    @JsonProperty(value = "fCertificate")
    private String fCertificate;
    @JsonProperty(value = "fComment")
    private String fComment;

}
