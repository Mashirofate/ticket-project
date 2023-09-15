package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeCharUserinfoAddDto {
    private String wcNick;
    private String wcPhone;
    private String code;
    @JsonProperty(value = "wAvatarUrl")
    private String wAvatarUrl;
}
