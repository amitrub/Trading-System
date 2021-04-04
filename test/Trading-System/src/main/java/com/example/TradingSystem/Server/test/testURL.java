package com.example.TradingSystem.Server.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class testURL {

    @RequestMapping(path = "api/test1")
    @GetMapping
    public List<User> getUser() throws InterruptedException {
        return List.of(
                new User(1L,
                        "amit",
                        "qweasd")
        );
    }
}
