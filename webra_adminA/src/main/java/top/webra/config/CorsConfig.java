package top.webra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * *********
 * @author webra
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter () {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOrigin("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
//        cors.addExposedHeader("*");

        UrlBasedCorsConfigurationSource ubccs = new UrlBasedCorsConfigurationSource();
        ubccs.registerCorsConfiguration("/**", cors);
        return new CorsFilter(ubccs);
    }
}

//public class CorsConfig extends WebMvcConfigurerAdapter {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedOrigins("*")
//            .allowCredentials(true)
//            .allowedMethods("*")
//            .allowedHeaders("*")
//            .maxAge(3600);
//    }
//}