package com.tickets.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tradeshow
 * @author
 */
@Data
public class Tradeshow implements Serializable {
    private String tid;

    private String tname;

    private String tqrcard;

    private String tidentitycard;

    private String tphone;

    private String tcompany;

    private String ttype;

    private Date tdata;

    private String telse;

    private String aid;

    private static final long serialVersionUID = 1L;
}