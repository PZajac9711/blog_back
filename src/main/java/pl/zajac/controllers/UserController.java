package pl.zajac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.services.UserService;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationDto userRegistrationDto){
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    @GetMapping(value = "/test")
    public void asd(@RequestParam int a){
        int b = a/0;
    }
}
