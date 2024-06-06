package com.tickets.service.impl;

import com.tickets.dto.*;
import com.tickets.entity.TicketingStaff;
import com.tickets.mapper.TicketingStaffMapper;
import com.tickets.service.TicketingStaffService;
import com.tickets.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public List<Map<String,Object>> getByKeys1(String scanCode,String aId) {
        return ticketingStaffMapper.selectCountByscanCode1(scanCode,aId);
    }
    @Override
    public int getscanCode(String scanCode) {
        return ticketingStaffMapper.getscanCode(scanCode );
    }
    @Override
    public List<Map<String,Object>> getcardId(String cardId,String aId,String tId) {
        List<Map<String,Object>> list=null;
        if(tId == null){
            list= ticketingStaffMapper.getcardIdto(cardId,aId,tId);
        }else{
            list=  ticketingStaffMapper.getcardId(cardId,aId,tId);
        }

        return list;
    }
    @Override
    public List<Map<String,Object>> getcardId2(String cardId,String aId,String tId) {
        List<Map<String,Object>> list=null;
        if(tId == null){
            list= ticketingStaffMapper.getcardId2to(cardId,aId,tId);
        }else{
            list=  ticketingStaffMapper.getcardId2(cardId,aId,tId);
        }

        return list;
    }
    /**
     * @param cardId
     * @param aId
     * @return
     */
    @Override
    public int getByKeycardIds(String cardId, String aId) {
        return ticketingStaffMapper.getByKeycardIds(cardId,aId);
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
    public boolean update(String cardId, String Rname, String tid, String datei, String BIND_MZXX) {
        return ticketingStaffMapper.updates(cardId, Rname,tid ,datei,BIND_MZXX) == 1;
    }

    @Override
    public boolean updatewe(String cardId, String Rname, String wtid,String datei,String BIND_MZXX) {
        return ticketingStaffMapper.updatewe(cardId, Rname,wtid ,datei,BIND_MZXX) == 1;
    }
    @Override
    public boolean installwtid(String cardId, String scanCode, String Rname, String aId,String datei,String BIND_MZXX) {

        UUID uuid = UUID.randomUUID();
        String tid =uuid.toString();
        return ticketingStaffMapper.installwtid(cardId, scanCode,Rname, aId,datei,tid,BIND_MZXX) == 1;
    }
    @Override
    public boolean installwtidexhibition(String cardId, String Rname, String Phone, String aId, String datei) {

        UUID uuid = UUID.randomUUID();
        String tid =uuid.toString();
        return ticketingStaffMapper.installwtidexhibition(cardId, Rname,Phone, aId,datei,tid) == 1;
    }
    /**
     * @param
     * @param aId
     * @return
     */
    @Override
    public List<Map<String, Object>> getByapplet( String aId, String ips) {
        return ticketingStaffMapper.getByapplet( aId, ips) ;
    }

    /**
     * @param aId
     * @param UploadQuantity
     * @return
     */
    @Override
    public int getByappletlisteid(String aId, List<String> UploadQuantity, String ips) {
        return  ticketingStaffMapper.getByappletlisteid(aId,  UploadQuantity, ips);
    }

    /*
     * @param //fImage
     * @return
     */
    @Override
    public int installEntryrecord(List listen,List listf,List listi) {
        int i=ticketingStaffMapper.installEntryrecord(listen);
        i=ticketingStaffMapper.installEntryrecordinfo(listi);
        i=ticketingStaffMapper.installEntryrecordfimage(listf);
        return i;
    }

    @Override
    public int installemploy(List list) {
        return ticketingStaffMapper.installemploy(list) ;
    }
    public int installcamera(String teId,String teDate,String teAisle,String teaId,String teCategory,String teMarking) {
        return ticketingStaffMapper.installcamera(teId,teDate,teAisle,teaId,teCategory,teMarking) ;
    }

    /**
     * @param aId
     * @param cardId
     * @param scanCode
     * @return
     */
    @Override
    public String getticketing(String eId) {
        return ticketingStaffMapper.getticketing(eId);
    }

    /**
     * @param aId
     * @param cardId
     * @param scanCode
     * @return
     */
    @Override
    public List<Map<String, Object>> getenueing(String aId, String cardId, String scanCode,String tSeatingarea,String tRownumber,String tSeat) {
        return ticketingStaffMapper.getenueing(aId, cardId,scanCode,tSeatingarea,tRownumber,tSeat);
    }
}
