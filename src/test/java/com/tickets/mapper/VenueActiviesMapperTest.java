package com.tickets.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VenueActiviesMapperTest {

    @Resource
    private VenueActiviesMapper venueActiviesMapper;

    @Test
    void selectByEnable() {

        System.out.println(venueActiviesMapper.selectByEnable('1'));
    }
}