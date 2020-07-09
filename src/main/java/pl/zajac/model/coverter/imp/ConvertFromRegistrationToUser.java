package pl.zajac.model.coverter.imp;

import org.springframework.stereotype.Component;
import pl.zajac.model.coverter.Converter;
import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.model.entities.User;
import pl.zajac.model.entities.UserProfile;

@Component(value = "registerToUser")
public class ConvertFromRegistrationToUser implements Converter<User, UserRegistrationDto> {
    @Override
    public User convert(UserRegistrationDto from) {
        User user = new User();
        user.setLogin(from.getLogin().toLowerCase());
        user.setPassword(from.getPassword());
        user.setEmail(from.getEmail().toLowerCase());
        user.setRole("user");
        UserProfile userProfile = new UserProfile();
        userProfile.setTest("Example profile text");
        user.setUserProfile(userProfile);
        return user;
    }
}
