package com.tickets.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema
@Data
public class Authority {
    @Schema(hidden = true)
    private String aId;
    private String aName;
    private String aNote;
    private String aGrade;
}
