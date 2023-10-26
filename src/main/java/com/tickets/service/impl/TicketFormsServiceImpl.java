package com.tickets.service.impl;

import com.tickets.mapper.TicketFormsMapper;
import com.tickets.service.TicketFormsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class TicketFormsServiceImpl implements TicketFormsService {

    @Resource
    private TicketFormsMapper ticketFormsMapper;
    @Override
    public List<Map<String, Object>> getNumber(String aId) {
     return ticketFormsMapper.getNumbers(aId);
    }

    @Override
    public List<Map<String, Object>> getSum(String aId) {

        return ticketFormsMapper.getSum(aId);
    }

    @Override
    public List<Map<String, Object>> getcolumns(String aId) {
        return ticketFormsMapper.getcolumns(aId);
    }

    @Override
    public List<Map<String, Object>> getTotality(String aId) {
        return ticketFormsMapper.getTotality(aId);
    }

    @Override
    public List<Map<String, Object>> getheadcount(String aId) {
        return ticketFormsMapper.getheadcount(aId);
    }

    @Override
    public List<Map<String, Object>> getExportTotality(String aId) {
        return ticketFormsMapper.getExportTotality(aId);
    }

    @Override
    public List<Map<String, Object>> getEnTotality(String aId) {
        return ticketFormsMapper.getEnTotality(aId);
    }

    @Override
    public List<Map<String, Object>> getExportSum(String aId) {
        return ticketFormsMapper.getExportSum(aId);
    }

    @Override
    public List<Map<String, Object>> getEnSum(String aId) {
      return ticketFormsMapper.getEnSum(aId);
    }

    @Override
    public List<Map<String, Object>> getExitcount(String aId) {
        return  ticketFormsMapper.getExitcount(aId);
    }

    @Override
    public List<Map<String, Object>> getEncount(String aId) {
        return  ticketFormsMapper.getEncount(aId);
    }
}
