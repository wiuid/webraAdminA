package top.webra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.webra.filter.AuthenticationTokenFilter;
import top.webra.hander.*;
import top.webra.provider.SelfAuthenticationProvider;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:16
 * @Description: Security配置
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SelfAuthenticationProvider selfAuthenticationProvider;
    @Autowired
    private AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint;
    @Autowired
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    @Autowired
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    @Autowired
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Autowired
    private AuthenticationTokenFilter authenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(selfAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf
        http
                .csrf().disable()
                // 没有登录访问资源的话，返回 code:0
                .httpBasic().authenticationEntryPoint(ajaxAuthenticationEntryPoint);

        http
                .authorizeRequests()
                // 对preflight放行
//                .antMatchers("/index").permitAll()
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 通用页面及静态资源
                .antMatchers("/index",
                        "/login",
                        "/static/**",
                        "/image/*").permitAll()
                // swagger
                .antMatchers("/swagger-ui/**",
                        "/swagger-resources/**",
                        "/profile/**",
                        "/v3/**").anonymous()
                .anyRequest()
                // 其他 url 需要身份认证
                .authenticated();

        http
                //开启登录
                .formLogin()
//                .loginProcessingUrl("/login")
                // 登录成功
                .successHandler(ajaxAuthenticationSuccessHandler)
                // 登录失败
                .failureHandler(ajaxAuthenticationFailureHandler)
                .permitAll();

        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .permitAll();
        // token验证拦截器
        http .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
