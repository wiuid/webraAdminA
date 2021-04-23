package top.webra.hander;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import top.webra.bean.ResponseBean;
import top.webra.service.impl.LogServiceImpl;
import top.webra.util.JwtUtil;
import top.webra.utils.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出 处理器
 * @author webra
 */
@Component
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LogServiceImpl logService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
            throws IOException {

        String token = httpServletRequest.getHeader("token");
        Long del = redisUtil.del("token" + token);
        if (del.equals(0L)){
            String username = JwtUtil.getUsername(token);
            logService.createLog("退出", username, "退出操作Redis失败，退出成功");
        }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(responseBean.buildOkMsg("退出成功"));
    }
}
