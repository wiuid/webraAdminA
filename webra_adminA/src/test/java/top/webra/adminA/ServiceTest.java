package top.webra.adminA;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.webra.bean.ResponseBean;
import top.webra.service.impl.UserServiceImpl;


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
    private UserServiceImpl userService;

    @Test
    public void informTest(){
    }
    @Test
    public void userTest(){
        System.out.println(userService.selectUser(1));
    }

    @Autowired
    private ResponseBean responseBean;
    @Test
    public void resTest(){
        responseBean.getData().put("user","my name is hello word");
        System.out.println(responseBean);
    }
}
