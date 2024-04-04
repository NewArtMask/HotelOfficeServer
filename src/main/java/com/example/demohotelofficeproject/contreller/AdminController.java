package com.example.demohotelofficeproject.contreller;

import com.example.demohotelofficeproject.entity.User;
import com.example.demohotelofficeproject.service.AdminService;
import com.example.demohotelofficeproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("hoteloffice")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;


    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(adminService.getAllUser());
    }
}
