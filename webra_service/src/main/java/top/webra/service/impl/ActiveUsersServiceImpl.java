package top.webra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.webra.bean.ResponseBean;
import top.webra.service.ActiveUsersService;
import top.webra.utils.RedisUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/4/23 8:14
 * @Description: --
 */
@Service
public class ActiveUsersServiceImpl implements ActiveUsersService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ResponseBean responseBean;
    @Override
    public String getActiveUserList() {
        Set<String> likeKeyList = redisUtil.getLikeKey("token*");
        if (likeKeyList.isEmpty()){
            return responseBean.buildOkInitNull("activeUserList");
        }else {
            ArrayList<Map<String, Object>> activeUserList = new ArrayList<>();
            for (String key : likeKeyList) {
                Map<String, Object> mapValue = redisUtil.hmget(key);
                activeUserList.add(mapValue);
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("activeUserList", activeUserList);

            return responseBean.buildOk(data);
        }
    }

    @Override
    public String deleteActiveUserList(String Uuid) {
        Set<String> likeKey = redisUtil.getLikeKey("token*");
        if (likeKey.isEmpty()){
            return responseBean.buildWarring("异常操作");
        }else {
            for (String key : likeKey) {
                Map<String, Object> hmget = redisUtil.hmget(key);
                String redisUuid = hmget.get("uuid").toString();
                if (redisUuid.equals(Uuid)) {
                    redisUtil.del(key);
                    return responseBean.buildOkMsg("踢下线成功！");
                }
            }
            return responseBean.buildWarring("未查到该人员！");
        }
    }
}
