package com.example.security;

import com.example.entity.User;
import com.example.util.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    
    private AuthenticationManager authenticationManager;
    
    public CustomLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/sys/login","POST"));
    }
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {
        try {
            User user = mapper.readValue(request.getInputStream(), User.class);
            return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Collections.emptyList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityUser user = (SecurityUser) authResult.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authResult);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = mapper.writeValueAsString(R.ok().put("authentication", user.getUser()));
        response.getWriter().write(json);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = mapper.writeValueAsString(R.error("登录失败" + failed.getLocalizedMessage()));
        response.getWriter().write(json);
    }
}
