package top.webra.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import top.webra.service.impl.LogServiceImpl;
import top.webra.service.impl.UserLoginServiceImpl;
import top.webra.util.MD5Util;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 14:06
 * @Description: 用户输入用户名密码 进行提交后首先进入该方法
 */
@Slf4j
@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserLoginServiceImpl userLoginService;

    @Autowired
    private LogServiceImpl logService;
    /**
     * 如果已经登录了，访问资源的话  不经过该方法
     * @param authentication 获取输入的账号密码
     * @throws AuthenticationException 异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("登录请求正常");
        // 这个获取表单输入中返回的用户名;
        String username = (String) authentication.getPrincipal();
        // 这个是表单中输入的密码；
        String password = (String) authentication.getCredentials();

        log.info("获取前端发送的账号："+username+ "密码:"+password);
        if (!username.equals("") || !password.equals("")){
            // 是否存在该用户
            UserDetails userInfo = userLoginService.loadUserByUsername(username);
            // 该用户密码是否正确
            if (! MD5Util.getSaltverifyMD5(password, userInfo.getPassword())) {
                logService.createLog("登录", username, "密码错误");
                throw new BadCredentialsException("用户名密码不正确，请重新登陆！");
            }
            logService.createLog("登录", username, "登录成功");
            return new UsernamePasswordAuthenticationToken(username, password, userInfo.getAuthorities());
        }else {
            throw new BadCredentialsException("账号或密码不可为空");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
