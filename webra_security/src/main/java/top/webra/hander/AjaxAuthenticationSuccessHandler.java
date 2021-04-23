package top.webra.hander;

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
import top.webra.util.CastUtil;
import top.webra.util.IpUtil;
import top.webra.util.JwtUtil;
import top.webra.utils.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    private RedisUtil redisUtil;

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

        String token = JwtUtil.createJWT(CastUtil.toString(user.getId()), user.getUsername(), role.getAuthIds());
        // 权限列表
        Map<String, Object> data = authService.getUserAside(token);
        // token
        data.put("token",token);

        UUID uuid = UUID.randomUUID();
        String auth = authentication.getDetails().toString();
        String ip = auth.substring(auth.lastIndexOf("s: "), auth.lastIndexOf(";")).split(" ")[1];
        String city = IpUtil.getCity(ip);

        HashMap<String, Object> navtive = new HashMap<>(5);
        navtive.put("uuid", uuid.toString());
        navtive.put("ip", ip);
        navtive.put("username", user.getUsername());
        navtive.put("city", city);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        navtive.put("date", simpleDateFormat.format(System.currentTimeMillis()));

        redisUtil.hmset("token"+ token, navtive, 3600L);

        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(responseBean.buildOk(data));
    }


}
