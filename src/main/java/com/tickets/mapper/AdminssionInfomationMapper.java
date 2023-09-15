package com.tickets.mapper;

        import java.util.Date;
        import java.util.List;
        import java.util.Map;

public interface AdminssionInfomationMapper {

    /**
     * 获取过去5分钟入场人数
     * @return
     */
    Map<String,Object> selectCountLimitTime(Date date, String vaId);

    Map<String,Object> selectTypeCount(String vaId);

    List<Map<String, Object>> getCountLimitTimeenter(String vaId);

    List<Map<String, Object>> getCountLimitTimeout(String vaId);

    List<String> getoutlist(String vaId);
    List<Map<String, Object>> getSeatList(String vaId);
    List<Map<String, Object>> getGrandList(String vaId);
    List<Map<String, Object>> getGrandsList(String vaId);
    List<String> getenterlist(String vaId);
    List<String> getTIMElist(String vaId);
}

