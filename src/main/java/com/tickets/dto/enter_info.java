package com.tickets.dto;

public class enter_info {
    private String eId;
    private String CHECK_CERT_TYPE;
    private String BIND_GENDER;
    private String BIND_MZXX;
    private String BIND_HJDXX;
    private String Device_CODE;
    private String Device_NAME;
    private String stamp;
    private String standby;

    public enter_info() {
    }

    public enter_info(String eId, String CHECK_CERT_TYPE, String BIND_GENDER, String BIND_MZXX, String BIND_HJDXX, String device_CODE, String device_NAME, String stamp, String standby) {
        this.eId = eId;
        this.CHECK_CERT_TYPE = CHECK_CERT_TYPE;
        this.BIND_GENDER = BIND_GENDER;
        this.BIND_MZXX = BIND_MZXX;
        this.BIND_HJDXX = BIND_HJDXX;
        Device_CODE = device_CODE;
        Device_NAME = device_NAME;
        this.stamp = stamp;
        this.standby = standby;
    }

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public String getCHECK_CERT_TYPE() {
        return CHECK_CERT_TYPE;
    }

    public void setCHECK_CERT_TYPE(String CHECK_CERT_TYPE) {
        this.CHECK_CERT_TYPE = CHECK_CERT_TYPE;
    }

    public String getBIND_GENDER() {
        return BIND_GENDER;
    }

    public void setBIND_GENDER(String BIND_GENDER) {
        this.BIND_GENDER = BIND_GENDER;
    }

    public String getBIND_MZXX() {
        return BIND_MZXX;
    }

    public void setBIND_MZXX(String BIND_MZXX) {
        this.BIND_MZXX = BIND_MZXX;
    }

    public String getBIND_HJDXX() {
        return BIND_HJDXX;
    }

    public void setBIND_HJDXX(String BIND_HJDXX) {
        this.BIND_HJDXX = BIND_HJDXX;
    }

    public String getDevice_CODE() {
        return Device_CODE;
    }

    public void setDevice_CODE(String device_CODE) {
        Device_CODE = device_CODE;
    }

    public String getDevice_NAME() {
        return Device_NAME;
    }

    public void setDevice_NAME(String device_NAME) {
        Device_NAME = device_NAME;
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
