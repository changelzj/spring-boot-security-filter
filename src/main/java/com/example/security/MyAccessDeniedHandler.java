package com.example.security;

import com.example.util.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException ex) 
            throws IOException, ServletException {
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = objectMapper.writeValueAsString(R.error("当前角色无权限"));
        resp.getWriter().write(json);
    }
}
