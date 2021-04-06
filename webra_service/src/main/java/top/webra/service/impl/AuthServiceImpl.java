package top.webra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.webra.bean.ResponseBean;
import top.webra.mapper.AuthMapper;
import top.webra.pojo.Auth;
import top.webra.service.AuthService;
import top.webra.util.JwtUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:08
 * @Description: 权限逻辑业务类（空置，不动）
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private ResponseBean responseBean;

    /**
     * 获取动态路由
     * @param token     token
     */
    public Map<String, Object> getUserAside(String token) {
        // 解析权限
        JwtUtil jwtUtil = new JwtUtil();
        Claims claims = jwtUtil.parseJWT(token);
        Object roles = claims.get("roles");
        String[] split = String.valueOf(roles).split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(Integer.valueOf(s));
        }

        // 获取各权限的详细信息
        List<Auth> authList = authMapper.selectList(new QueryWrapper<Auth>().in("id", integers));

        // 对权限进行套娃
        ArrayList<Auth> auths = new ArrayList<>();
        for (Auth auth : authList) {
            if (auth.getSuperId().equals(0)){
                auths.add(auth);
            }else{
                for (Auth auth1 : auths) {
                    if (auth1.getId().equals(auth.getSuperId())){
                        List<Auth> children = auth1.getChildren();
                        if (children == null){
                            ArrayList<Auth> auths1 = new ArrayList<>();
                            auths1.add(auth);
                            auth1.setChildren(auths1);
                        }else {
                            children.add(auth);
                        }
                    }
                }
            }
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("routes", auths);
        return data;
    }

    /**
     * 获得选择树，用于角色选择对应的权限
     */
    public ResponseBean getTree() {
        // 寄存嵌套后的数据
        ArrayList<Auth> auths = new ArrayList<>();
        // 查询的源数据
        List<Auth> authList = authMapper.selectList(new QueryWrapper<Auth>().select("id", "title", "super_id", "whether").orderByAsc("id"));
        // 将父节点都取出来
        for (Auth auth : authList) {
            if (auth.getSuperId().equals(0)) {
                auths.add(auth);
            }else{
                break;
            }
        }
        // 嵌套数据
        getChildren(auths, authList);

        HashMap<String, Object> data = new HashMap<>();
        data.put("authTree", auths);
        responseBean.buildOk(data);
        return responseBean;
    }

    /**
     * @param auths         数据存储
     * @param authList      源数据
     */
    private void getChildren(List<Auth> auths, List<Auth> authList){
        for (Auth auth : auths) {
            if (!auth.getWhether().equals(0)){
                for (Auth auth1 : authList) {
                    if (auth.getId().equals(auth1.getSuperId())) {
                        List<Auth> children = auth.getChildren();
                        if (children == null) {
                            ArrayList<Auth> auths1 = new ArrayList<>();
                            auths1.add(auth1);
                            auth.setChildren(auths1);
                        }else {
                            children.add(auth1);
                        }
                        if (auth1.getWhether().equals(1)) {
                            getChildren(auth.getChildren(), authList);
                        }
                    }
                }
            }
        }
    }
}
