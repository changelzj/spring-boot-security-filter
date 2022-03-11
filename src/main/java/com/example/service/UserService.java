package com.example.service;

import com.example.entity.User;

public interface UserService {
    User findByUsername(String username);
}
