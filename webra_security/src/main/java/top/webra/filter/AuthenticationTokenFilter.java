package top.webra.filter;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.webra.bean.ResponseBean;
import top.webra.service.impl.UserLoginServiceImpl;
import top.webra.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-08 14:16
 * @Description: 将security默认的session验证更改为token验证
 */
@Slf4j
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserLoginServiceImpl userLoginService;

    @Autowired
    private ResponseBean responseBean;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中提取token字段
        String authHeader = httpServletRequest.getHeader("token");//获取header中的验证信息

        if (authHeader != null) {
            JwtUtil jwtUtil = new JwtUtil();
            try {
                // 将JWT 产生的token过期的报错进行吸收
                Claims claims = jwtUtil.parseJWT(authHeader);
                String subject = claims.getSubject();
                // 获取用户名username
                String username = String.valueOf(subject);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 根据用户名获取用户对象
                    UserDetails userDetails = userLoginService.loadUserByUsername(username);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        //设置为已登录
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                filterChain.doFilter(httpServletRequest, response);
            }catch (Exception e){
                // token过期 直接返回403
                response.setHeader("Content-Type", "application/json");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                responseBean.buildNotLogin("login timeOut");
                writer.write(JSON.toJSONString(responseBean));
                writer.flush();
                log.info("token 过期");

            }
        }else {
            // 没有token的情况，判断路径
            String uri = httpServletRequest.getRequestURI();
            if ( uri.contains("/system/") ){
                response.setHeader("Content-Type", "application/json");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                responseBean.buildNotLogin("当前未登录，跳转至登录页");
                writer.write(JSON.toJSONString(responseBean));
                writer.flush();
            }else {
                filterChain.doFilter(httpServletRequest, response);
            }

        }
    }
}
