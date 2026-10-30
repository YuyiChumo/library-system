package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public Result<Boolean> userSignUp(@RequestBody UserRequest request) {
        return userService.signUp(request);
    }

    @PostMapping("/signIn")
    public Result<Boolean> userSignIn(@RequestBody UserRequest request) {
        return userService.signIn(request);
    }
}