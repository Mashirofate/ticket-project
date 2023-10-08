package com.tickets.service;

import java.util.List;
import java.util.Map;

public interface TurnoutService {
    Map<String, Object> getTypeCount(String vaId);

    List<Map<String, Object>> getEachexportCount(String vaId);

    List<String> getImageByActivityId(String vaId);

    Map<String, Object> getintraFieldCount(String vaId);
    Map<String, Object> getintworkCount(String vaId);


}
