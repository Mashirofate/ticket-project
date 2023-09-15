package com.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FaceSaveDto {
    /**
     * 当前页
     */
    private Long current = 1L;
    /**
     * 分页大小
     */
    private Long size = 10L;

    private String similar;
    @JsonProperty(value = "Image")
    private String Image;
}
