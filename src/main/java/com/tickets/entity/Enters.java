package com.tickets.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
public class Enters {
    private String eId;
    private String edId;
    private String etId;
    private Date eDate;
    private Date Information;
    private byte[] informationImage;
}
