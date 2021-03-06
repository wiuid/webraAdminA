package top.webra.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.webra.utils.RedisUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/4/7 21:04
 * @Description: 启动完全启动前的一些准备
 */
@Component
public class CustomRunner implements ApplicationRunner {
    @Value("${file-path.win}")
    private String winPath;
    @Value("${file-path.lin}")
    private String linPath;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public void run(ApplicationArguments args) {
        // 判断当前系统是否是Windows，并赋值路径
        String path = System.getProperty("os.name").toLowerCase().startsWith("win")?winPath:linPath;
        File file = new File(path);
        // 如果路径不存在，则创建路径
        if (!file.exists()){
            file.mkdirs();
        }
        // 最新公告
        String newInform = "newInform";
        if (!redisUtil.hasKey(newInform)){
            redisUtil.set(newInform, "0");
        }

    }
}
