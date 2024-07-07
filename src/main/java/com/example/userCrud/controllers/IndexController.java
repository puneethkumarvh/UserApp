package com.example.userCrud.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/test")
    public String getTest(){
        return "Hello from get test controller.";
    }
    @GetMapping("/")
    public String getHome(){
        return "Welcome to Home Page !";
    }
    @GetMapping("/app/admin")
    public String getAdmin(){
        return "Welcome Admin";
    }

    @GetMapping("app/dashboard")
    public String getDashboard(){
        return "Welcome to Dashboard!";
    }
}
