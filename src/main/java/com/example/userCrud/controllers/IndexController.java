package com.example.userCrud.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/test")
    public String getTest(){
        return "Hello from get test controller.";
    }
}
