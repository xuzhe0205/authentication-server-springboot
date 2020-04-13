package com.posthem.springboot.controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {

    @GetMapping("/")
    public String testOauth() {
        return "Hello OAuth";
    }
    @GetMapping("/restricted")
    public String restricted() {
        return "Log in to google to see this ";
    }
    @RequestMapping("user")
    public Principal user (Principal principal){
        return principal;
    }
}
