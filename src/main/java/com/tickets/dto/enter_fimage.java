package com.tickets.dto;

public class enter_fimage {
    private String eId;
    private String aId;
    private byte[] BIND_PHOTO;
    private byte[] BIND_HRFACE_PHOTO;
    private byte[] BIND_CARD_PHOTO;
    private String stamp;
    private String standby;

    public enter_fimage() {
    }

    public enter_fimage(String eId, String aId, byte[] BIND_PHOTO, byte[] BIND_HRFACE_PHOTO, byte[] BIND_CARD_PHOTO, String stamp, String standby) {
        this.eId = eId;
        this.aId = aId;
        this.BIND_PHOTO = BIND_PHOTO;
        this.BIND_HRFACE_PHOTO = BIND_HRFACE_PHOTO;
        this.BIND_CARD_PHOTO = BIND_CARD_PHOTO;
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

    public byte[] getBIND_PHOTO() {
        return BIND_PHOTO;
    }

    public void setBIND_PHOTO(byte[] BIND_PHOTO) {
        this.BIND_PHOTO = BIND_PHOTO;
    }

    public byte[] getBIND_HRFACE_PHOTO() {
        return BIND_HRFACE_PHOTO;
    }

    public void setBIND_HRFACE_PHOTO(byte[] BIND_HRFACE_PHOTO) {
        this.BIND_HRFACE_PHOTO = BIND_HRFACE_PHOTO;
    }

    public byte[] getBIND_CARD_PHOTO() {
        return BIND_CARD_PHOTO;
    }

    public void setBIND_CARD_PHOTO(byte[] BIND_CARD_PHOTO) {
        this.BIND_CARD_PHOTO = BIND_CARD_PHOTO;
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
