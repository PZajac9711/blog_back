package pl.zajac.services;

import pl.zajac.model.dto.PasswordChangeRequest;
import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;

import javax.mail.MessagingException;

public interface UserService {
    void createUser(UserRegistrationDto userRegistrationDto);
    String checkUserDetails(UserDto userDto);

    void changePassword(String token, PasswordChangeRequest passwordChangeRequest);

    void resetPassword(String token,String password);
}
