package com.tickets.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExhibitionDto {

    /**
     * 实时入场人数
     */
    private Integer realTimePeopleCount;

    /**
     * 票务总数
     */
    private Integer ticketAllCount;

    /**
     * 票务实时入场人数
     */
    private Integer realTimeTicketCount;

    /**
     * 实名入场人数
     */
    private Integer realNameCount;

    /**
     * 未实名入场人数
     */
    private Integer noRealNameCount;


    /**
     * 实时入口人数数量
     */
    private List<Map<String, Integer>> realTimeEntranceData;


    /**
     * 实时入场人数变化
     */
    private List<Map<String, Object>> realTimePeopleData;
}
