package com.tickets.service.impl;

import com.tickets.dto.*;
import com.tickets.entity.EntranceManagement;
import com.tickets.entity.TicketingStaff;
import com.tickets.entity.VenueManagement;
import com.tickets.mapper.TicketingStaffMapper;
import com.tickets.service.TicketingStaffService;
import com.tickets.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class TicketingStaffServiceImpl implements TicketingStaffService {

    @Resource
    private TicketingStaffMapper ticketingStaffMapper;


    @Override
    public Page getByKeys(TicketingStaffSearchDto ticketingStaffSearchDto) {
        Page<Map<String, Object>> page = new Page<>();
        BeanUtils.copyProperties(ticketingStaffSearchDto, page);
        int count = ticketingStaffMapper.selectCountByKeys(ticketingStaffSearchDto);
        page.setTotal(count);
        if (count != 0) {
            page.setRecords(ticketingStaffMapper.selectByKeys(ticketingStaffSearchDto));
        }
        return page;
    }

    @Override
    public List<Map<String,Object>> getByKeys(String scanCode,String aId) {
        return ticketingStaffMapper.selectCountByscanCode(scanCode,aId);
    }

    @Override
    public List<Map<String, Object>>  getByBuys(BuyticketDto buyticketDto) {
        List<Map<String, Object>> list =ticketingStaffMapper.getByBuys(buyticketDto);
      return list;
    }

    @SneakyThrows
    @Override
    public void exportExcel(HttpServletResponse response) {
        //表头数据
        String[] header = {"二维码信息", "座位区域", "座位排号", "座位号", "看台信息", "身份证号", "区域通道信息"};

        //数据内容
        String[] student1 = {"145613", "4", "14", "23", "内场看台","123456789X","内场通道"};
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet;
        sheet = workbook.createSheet("票务导入信息表");

        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(20);

        //创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        //遍历添加表头(下面模拟遍历学生，也是同样的操作过程)
        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);
            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
        }

        //模拟遍历结果集，把内容加入表格
        //模拟遍历第一个学生
        HSSFRow row1 = sheet.createRow(1);
        for (int i = 0; i < student1.length; i++) {
            HSSFCell cell = row1.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(student1[i]);
            cell.setCellValue(text);
        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称，此例中名为student.xls
        response.setHeader("Content-disposition", "attachment;filename=ticketingStaff.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }

    @Resource
    private HttpServletRequest httpServletRequest;

    @Override
    public void saveBath(List<Map<String, Object>> list, String aId) {

        List<Map<String, Object>> success = new ArrayList<>();
        List<Map<String, Object>> error = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String tsQrcard = (String) map.get("二维码信息");
            String tsSeatingarea = (String) map.get("座位区域");
            String tsRownumber = (String) map.get("座位排号");
            String tsSeat = (String) map.get("座位号");
            String tsGrandstand = (String) map.get("看台信息");
            String tsIdentitycard = (String) map.get("身份证号");
            String tsIccard = (String) map.get("区域通道信息");
            TicketingStaff ticketingStaff = new TicketingStaff();

            String header = httpServletRequest.getHeader("Token");
            Claims claims = JwtUtil.checkJWT(header);
            String id = (String) claims.get("id");
            ticketingStaff.setTsVaId(id);
            ticketingStaff.setTsQrcard(tsQrcard)
                    .setTsSeatingArea(tsSeatingarea)
                    .setTsRownumber(tsRownumber)
                    .setTsSeat(tsSeat)
                    .setTsGrandstand(tsGrandstand)
                    .setTsIdentiycard(tsIdentitycard)
                    .setTsIccard(tsIccard)
                    .setTsVaId(aId);
            ticketingStaffMapper.insert(ticketingStaff);
        }

    }

    @Override
    public boolean save(TicketingSaveDto ticketingSaveDto) {
        String header = httpServletRequest.getHeader("X-Token");
        Claims claims = JwtUtil.checkJWT(header);
        String id = (String) claims.get("id");
        TicketingStaff ticketingStaff = new TicketingStaff();
        BeanUtils.copyProperties(ticketingSaveDto, ticketingStaff);
        ticketingStaff.setTsQrcard(UUID.randomUUID().toString());
        ticketingStaff.setTWId(id);
        return ticketingStaffMapper.insert(ticketingStaff) == 1;
    }

    @Override
    public List<Map<String, Object>> getById(String wId) {
        return ticketingStaffMapper.selectById(wId);
    }

    @Override
    public boolean remove(String tid) {
        return ticketingStaffMapper.deleteTs(tid) == 1;
    }

    @Override
    public boolean install(TicketingAddDto ticketingAddDto) {
        TicketingStaff ticketingStaff = new TicketingStaff();
        BeanUtils.copyProperties(ticketingAddDto, ticketingStaff);
        return ticketingStaffMapper.install(ticketingStaff) == 1;
    }

    @Override
    public boolean update(TicketingAddDto ticketingAddDto) {
        TicketingStaff ticketingStaff = new TicketingStaff();
        BeanUtils.copyProperties(ticketingAddDto, ticketingStaff);
        return ticketingStaffMapper.update(ticketingStaff) == 1;
    }

    @Override
    public boolean update(String cardId, String Rname, String tid,String datei) {
        return ticketingStaffMapper.updates(cardId, Rname,tid ,datei) == 1;
    }
}
