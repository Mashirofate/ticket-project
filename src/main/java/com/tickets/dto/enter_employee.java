package com.tickets.dto;

public class enter_employee {
    private String eId;
    private String aId;
    private String tId;
    private String eName;
    private String ENTERID;
    private String CHECK_CERT_TYPE;
    private String CHECK_CERT_TYPE_NO;
    private String IDCODE;
    private String IDNAME;
    private byte[] FACEPHOTO;
    private byte[] HRFACEPHOTO;
    private String BIND_HJDXX;
    private String BIND_MZXX;
    private String ENTRY_TIME;
    private String  stamp;
    private String standby;

    public enter_employee() {
    }

    public enter_employee(String eId, String aId, String tId, String eName, String ENTERID, String CHECK_CERT_TYPE, String CHECK_CERT_TYPE_NO, String IDCODE, String IDNAME, byte[] FACEPHOTO, byte[] HRFACEPHOTO, String BIND_HJDXX, String BIND_MZXX, String ENTRY_TIME, String stamp, String standby) {
        this.eId = eId;
        this.aId = aId;
        this.tId = tId;
        this.eName = eName;
        this.ENTERID = ENTERID;
        this.CHECK_CERT_TYPE = CHECK_CERT_TYPE;
        this.CHECK_CERT_TYPE_NO = CHECK_CERT_TYPE_NO;
        this.IDCODE = IDCODE;
        this.IDNAME = IDNAME;
        this.FACEPHOTO = FACEPHOTO;
        this.HRFACEPHOTO = HRFACEPHOTO;
        this.BIND_HJDXX = BIND_HJDXX;
        this.BIND_MZXX = BIND_MZXX;
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

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getENTERID() {
        return ENTERID;
    }

    public void setENTERID(String ENTERID) {
        this.ENTERID = ENTERID;
    }

    public String getCHECK_CERT_TYPE() {
        return CHECK_CERT_TYPE;
    }

    public void setCHECK_CERT_TYPE(String CHECK_CERT_TYPE) {
        this.CHECK_CERT_TYPE = CHECK_CERT_TYPE;
    }

    public String getCHECK_CERT_TYPE_NO() {
        return CHECK_CERT_TYPE_NO;
    }

    public void setCHECK_CERT_TYPE_NO(String CHECK_CERT_TYPE_NO) {
        this.CHECK_CERT_TYPE_NO = CHECK_CERT_TYPE_NO;
    }

    public String getIDCODE() {
        return IDCODE;
    }

    public void setIDCODE(String IDCODE) {
        this.IDCODE = IDCODE;
    }

    public String getIDNAME() {
        return IDNAME;
    }

    public void setIDNAME(String IDNAME) {
        this.IDNAME = IDNAME;
    }

    public byte[] getFACEPHOTO() {
        return FACEPHOTO;
    }

    public void setFACEPHOTO(byte[] FACEPHOTO) {
        this.FACEPHOTO = FACEPHOTO;
    }

    public byte[] getHRFACEPHOTO() {
        return HRFACEPHOTO;
    }

    public void setHRFACEPHOTO(byte[] HRFACEPHOTO) {
        this.HRFACEPHOTO = HRFACEPHOTO;
    }

    public String getBIND_HJDXX() {
        return BIND_HJDXX;
    }

    public void setBIND_HJDXX(String BIND_HJDXX) {
        this.BIND_HJDXX = BIND_HJDXX;
    }

    public String getBIND_MZXX() {
        return BIND_MZXX;
    }

    public void setBIND_MZXX(String BIND_MZXX) {
        this.BIND_MZXX = BIND_MZXX;
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
