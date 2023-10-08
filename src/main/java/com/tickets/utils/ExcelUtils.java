package com.tickets.utils;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    private static Object CellType;

    /**
     * 获取并解析excel文件，返回一个二维集合
     *
     * @param file 上传的文件
     * @return 二维集合（第一重集合为行，第二重集合为列，每一行包含该行的列集合，列集合包含该行的全部单元格的值）
     */
    public static List<Map<String, Object>> analysis(MultipartFile file) {
        List<Map<String, Object>> list = new ArrayList<>();
        //获取文件名称
        String fileName = file.getOriginalFilename();
        try {
            //获取输入流
            InputStream in = file.getInputStream();
            //判断excel版本
            Workbook workbook = null;
            if (judegExcelEdition(fileName)) {
                workbook = new XSSFWorkbook(in);
            } else {
                workbook = new HSSFWorkbook(in);
            }

//            获取头部
            List<String> header = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(0);
            Row sheetRow1 = sheet.getRow(0);
            for (int j = 0; j < sheetRow1.getPhysicalNumberOfCells(); j++) {
                //将每一个单元格的值装入列集合
                header.add(sheetRow1.getCell(j).getStringCellValue());
            }

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                //循环获取工作表的每一行
                sheetRow1 = sheet.getRow(i);
                Map<String, Object> cel = new HashMap<>();
                for (int j = 0; j < sheetRow1.getPhysicalNumberOfCells(); j++) {

//
                    if(sheet.getRow(i).getCell(j) == null){
                        sheet.getRow(i).createCell(j).setCellType(Cell.CELL_TYPE_STRING);
                        sheet.getRow(i).createCell(j).setCellValue(new  HSSFRichTextString(String.valueOf(sheet.getRow(i).getCell(j).getStringCellValue())));
                        cel.put(header.get(j),null);
                    }else {//单元格有值时，getCell方法获获取到单元格。
                        sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                        cel.put(header.get(j), sheetRow1.getCell(j).getStringCellValue());
                    }

                }
                list.add(cel);

                //关闭资源
                /*1 在引用POI包时一定要选择合适正确的POI版本。
                2 在POI3.1时，workbook需要我们自己将其关闭，而在POI3.7时，workbook已经不需要我们自己调用close()方法进行关闭了
                workbook.close();*/
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("===================未找到文件======================");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("===================上传失败======================");
        }

        return list;
    }

    /**
     * 判断上传的excel文件版本（xls为2003，xlsx为2017）
     *
     * @param fileName 文件路径
     * @return excel2007及以上版本返回true，excel2007以下版本返回false
     */
    private static boolean judegExcelEdition(String fileName) {
        return !fileName.matches("^.+\\.(?i)(xls)$");

    }

}
