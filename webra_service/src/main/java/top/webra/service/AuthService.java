package top.webra.service;

import top.webra.bean.ResponseBean;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:06
 * @Description: --
 */
public interface AuthService {

    /**
     * 根据token解析出用户的权限信息，用来动态生成路由
     * @param token 解析
     */
    Map<String, Object> getUserAside(String token);

    /**
     * 获得tree列表
     */
    ResponseBean getTree();
}
