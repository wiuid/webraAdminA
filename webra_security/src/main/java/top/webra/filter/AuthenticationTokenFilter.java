package top.webra.filter;

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
import top.webra.utils.RedisUtil;

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
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中提取token字段
        //获取header中的验证信息
        String token = httpServletRequest.getHeader("token");
        String url = httpServletRequest.getRequestURI();
        log.info("获取到的请求地址：" + url);
        if (token != null) {
            // 获取用户名username
            Claims claims = JwtUtil.getClaims(token);
            String username = String.valueOf(claims.get("sub"));
            boolean tokenTimeout = JwtUtil.getTokenTimeout(claims);
            boolean tokenHas = redisUtil.hasKey("token" + token);
            // token过期 和 检测上下文中是否存在该用户的令牌
            if (tokenHas && tokenTimeout && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 根据用户名获取用户对象
                UserDetails userDetails = userLoginService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    // 将该用户设置为已登录
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(httpServletRequest, response);
            }else if (tokenHas && tokenTimeout && SecurityContextHolder.getContext().getAuthentication() != null){
                filterChain.doFilter(httpServletRequest, response);
            }else {
                customResponseLoginErr(response, "登录超时");
            }
        }else {
            // 没有token的情况，判断路径
            String uri = httpServletRequest.getRequestURI();
            String str = "/system/";
            if ( uri.contains(str) ){
                customResponseLoginErr(response, "未登录");
            }else {
                filterChain.doFilter(httpServletRequest, response);
            }

        }
    }

    private void customResponseLoginErr(HttpServletResponse response, String msg) {
        try {
            response.setHeader("Content-Type", "application/json");
            response.setStatus(200);
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(responseBean.buildNotLogin(msg));
            writer.flush();
        }catch (IOException e){
            System.out.println("IO异常");
        }

    }
}
