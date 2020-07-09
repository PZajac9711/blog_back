package pl.zajac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.model.exceptions.custom.UserRegistrationException;
import pl.zajac.services.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) throws UserRegistrationException {
        this.userService.createUser(userRegistrationDto);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    @PostMapping(value = "/login")
    public ResponseEntity<String> signIn(@RequestBody UserDto userDto){
        String token = "token";
        return new ResponseEntity<>(token,HttpStatus.OK);
    }
}
