package com.example.TradingSystem.Server.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "api/v1/user")
    @GetMapping
    public synchronized List<User> getUser() throws InterruptedException {
//        Thread.sleep(5000);
        return List.of(
                new User(1L,
                        "amit",
                        "qweasd")
        );
    }

    @RequestMapping(path = "api/v2/user")
    @GetMapping
    public List<User> getUser2(){
        return userService.getUser2();
    }

    @RequestMapping(path = "api/v2/user11")
    @PostMapping
    public void addNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }
}
