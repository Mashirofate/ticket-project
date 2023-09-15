package com.tickets.service;

import com.tickets.dto.EntersSearchDto;
import com.tickets.dto.Page;

public interface EntersService {


    Page getByKeys( EntersSearchDto entersSearchDto);

    boolean delById(String eid);

    com.tickets.dao.Page selectNewData(String date,String clientId, int currPage, int pageSize);
}
