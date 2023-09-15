package com.tickets.entity;

import lombok.Data;

@Data
public class WecharUser {
    private String wcId;
    private String wcOpenid;
    private String wcSessionKey;
    private String wcNick;
    private String wcPhone;
    private String wAvatarUrl;
}
