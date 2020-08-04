package pl.zajac.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.zajac.model.coverter.Converter;
import pl.zajac.model.dto.PasswordChangeRequest;
import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.model.entities.User;
import pl.zajac.model.exceptions.custom.InvalidUserData;
import pl.zajac.model.exceptions.custom.UserRegistrationException;
import pl.zajac.model.repository.UserRepository;
import pl.zajac.model.security.jwt.JwtGenerate;
import pl.zajac.model.security.jwt.ReadToken;
import pl.zajac.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private Converter<User, UserRegistrationDto> registrationToUser;
    private ReadToken readToken;

    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          @Qualifier("registerToUser") Converter<User, UserRegistrationDto> registrationToUser, ReadToken readToken) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registrationToUser = registrationToUser;
        this.readToken = readToken;
    }

    @Override
    public void createUser(UserRegistrationDto userRegistrationDto){
        //We need list because there's scenario where one user can have equal login and another equal email and we get 2 rows from db.
        List<User> user = this.userRepository.findUserByLoginOrEmail(
                userRegistrationDto.getLogin().toLowerCase(),
                userRegistrationDto.getEmail().toLowerCase()
        );
        if (user.size() > 0) {
            throw new UserRegistrationException("User already exist, login and email need to be unique");
        }
        if (checkAll(userRegistrationDto)) {
            userRegistrationDto.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
            this.userRepository.save(this.registrationToUser.convert(userRegistrationDto));
        }
    }

    @Override
    public String checkUserDetails(UserDto userDto){
        Optional<User> user = userRepository.findUserByLogin(userDto.getLogin().toLowerCase());
        if (!user.isPresent()) {
            throw new InvalidUserData("No user with this login");
        }
        if (!passwordEncoder.matches(userDto.getPassword(),user.get().getPassword())) {
            throw new InvalidUserData("Wrong password");
        }
        JwtGenerate jwtGenerate = new JwtGenerate();
        return jwtGenerate.generateToken(userDto.getLogin(),user.get().getRole());
    }

    @Override
    public void changePassword(String token, PasswordChangeRequest passwordChangeRequest) {
        String login = readToken.getLogin(token);
        Optional<User> user = userRepository.findUserByLogin(login);
        if(user.isEmpty()){
            throw new InvalidUserData("There's no user with this login");
        }
        if(!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.get().getPassword())){
            throw new InvalidUserData("Password didnt match");
        }
        if(passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getOldPassword())){
            throw new InvalidUserData("Password need to be difference");
        }
        user.get().setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user.get());
    }

    @Override
    public void resetPassword(String token, String password) {
        //To Do handle moment where token expired \/ and generate exception
        //Signature exception
        String email = readToken.getEmailFromResetToken(token);
        Optional<User> user = userRepository.findUserByEmail(email);
        if(user.isEmpty()){
            throw new InvalidUserData("There's no user with this email");
        }
        user.get().setPassword(passwordEncoder.encode(password));
        userRepository.save(user.get());
    }


    private boolean checkLogin(String login){
        String regex = "^[a-zA-Z0-9]{3,}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(login).matches()) {
            throw new UserRegistrationException("Wrong login");
        }
        return true;
    }

    private boolean checkPassword(String password) throws UserRegistrationException {
        return true;
    }

    private boolean checkEmail(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches()) {
            throw new UserRegistrationException("Wrong email");
        }
        return true;
    }

    private boolean checkAll(UserRegistrationDto userRegistrationDto){
        String password = userRegistrationDto.getPassword();
        String login = userRegistrationDto.getLogin();
        String email = userRegistrationDto.getEmail();
        return checkLogin(login) && checkPassword(password) && checkEmail(email);
    }
}
