package com.tickets.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 密码工具类
 */
public class PwdUtil {

    /**
     * 加密
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String digest(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("md5");

            byte[] bytes = md.digest(str.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
