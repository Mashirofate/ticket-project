package com.tickets.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
/**
 * @Author fanchunyu
 * @Date 2020/4/23
 * @Version V1.0
 **/
public class ZipUtil2 {
    // 测试方法
    public static void main(String[] args) throws IOException {

// 测试字符串
        String str = "";
        System.out.println("原长度：" + str.length());
        System.out.println("压缩后：" + ZipUtil2.compress(str).length());
        System.out
                .println("解压缩：" + ZipUtil2.uncompress(ZipUtil2.compress(str)));
    }

    // 压缩
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes("GB18030"));
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    // 解压缩
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(
                str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
// toString()使用平台默认编码，也可以显式的指定如toString("GBK")
        return out.toString("GB18030");
    }
}