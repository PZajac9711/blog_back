package pl.zajac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zajac.services.UserService;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/test")
    public void asd(){
        this.userService.save();
    }
    @GetMapping(value = "/test2")
    public void asdd(){
        this.userService.save2();
    }
    @GetMapping(value = "/test3")
    public void asddd(){
        this.userService.save3();
    }
}
