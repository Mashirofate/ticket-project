package com.tickets.interceptor;

import com.tickets.annotations.Authentication;
import com.tickets.dto.HttpCode;
import com.tickets.exception.BizException;
import com.tickets.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component
public class AuthorityInterceptor implements HandlerInterceptor {
    /**
     * 前端携带token的头属性名称
     */
    private static final String TOKEN_KEY = "X-Token";




    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 预请求直接放行（解决 token拦截错误）
        String method = request.getMethod();
        if (method.equals("OPTIONS")) {
            return true;
        }
        // 对于请求不是Controller的，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //判断如果请求的类是swagger的控制器，直接通行。
        if(handlerMethod.getBean().getClass().getName().equals("springfox.documentation.swagger.web.ApiResourceController")){
            return  true;
        }


        // 获取Controller 参数上的注解
        Authentication methodAnnotation = handlerMethod.getMethodAnnotation(Authentication.class);
        // 没有参数，将会认定接口未开放
        if (methodAnnotation == null) {
            throw new BizException().setCode(HttpCode.NOT_AUTHORIZED.getCode()).setMsg("接口暂时未开放");
        }

        //  不需要接口鉴权的，直接放行
        if (!methodAnnotation.required()) {
            return true;
        }
        // 判断是否需要登录
        if (methodAnnotation.isLogin()) {
            String token = request.getHeader(TOKEN_KEY);
           if (StringUtils.isEmpty(token)) {
                // token不存在
                throw new BizException().setCode(HttpCode.TOKEN_ERROR.getCode()).setMsg("需要登录");
            }
            Claims claims = JwtUtil.checkJWT(token);
            if (methodAnnotation.isRequiredUserInfo()) {
                // 将用户的主键id放入请求中
                String id = (String) claims.get("id");
                request.setAttribute("id", id);
            }

        }

        return true;
    }
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
