package com.tickets.dto;

import java.util.Date;

public class EntertfDto {

    private String eId;
    private String aId;
    private String eName;
    private String tId;
    private String BIND_NAME;
    private String BIND_CARD;
    private String QR_CODE;
    private String ENTRY_TIME;
    private String stamp;
    private String standby;

    public EntertfDto() {
    }

    public EntertfDto(String eId, String aId, String eName, String tId, String BIND_NAME, String BIND_CARD, String QR_CODE, String ENTRY_TIME, String stamp, String standby) {
        this.eId = eId;
        this.aId = aId;
        this.eName = eName;
        this.tId = tId;
        this.BIND_NAME = BIND_NAME;
        this.BIND_CARD = BIND_CARD;
        this.QR_CODE = QR_CODE;
        this.ENTRY_TIME = ENTRY_TIME;
        this.stamp = stamp;
        this.standby = standby;
    }

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getBIND_NAME() {
        return BIND_NAME;
    }

    public void setBIND_NAME(String BIND_NAME) {
        this.BIND_NAME = BIND_NAME;
    }

    public String getBIND_CARD() {
        return BIND_CARD;
    }

    public void setBIND_CARD(String BIND_CARD) {
        this.BIND_CARD = BIND_CARD;
    }

    public String getQR_CODE() {
        return QR_CODE;
    }

    public void setQR_CODE(String QR_CODE) {
        this.QR_CODE = QR_CODE;
    }

    public String getENTRY_TIME() {
        return ENTRY_TIME;
    }

    public void setENTRY_TIME(String ENTRY_TIME) {
        this.ENTRY_TIME = ENTRY_TIME;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getStandby() {
        return standby;
    }

    public void setStandby(String standby) {
        this.standby = standby;
    }
}
