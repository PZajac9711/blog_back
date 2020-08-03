package pl.zajac.services;

import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;

public interface UserService {
    void createUser(UserRegistrationDto userRegistrationDto);
    String checkUserDetails(UserDto userDto);
}
