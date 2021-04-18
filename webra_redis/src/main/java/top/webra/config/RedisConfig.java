package top.webra.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/4/18 14:07
 * @Description: Redis 配置类
 */
@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //关联连接工厂
        template.setConnectionFactory(factory);
        //为key设置序列化
        template.setKeySerializer(new StringRedisSerializer());
        //为value设置序列化
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
