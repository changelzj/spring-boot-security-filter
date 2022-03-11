package com.example.service;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    
    @Override
    public User findByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
    
    
    
}



