package top.webra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.webra.mapper.AuthMapper;
import top.webra.mapper.RoleMapper;
import top.webra.mapper.UserMapper;
import top.webra.pojo.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:42
 * @Description: --
 */
@Slf4j
@Service
public class UserLoginServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private LogServiceImpl logService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailsServiceImpl参数的用户名：" + username);

        // 权限列表
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // 根据用户名获取用户息信
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username",username);
        User user = userMapper.selectOne(userWrapper);

        // 如果用户不存在，抛出异常
        if(user == null){
            logService.createLog("登录", username, "未找到该账户");
            throw new UsernameNotFoundException("user not font");
        }

        log.info("UserDetailsServiceImpl通过数据库获取到的用户名：" + user.getUsername());
        log.info("UserDetailsServiceImpl通过数据库获取到的密码：" + user.getPassword());

        // 获取权限信息
        QueryWrapper<Role> userRoleWrapper = new QueryWrapper<>();
        userRoleWrapper.eq("id",user.getRoleId());

        Role role = roleMapper.selectOne(userRoleWrapper);

        // 权限信息为空，不可能存在的情况，应至少有一个默认权限
        if(role == null){
            return new UserLogin(user.getId(),username,user.getPassword(),authorities);
        }
        // 将权限字符串 转换为 字符串列表
        String authIds = role.getAuthIds();
        List<String> authIdsList= Arrays.asList(authIds .split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
        // 根据角色的权限列表 查询权限
        ArrayList<Integer> ids = new ArrayList<>();
        QueryWrapper<Auth> authListWrapper = new QueryWrapper<>();
        // 将 字符串类型的权限列表 转换为 Integer类型
        for (String s : authIdsList) {
            Integer integer = Integer.valueOf(s);
            ids.add(integer);
        }
        // 将ids权限列表 传入条件构造器，查询所有对应的权限
        authListWrapper.in("id",ids);
        List<Auth> auths = authMapper.selectList(authListWrapper);
        // 将对应的权限字符加上前缀ROLE_ 传入authorities，将该值传给UserDetails
        for (Auth auth : auths) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + auth.getName()));
        }
        return new UserLogin(user.getId(),username,user.getPassword(),authorities);
    }
}
