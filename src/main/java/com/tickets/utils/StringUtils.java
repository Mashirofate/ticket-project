package com.tickets.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 字符串辅助工具类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 清空StringBuffer内容
     */
    public static StringBuffer clear(StringBuffer buffer) {
        if (buffer == null || buffer.length() == 0) {
            return buffer;
        } else {
            return buffer.delete(0, buffer.length());
        }
    }

    /**
     * 比较两字符串是否相等<br>
     * null equals null = true<br>
     * null equals "" = true<br>
     * "" equals null = true<br>
     * " " equals "" = true<br>
     * "A" equals "a" = false
     */
    public static boolean equals(String str1, String str2) {
        str1 = (str1 == null) ? "" : str1.trim();
        str2 = (str2 == null) ? "" : str2.trim();
        return str1.equals(str2);
    }

    /**
     * 将一组字符串连接为一个字符串
     *
     * @param items
     * @param separator
     *            分隔符
     * @return
     */
    public static String join(List<String> items, String separator) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        int size = items.size();
        Object[] objs = new Object[size];
        for (int i = 0; i < size; i++) {
            objs[i] = items.get(i);
        }
        return join(objs, separator);
    }

    public static String format(String str, int length, char cover) {
        str = trimToEmpty(str);
        int currLength = str.length();
        if (currLength < length) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 1; i <= length - currLength; i++) {
                buffer.append(cover);
            }
            buffer.append(str);
            return buffer.toString();
        }
        return str;
    }

    public static String format(long number, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }

    /**
     * 将字符串分隔成字符数组
     *
     * @param str
     * @return
     */
    public static String[] splitWithSplace(String str) {
        str = trimToEmpty(str);
        str = deleteWhitespace(str);
        String[] item = str.split(""); // items[0]为""
        String[] result = new String[item.length - 1];
        for (int i = 1, j = item.length; i < j; i++) {
            result[i - 1] = item[i];
        }
        return result;
    }

    /**
     * 移除字符串中指定的一组字符串
     *
     * @param str
     * @param searchList
     * @param max
     *            最大移除数量
     * @return
     */
    public static String remove(String str, String[] searchList, int max) {
        for (String sub : searchList) {
            str = replace(str, sub, "", max);
        }
        return str;
    }

    public static String remove(String str, List<String> searchList, int max) {
        for (String sub : searchList) {
            str = replace(str, sub, "", max);
        }
        return str;
    }

    /**
     * 压缩字符串
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String compress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
            return out.toString("ISO-8859-1");
        } catch (IOException e) {
            return str;
        }
    }

    /**
     * 解压缩字符串
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String uncompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
            return out.toString();
        } catch (IOException e) {
            return str;
        }
    }
    /**
     *
     * 方法名: </br>
     * 详述:SQL 参数转换 </br>
     * 开发人员：钟庆亮</br>
     * 创建时间：2017-6-10 下午10:53:25</br>
     * @param map
     * @return
     */
    public static Object[] getMapToObjectSql(Map<String, Object> map){
        Object[] object=new Object[map.size()];
        int i=0;
        for (String key : map.keySet()) {
            object[i++]=map.get(key);
        }
        return object;
    }

    /**
     *
     * 方法名: guoHtml</br>
     * 详述:过滤html标签的代码 </br>
     * 开发人员：钟庆亮</br>
     * 创建时间：2017-8-1 上午3:43:26</br>
     * @param s
     * @return
     */
    public static String guoHtml(String s){
        if(null != s  && !"".equals(s)){
            String str=s.replaceAll("<[.[^<]]*>|&nbsp;","");
            return str;
        }
        return s;
    }
    /**
     *
     * 方法名: </br>
     * 详述:阿拉伯数字转换为中文数字 </br>
     * 开发人员：钟庆亮</br>
     * 创建时间：2017-8-1 上午4:06:45</br>
     * @param intInput
     * @return
     */
    public static String ToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十
        {
            if (si.substring(0, 1).equals("1"))
                sd += "十";
            else
                sd += (GetCH(intInput / 10) + "十");
            sd += ToCH(intInput % 10);
        } else if (si.length() == 3)// 百
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2)
                sd += "零";
            sd += ToCH(intInput % 100);
        } else if (si.length() == 4)// 千
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3)
                sd += "零";
            sd += ToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4)
                sd += "零";
            sd += ToCH(intInput % 10000);
        }

        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }
}
