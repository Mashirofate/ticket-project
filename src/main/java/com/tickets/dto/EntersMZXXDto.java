package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EntersMZXXDto {
    @JsonProperty(value = "aId")
    private String aId;
    @JsonProperty(value = "BIND_MZXX")
    private String BIND_MZXX;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getBIND_MZXX() {
        return BIND_MZXX;
    }

    public void setBIND_MZXX(String BIND_MZXX) {
        this.BIND_MZXX = BIND_MZXX;
    }
}
