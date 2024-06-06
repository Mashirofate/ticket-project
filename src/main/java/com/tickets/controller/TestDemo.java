package com.tickets.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDemo {

    public static   List<DxhdYpxx>  readExcel(String path) {
        List<DxhdYpxx> newList=new ArrayList<>();
        try {
			/*// 如果需要通过URL获取资源的加上以下的代码，不需要的省略就行
			URL url = new URL(strURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3*1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 获取输入流
			InputStream inputStream = conn.getInputStream();
			......*/
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 获取文件输入流
            InputStream inputStream = new FileInputStream(path);
            // 定义一个org.apache.poi.ss.usermodel.Workbook的变量
            Workbook workbook = null;
            // 截取路径名 . 后面的后缀名，判断是xls还是xlsx
            // 如果这个判断不对，就把equals换成 equalsIgnoreCase()
            workbook = new HSSFWorkbook(inputStream);
            // 获取第一张表
            Sheet sheet = workbook.getSheetAt(0);
            // sheet.getPhysicalNumberOfRows()获取总的行数

            // 循环读取每一行
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                // 循环读取每一个格
                Row row = sheet.getRow(i);
                DxhdYpxx ypxx=new DxhdYpxx();
                // row.getPhysicalNumberOfCells()获取总的列数
                for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                    // 获取数据，但是我们获取的cell类型
                    Cell cell = row.getCell(index);
                    // 转换为字符串类型
                    // 获取得到字符串
                    String value = cell.getStringCellValue();

                    if(index==0){
                        ypxx.setId(value);
                    }
                    if(index==1){
                        ypxx.setPerformId(value);
                    }
                    if(index==2){
                        ypxx.setProjectId(value);
                    }
                    if(index==3){
                        ypxx.setCheckPass(value);
                    }
                    if(index==4){
                        ypxx.setCheckCertType(value);
                    }
                    if(index==5){
                        ypxx.setCheckCertTypeNo(value);
                    }
                    if(index==6){
                        ypxx.setVenueName(value);
                    }
                    if(index==7){
                        try {
                            Date date = sdf.parse(value);
                            ypxx.setEntryTime(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if(index==8){
                        try {
                            Date date = sdf.parse(value);
                            ypxx.setCheckTime(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if(index==9){
                        ypxx.setVoucherId(value);
                    }
                    if(index==10){
                        ypxx.setBindCard(value);
                    }
                    if(index==11){
                        ypxx.setBindUserName(value);
                    }
                    if(index==12){
                        ypxx.setDeviceName(value);
                    }
                    if(index==13){
                        ypxx.setDeviceCode(value);
                    }
                    if(index==14){
                        try {
                            Date date = sdf.parse(value);
                            ypxx.setInsertTime(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if(index==15){
                        ypxx.setBindPhoto(value);
                    }
                    if(index==16){
                        ypxx.setBindCardPhoto(value);
                    }
                    if(index==17){
                        ypxx.setIdcode(value);
                    }
                    if(index==18){
                        ypxx.setHjdxx(value);
                    }
                    if(index==19){
                        ypxx.setMzxx(value);
                    }
                    newList.add(ypxx);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  newList;
    }
}

