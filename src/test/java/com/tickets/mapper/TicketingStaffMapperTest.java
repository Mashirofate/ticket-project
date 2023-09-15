package com.tickets.mapper;

import com.tickets.dto.TicketingStaffSearchDto;
import com.tickets.entity.TicketingStaff;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketingStaffMapperTest {

    @Resource
    TicketingStaffMapper ticketingStaffMapper;

    @Test
    void selectByKeys() {
        System.out.println(ticketingStaffMapper.selectByKeys(new TicketingStaffSearchDto()));
    }

    @Test
    void insert() {
        ticketingStaffMapper.insert(new TicketingStaff());
    }
}