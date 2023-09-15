package com.tickets.entity;

import lombok.Data;

@Data
public class User {
    private String uId;
    private String uUser;
    private String uPassword;
    private Integer uStartusing;
    private String uAuthority;
}

