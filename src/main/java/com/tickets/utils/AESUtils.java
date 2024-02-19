package com.tickets.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtils {
    /**
     * 加密 1.构造密钥生成器 2.根据 ecnodeRules 规则初始化密钥生成器 3.产生密钥 4.创建和初始化密码器 5.内容加密 6.返回字符串
     * @param encodeRules 密钥规则，类似于密钥
     * @param content 待加密内容
     * @return
     */
    public static String AESEncode(String encodeRules, String content) {
        // 初始化向量,必须 16 位
        String ivStr = "AESCBCPKCS5Paddi";
        try {
            // 1.构造密钥生成器，指定为 AES 算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            // 新增下面两行，处理 Linux 操作系统下随机数生成不一致的问题
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(encodeRules.getBytes());
            keygen.init(128, secureRandom);
            // 3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            // 4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //System.out.println(Base64.getEncoder().encodeToString(raw));

            // 5.根据字节数组生成 AES 密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            // 6.根据指定算法 AES 自成密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的 KEY
            //// 指定一个初始化向量 (Initialization vector，IV)， IV 必须是 16 位
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivStr.getBytes("UTF-8")));
            // 8.获取加密内容的字节数组(这里要设置为 utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes("utf-8");
            // 9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            // 10.将加密后的数据转换为字符串
            // 这里用 Base64Encoder 中会找不到包
            // 解决办法：
            // 在项目的 Build path 中先移除 JRE System Library，再添加库 JRE System
            // Library，重新编译后就一切正常了。
            String AES_encode = new String(Base64.getEncoder().encode(byte_AES));
            // 11.将字符串返回
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        // 如果有错就返加 nulll
        return null;
    }

    /**
     * 解密 解密过程： 1.同加密1-4步 2.将加密后的字符串反纺成byte[]数组 3.将加密内容解密
     * @param encodeRules 密钥规则，类似于密钥
     * @param content 待加密内容
     * @return
     */
    public static String AESDecode(String encodeRules, String content) {
        // 初始化向量,必须16位
        String ivStr = "AESCBCPKCS5Paddi";
        try {
            // 1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            // 新增下面两行，处理 Linux 操作系统下随机数生成不一致的问题
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(encodeRules.getBytes());
            keygen.init(128, secureRandom);
            // 3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            // 4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            // 5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            // 6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            //// 指定一个初始化向量 (Initialization vector，IV)， IV 必须是16位
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivStr.getBytes("UTF-8")));
            // 8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = Base64.getDecoder().decode(content);
            /*
             * 解密
             */
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        // 如果有错就返加nulll
        return null;
    }
}
