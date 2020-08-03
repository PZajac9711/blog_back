package pl.zajac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zajac.model.dto.TokenDto;
import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;
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
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationDto userRegistrationDto){
        this.userService.createUser(userRegistrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping(value = "/authenticate")
    public ResponseEntity<TokenDto> signIn(@RequestBody UserDto userDto){
        TokenDto token = new TokenDto(this.userService.checkUserDetails(userDto));
        return new ResponseEntity<>(token,HttpStatus.OK);
    }
}
