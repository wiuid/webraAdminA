package top.webra.adminA;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.webra.bean.ResponseBean;
import top.webra.mapper.DepartmentMapper;
import top.webra.pojo.Auth;
import top.webra.service.impl.AuthServiceImpl;
import top.webra.service.impl.DepartmentServiceImpl;
import top.webra.service.impl.InformServiceImpl;
import top.webra.service.impl.UserServiceImpl;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-12 11:55
 * @Description: Service 测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    private InformServiceImpl informService;
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void informTest(){
    }
    @Test
    public void userTest(){
        ResponseBean user = userService.selectUser(1);
        System.out.println(user);
    }

    @Autowired
    private ResponseBean responseBean;
    @Test
    public void resTest(){
        responseBean.getData().put("user","my name is hello word");
        System.out.println(responseBean);
    }

    @Autowired
    private DepartmentServiceImpl departmentService;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private AuthServiceImpl authService;
    @Test
    public void authTest(){
        ResponseBean tree = authService.getTree();
        String s = JSON.toJSONString(tree.getData().get("authTree"));
        System.out.println(s);
    }
}
