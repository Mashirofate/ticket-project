package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserSinInDto {
    @JsonProperty(value = "uId")
    private String uId;
    @JsonProperty(value = "uUser")
    private String uUser;
    @JsonProperty(value = "uPassword")
    private String uPassword;
    @JsonProperty(value = "uStartusing")
    private Integer uStartusing;
    @JsonProperty(value = "uAuthority")
    private String uAuthority;
}
