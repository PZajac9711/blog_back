package pl.zajac.services;

import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.model.exceptions.custom.UserRegistrationException;

public interface UserService {
    void createUser(UserRegistrationDto userRegistrationDto) throws UserRegistrationException;
}
