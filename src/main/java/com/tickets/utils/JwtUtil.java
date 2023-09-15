package com.tickets.utils;

import com.tickets.dto.HttpCode;
import com.tickets.exception.BizException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    // 主题
    public static final String SUBJECT = "RookieLi";

    // 秘钥
    public static final String SECRETKEY = "Rookie666";

    // 过期时间
    public static final long EXPIRE = 1000 * 60 * 60 * 6;  //过期时间，毫秒，一周 1000 * 60 * 60 * 24 * 1

    // 生成 JWT
    public static String geneJsonWebToken(String id) {

        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRETKEY).compact();

        return token;
    }


    // 校验 JWT
    public static Claims checkJWT(String token) {

        try {
            final Claims claims = Jwts.parser().setSigningKey(SECRETKEY).
                    parseClaimsJws(token).getBody();
            return claims;

        } catch (Exception e) {
            throw new BizException().setCode(HttpCode.TOKEN_ERROR.getCode()).setMsg("登录状态过期");
        }

    }
}
