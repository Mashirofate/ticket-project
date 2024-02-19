package com.tickets.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public static String postWithJson(String argUrl, Map<String, String> headers, String jsonStr, Proxy proxy) throws IOException {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "application/json"); //发送数据类型
        headers.put("Charset", "UTF-8"); //发送请求编码类型
        headers.put("Connection", "Close"); //不使用长链接
        return post(argUrl, headers, jsonStr, proxy);
    }

    public static String post(String argUrl, Map<String, String> headers, String postData, Proxy proxy) throws IOException {
        InputStream is = null; //字节输入流，用来将文件中的数据读取到java程序中
        BufferedWriter writer = null; //字符缓冲输出流，将文本写入字符输出流，缓冲字符
        ByteArrayOutputStream os = null; //byte数组缓冲区，用来捕获内存缓冲区的数据
        HttpURLConnection connection = null; //网络请求连接

        try {
            // 将url转换为URL类对象
            URL url = new URL(argUrl);
            // 打开url连接
            if (proxy == null) {
                connection = (HttpURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection(proxy);
            }
            // 连接设置
            connection.setRequestMethod("POST"); //设定请求的方法为POST，默认是GET
            connection.setConnectTimeout(20000); //设置连接主机超时（单位：毫秒）
            connection.setReadTimeout(20000); //设置从主机读取数据超时（单位：毫秒）
            connection.setDoOutput(true); //设置是否向HttpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
            connection.setDoInput(true); //设置是否从HttpUrlConnection读入，默认情况下是true;
            connection.setUseCaches(false); //Post请求不能使用缓存

            // 设置请求头信息
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 连接
            connection.connect();

            // 向字符缓冲流写入数据
            if (postData != null) {
                writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                writer.write(postData);
                writer.close();
            }

            // 得到响应码
            int responseCode = connection.getResponseCode();

            // 请求成功
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 得到响应流
                is = connection.getInputStream();
                os = new ByteArrayOutputStream();
                // 创建字节数组 用于缓存
                byte[] tmp = new byte[1024];
                // 将内容读到tmp中 读到末尾为-1
                int i = is.read(tmp);
                while (i > 0) {
                    os.write(tmp, 0, i);
                    i = is.read(tmp);
                }
                // 获取内存缓冲区的数据
                byte[] bs = os.toByteArray();
                // 转换为字符串
                return new String(bs);
            }
            // 抛出错误状态码
            throw new IOException("responseCode:" + responseCode);
        } catch (Exception e) {
            // 抛出错误信息
            throw e;
        } finally {
            // 关闭资源
            if (is != null)
                is.close();
            if (writer != null)
                writer.close();
            if (os != null)
                os.close();
            if (connection != null)
                connection.disconnect();
        }
    }
}
