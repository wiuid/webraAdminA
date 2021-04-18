package top.webra.adminA;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.webra.mapper.UserMapper;
import top.webra.pojo.User;
import top.webra.utils.RedisUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/4/18 15:23
 * @Description: --
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void redisGetTest(){
        RedisUtil redisUtil = new RedisUtil();
        if (redisUtil.hasKey("userList")){
            System.out.println("yes");
        }else {
            List<User> userList = userMapper.selectList(new QueryWrapper<User>());
            String s = JSON.toJSONString(userList);
            redisUtil.set("userList", s);
            System.out.println("no");
        }

    }

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    //添加一个字符串
    @Test
    public void testSet() {
        boolean webSite = redisUtil.set("WebraAdminVueA", "https://admin.webra.top");
        System.out.println(webSite);
    }
    //获取一个字符串
    @Test
    public void testGet() {
        String value = this.redisTemplate.opsForValue().get("key");
        System.out.println(value);
    }
}
