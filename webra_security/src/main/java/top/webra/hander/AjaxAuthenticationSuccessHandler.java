package top.webra.hander;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.webra.bean.ResponseBean;
import top.webra.mapper.RoleMapper;
import top.webra.mapper.UserMapper;
import top.webra.pojo.Role;
import top.webra.pojo.User;
import top.webra.service.impl.AuthServiceImpl;
import top.webra.service.impl.UserLoginServiceImpl;
import top.webra.util.CastUtil;
import top.webra.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 登录成功处理器
 * @author webra
 */
@Component

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ResponseBean responseBean;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AuthServiceImpl authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication)
            throws IOException {

        String username = (String) authentication.getPrincipal();

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);

        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("id", user.getRoleId());
        Role role = roleMapper.selectOne(roleQueryWrapper);

        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.createJWT(CastUtil.toString(user.getId()), user.getUsername(), role.getAuthIds());
        // 权限列表
        Map<String, Object> data = authService.getUserAside(token);
        // token
        data.put("token",token);

        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(responseBean.buildOk(data));
    }


}
