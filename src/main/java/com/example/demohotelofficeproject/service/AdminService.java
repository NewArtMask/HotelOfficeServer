package com.example.demohotelofficeproject.service;

import com.example.demohotelofficeproject.entity.User;
import com.example.demohotelofficeproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
