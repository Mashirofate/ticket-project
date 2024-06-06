package com.tickets.service;

import java.util.List;
import java.util.Map;

public interface TurnoutService {
    Map<String, Object> getTypeCount(String vaId,int i);

    List<Map<String, Object>> getEachexportCount(String vaId,int i);
    List getOUTENCount(String vaId,int i,int j);
    List<String> getImageByActivityId(String vaId);

    Map<String, Object> getintraFieldCount(String vaId);
    Map<String, Object> getVotesCount(String vaId);
    Map<String, Object> getintworkCount(String vaId);
    List getTIMElist(String vaId,int i,int j);


}
