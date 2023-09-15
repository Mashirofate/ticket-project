package com.tickets.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;
@ApiModel
@Data
public class Authority {
    @ApiModelProperty(hidden = true)
    private String aId;
    private String aName;
    private String aNote;
    private String aGrade;
}
