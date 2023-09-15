package com.tickets.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * aisle
 * @author 
 */
@Data
public class Aisle implements Serializable {
    @JsonProperty(value = "aId")
    private String aId;
    @JsonProperty(value = "aName")
    private String aName;
}