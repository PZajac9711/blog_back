package pl.zajac.services;

import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.model.exceptions.custom.InvalidUserData;
import pl.zajac.model.exceptions.custom.UserRegistrationException;

import java.util.Optional;

public interface UserService {
    void createUser(UserRegistrationDto userRegistrationDto) throws UserRegistrationException;
    String checkUserDetails(UserDto userDto) throws InvalidUserData;
}
