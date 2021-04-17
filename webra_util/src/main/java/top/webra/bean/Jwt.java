package top.webra.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author webra
 * @date 2021年4月17日
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.config")
public class Jwt {
    private String key;
    private Long ttl;
}
