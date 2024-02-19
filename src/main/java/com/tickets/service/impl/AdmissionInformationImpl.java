package com.tickets.service.impl;

import com.tickets.mapper.AdmissionInformatization;
import com.tickets.service.AdmissionInformationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdmissionInformationImpl implements AdmissionInformationService {

    @Resource
    private AdmissionInformatization admissionInformatization;

    @Override
    public Map<String,Object> getCountLimitTime(Date date, String vaId) {
        return admissionInformatization.selectCountLimitTime(date,vaId);
    }

    @Override
    public Map<String, Object> getTypeCount(String vaId) {
        return admissionInformatization.selectTypeCount(vaId);
    }

    @Override
    public List<Map<String, Object>> getCountLimitTimeenter(String vaId) {
        return admissionInformatization.getCountLimitTimeenter(vaId);
    }

    @Override
    public List<Map<String, Object>> getCountLimitTimeout(String vaId) {
        return admissionInformatization.getCountLimitTimeout(vaId) ;
    }

    @Override
    public List getoutlist(String vaId) {
        return admissionInformatization.getoutlist(vaId) ;
    }

    public List<Map<String, Object>> getSeatList(String vaId) {
        return admissionInformatization.getSeatList(vaId) ;
    }
    public List<Map<String, Object>> getGrandList(String vaId) {
        return admissionInformatization.getGrandList(vaId) ;
    }
    public List<Map<String, Object>> getGrandsList(String vaId) {
        return admissionInformatization.getGrandsList(vaId) ;
    }
    public List getenterlist(String vaId) {
        return admissionInformatization.getenterlist(vaId) ;
    }

    public List getTIMElist(String vaId) {
        return admissionInformatization.getTIMElist(vaId) ;
    }

    public Map<String, Object> getrealnameCount(String vaId) {
        return  admissionInformatization.selectrealnameCount(vaId);
    }


    public Map<String, Object> getNorealnameCount(String vaId) {
        return admissionInformatization.selectNorealnameCount(vaId);
    }
}
