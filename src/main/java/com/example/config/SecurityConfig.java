package com.example.config;

import com.example.security.CustomLoginFilter;
import com.example.security.MyAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable()
                .httpBasic().disable()
                .addFilter(new CustomLoginFilter(authenticationManagerBean())); 
        
        // 拦截规则
        http.authorizeRequests()
                // 放行
                .antMatchers("/html/login.html","/sys/login", "/favicon.ico", "/js/**").permitAll()
                // 角色
                .antMatchers("/html/user/admin.html").hasRole("ADMIN")
                .antMatchers("/html/user/baomi.html").hasRole("BAOMI")
                .antMatchers("/html/user/shenji.html").hasRole("SHENJI")
                // access方法传入SPEL  
                //.antMatchers("").access("hasRole('user')")
                // 所有请求认证后才能访问
                .anyRequest().authenticated(); 

        // 权限不足处理器
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
        
        http.authenticationProvider(authenticationProvider());
        
        http.logout()
                .logoutUrl("sys/logout")
                .logoutSuccessUrl("/html/index.html")
                .invalidateHttpSession(true);

        http.csrf().disable();

    }




}



