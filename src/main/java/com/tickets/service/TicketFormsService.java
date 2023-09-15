package com.tickets.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TicketFormsService {
    List<Map<String, Object>> getNumber(String aId);

    List<Map<String, Object>> getSum(String aId);

    List<Map<String, Object>> getcolumns(String aId);

    List<Map<String, Object>> getTotality(String aId);

    List<Map<String, Object>> getheadcount(String aId);

    List<Map<String, Object>> getExportTotality(String aId);

    List<Map<String, Object>> getEnTotality(String aId);

    List<Map<String, Object>> getExportSum(String aId);

    List<Map<String, Object>> getEnSum(String aId);

    List<Map<String, Object>> getExitcount(String aId);

    List<Map<String, Object>> getEncount(String aId);
}
