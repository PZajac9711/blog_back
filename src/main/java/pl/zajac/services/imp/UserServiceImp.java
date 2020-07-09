package pl.zajac.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.zajac.model.coverter.Converter;
import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.model.entities.User;
import pl.zajac.model.exceptions.custom.UserRegistrationException;
import pl.zajac.model.repository.UserRepository;
import pl.zajac.services.UserService;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImp implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private Converter<User,UserRegistrationDto> registrationToUser;
    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          @Qualifier("registerToUser") Converter<User, UserRegistrationDto> registrationToUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registrationToUser = registrationToUser;
    }

    @Override
    public void createUser(UserRegistrationDto userRegistrationDto) throws UserRegistrationException{
        //We need list because there's scenario where one user can have equal login and another equal email and we get 2 rows from db.
        List<User> user = this.userRepository.findUserByLoginOrEmail(
                userRegistrationDto.getLogin().toLowerCase(),
                userRegistrationDto.getEmail().toLowerCase()
        );
        if(user.size() > 0){
            throw new UserRegistrationException("User already exist, login and email need to be unique");
        }
        if(checkAll(userRegistrationDto)){
            userRegistrationDto.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
            this.userRepository.save(this.registrationToUser.convert(userRegistrationDto));
        }
    }
    private boolean checkLogin(String login) throws UserRegistrationException {
        String regex = "^[a-zA-Z0-9]{3,}$";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(login).matches()){
            throw new UserRegistrationException("Wrong login");
        }
        return true;
    }
    private boolean checkPassword(String password) throws UserRegistrationException{
        return true;
    }
    private boolean checkEmail(String email) throws UserRegistrationException{
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(email).matches()){
            throw new UserRegistrationException("Wrong email");
        }
        return true;
    }
    private boolean checkAll(UserRegistrationDto userRegistrationDto) throws UserRegistrationException{
        String password = userRegistrationDto.getPassword();
        String login = userRegistrationDto.getLogin();
        String email = userRegistrationDto.getEmail();
        return checkLogin(login) && checkPassword(password) && checkEmail(email);
    }
}
