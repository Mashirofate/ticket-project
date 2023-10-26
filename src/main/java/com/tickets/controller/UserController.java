package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.exception.BizException;
import com.tickets.service.UserService;
import com.tickets.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name= "用户接口")
@RestController
@RequestMapping("/u")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "创建一个用户")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody UserSinInDto userSinInDto) {
        userService.save(userSinInDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(required = true)
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginDto loginDto) {
        String uId = userService.isLogin(loginDto.getUsername(), loginDto.getPassword());
        if (StringUtils.isEmpty(uId)) {
            return new ResponseResult(HttpCode.ERROR.getCode(),"登录失败");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", JwtUtil.geneJsonWebToken(uId));

        return ResponseResult.SUCCESS(map);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "用户推出登录")
    @PostMapping("/logout")
    public ResponseResult logout() {

        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "获取用户信息", description  = "前提是以通过 /login接口获得了token信息，且请求头携带了token信息。")
    @GetMapping("/info")
    public ResponseResult info() {
        String id = (String) httpServletRequest.getAttribute("id");
        Map<String, Object> byId = userService.getById(id);

        String uStartusing = (String) byId.get("uStartusing");
        if (byId.isEmpty() || "0".equals(uStartusing)){
            throw new BizException().setCode(HttpCode.TOKEN_ERROR.getCode()).setMsg("用户不存在");
        }else if ("2".equals(uStartusing)){
            throw new BizException().setCode(HttpCode.TOKEN_ERROR.getCode()).setMsg("用户已冻结");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("name", byId.get("uUser"));
        map.put("role", byId.get("uAuthority"));
        map.put("avatar","https://img.bosszhipin.com/beijin/upload/com/logo/20200313/00e5073d077fc9d9acda4803ec6568ec52eb86bee275784a57bc4e195a240f82.jpg?x-oss-process=image/resize,w_120,limit_0");
        return ResponseResult.SUCCESS(map);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "用户条件搜索")
    @GetMapping("/search")
    public ResponseResult search(UserSeachDto seachDto) {
        return ResponseResult.SUCCESS(userService.getByKeys(seachDto));
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "删除用户", description  = "根据uId删除")
    @DeleteMapping("/{uId}")
    public ResponseResult delById(@PathVariable String uId) {
        userService.remove(uId);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "更新用户", description  = "")
    @PostMapping("/update")
    public ResponseResult updateVenue(@RequestBody UserSinInDto userSinInDto) {
        userService.update(userSinInDto);
        return ResponseResult.SUCCESS();
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "下载批量导入用户模板", description  = "")
    @GetMapping("/model/download")
    public void modelDownload(HttpServletResponse response) throws IOException {
        //表头数据
        String[] header = {"用户名", "姓名", "性别", "年龄", "地址", "分数"};
        //数据内容
        String[] student1 = {"1", "小红", "女", "23", "成都青羊区", "96"};
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet = workbook.createSheet("学生表");
        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);
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
        response.setHeader("Content-disposition", "attachment;filename=student.xls");
        //刷新缓冲
        response.flushBuffer();
        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "批量添加用户", description  = "需要下载固定的模板")
    @PostMapping("/addBatch")
    public ResponseResult addBatch(@RequestParam("file") MultipartFile file) {
//        ArrayList<ArrayList<String>> analysis = ExcelUtils.analysis(file);
//        System.out.println(analysis);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "批量删除用户", description  = "根据uId删除")
    @DeleteMapping("/batch")
    public ResponseResult delByIds(@RequestParam String ids) {
        String[] uIds = ids.split(",");
        userService.removeBatch(uIds);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "更新用户的状态", description  = "")
    @PutMapping("/{uId}/{uStartusing}")
    public ResponseResult updateStartusing(@PathVariable String uId, @PathVariable String uStartusing) {
        userService.updateStartusing(uId, uStartusing);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "判断用户名是否存在")
    @GetMapping("/is/{uUser}")
    public ResponseResult isUUserExist(@PathVariable String uUser) {
        return ResponseResult.SUCCESS(userService.isUUserExist(uUser));
    }
}
