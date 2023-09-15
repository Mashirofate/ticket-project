package com.tickets.service;

import com.tickets.dto.*;

public interface ClearCardService {


    Page getByKeys(ClearCardSearchDto clearCardSearchDto);

    boolean delById(String fid);

    boolean save(ClearCardAddDto clearCardAddDto);

    boolean update(ClearCardAddDto clearCardAddDto);
}
