package com.spring.security;

import com.spring.CustomAnnotations.Admin;
import com.spring.CustomAnnotations.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public Userinfo registerUser(@RequestBody Userinfo user) {
        System.out.println(user.toString());
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Userinfo user) {
        return userService.verify(user);
    }

    @Admin
    @GetMapping("/admin")
    public String WelcometoAdmin() {
        return "Welcome Admin";
    }

    @User
    @GetMapping("/user")
    public String WelcometoUser() {
        return "Welcome User";
    }
}