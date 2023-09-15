package com.tickets.mapper;

import com.tickets.dto.VenueMgDto;
import com.tickets.entity.VenueManagement;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VenueManagementMapperTest {

    @Resource
    private VenueManagementMapper venueManagementMapper;

    @Test
    void insert() {

        VenueManagement venueManagement = new VenueManagement();
        venueManagement.setVmName("一盒糖");
        venueManagement.setVmEnable('1');
        venueManagement.setVmNote("测试");
        venueManagement.setVmCreationtime(new Date());
        System.out.println(venueManagementMapper.insert(venueManagement));
    }

    @Test
    void selectByKeys() {
        System.out.println(venueManagementMapper.selectByKeys(new VenueMgDto()));
    }

    @Test
    void selectCountByKeys() {
        System.out.println(venueManagementMapper.selectCountByKeys(new VenueMgDto()));
    }
}