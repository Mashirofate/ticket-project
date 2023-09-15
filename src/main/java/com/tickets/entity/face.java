package com.tickets.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class face {
    private String fId;
    private byte[] fImage;
    private byte[] fFeature;
    private String ftId;
}
