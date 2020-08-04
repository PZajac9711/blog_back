package pl.zajac.services;

import pl.zajac.model.dto.PasswordChangeRequest;
import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;

public interface UserService {
    void createUser(UserRegistrationDto userRegistrationDto);
    String checkUserDetails(UserDto userDto);

    void changePassword(String token, PasswordChangeRequest passwordChangeRequest);
}
