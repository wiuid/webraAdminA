package top.webra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/3/23 17:31
 * @Description: 路径映射
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Value("${file-path.win}")
    private String winPath;

    @Value("${file-path.lin}")
    private String linPath;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name");

        //如果是Windows系统
        if (os.toLowerCase().startsWith("win")) {
            registry.addResourceHandler("/image/*")
                    // /image/**表示在磁盘filePathWindow目录下的所有资源会被解析为以下的路径
                    .addResourceLocations("file:" + winPath);
        } else {  //linux 和mac
            registry.addResourceHandler("/image/*")
                    .addResourceLocations("file:" + linPath) ;
        }
        // 静态资源路径映射
        registry.addResourceHandler("/static/*").addResourceLocations("classpath:/static/");

        // swagger 404 问题
        registry.addResourceHandler("/swagger-ui/*").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        // js 无法访问
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        super.addResourceHandlers(registry);
    }
}
