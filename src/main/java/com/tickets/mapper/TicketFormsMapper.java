package com.tickets.mapper;


import java.util.List;
import java.util.Map;

public interface TicketFormsMapper {
    List<Map<String, Object>> getNumbers(String aId);

    List<Map<String, Object>> getSum(String aId);

    List<Map<String, Object>> getcolumns(String aId);

    List<Map<String, Object>> getTotality(String aId);

    List<Map<String, Object>> getheadcount(String aId);

    List<Map<String, Object>> getExportTotality(String aId);

    List<Map<String, Object>> getEnTotality(String aId);

    List<Map<String, Object>> getExportSum(String aId);

    List<Map<String, Object>> getEnSum(String aId);

    List<Map<String, Object>> getExitcount(String aId);


    // getEncountsingle List<Map<String, Object>> getEncountsingle(String aId);
    //这个是每一天单个们的出场数量
    List<Map<String, Object>> getEncount(String aId);
}
