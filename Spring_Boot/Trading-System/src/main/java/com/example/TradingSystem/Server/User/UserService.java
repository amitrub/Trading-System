package com.example.TradingSystem.Server.User;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserService {

    private List<User> users = new LinkedList<>();

    public List<User> getUser(){
        return List.of(
                new User(1L,
                        "amit",
                        "qweasd")
        );
//        return users;
    }

    public List<User> getUser2(){
        return List.of(
                new User(2L,
                        "amit2",
                        "qweasd")
        );
    }

    public void addNewUser(User user) {
        System.out.println(user);
        users.add(user);
    }
}
