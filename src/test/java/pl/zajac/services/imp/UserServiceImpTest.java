package pl.zajac.services.imp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.zajac.model.coverter.Converter;
import pl.zajac.model.dto.PasswordChangeRequest;
import pl.zajac.model.dto.PostDto;
import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.model.entities.Post;
import pl.zajac.model.entities.User;
import pl.zajac.model.exceptions.custom.InvalidUserData;
import pl.zajac.model.exceptions.custom.UserRegistrationException;
import pl.zajac.model.repository.UserRepository;
import pl.zajac.model.security.jwt.ReadToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImpTest {
    @InjectMocks
    UserServiceImp userServiceImp;

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    Converter<User, UserRegistrationDto> registrationToUser;
    @Mock
    ReadToken readToken;

    @Test(expected = UserRegistrationException.class)
    public void failCreateUserTest(){
        //given
        List<User> userList = new ArrayList<>();
        userList.add(new User.Builder()
                .setLogin("kicaj")
                .setRole("user")
                .setPassword("kicaj").setEmail("kicaj@gmail.com")
                .build());
        when(userRepository.findUserByLoginOrEmail("kicaj", "kicaj@gmail.com")).thenReturn(userList);
        //when
        userServiceImp.createUser(new UserRegistrationDto("kicaj", "kicaj", "kicaj@gmail.com"));
        //then
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findUserByLoginOrEmail("kicaj", "kicaj@gmail.com");
    }

    @Test
    public void successCreateUserTest(){
        //given
        List<User> userList = new ArrayList<>();
        when(userRepository.findUserByLoginOrEmail("login", "user@gmail.com")).thenReturn(userList);
        when(passwordEncoder.encode("password")).thenReturn("#assword");
        //when
        userServiceImp.createUser(new UserRegistrationDto("login", "password", "user@gmail.com"));
        //then
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).findUserByLoginOrEmail("login", "user@gmail.com");
    }

    @Test(expected = InvalidUserData.class)
    public void failCheckUserDetails(){
        //given
        when(userRepository.findUserByLogin("login")).thenReturn(Optional.empty());
        //when
        userServiceImp.checkUserDetails(new UserDto("login", "password"));
        //then
        verify(userRepository, times(1)).findUserByLogin("login");
        verify(passwordEncoder, times(0)).matches(any(), any());
    }

    @Test
    public void successCheckUserDetails(){
        //given
        User user = new User.Builder()
                .setEmail("user@gmail.com")
                .setPassword("password")
                .setLogin("login")
                .setRole("user")
                .build();
        when(userRepository.findUserByLogin("login")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        //when
        String token = userServiceImp.checkUserDetails(new UserDto("Login", "password"));
        //then
        verify(passwordEncoder, times(1)).matches("password", "password");
        verify(userRepository, times(1)).findUserByLogin("login");
        long dotsInToken = token.chars().filter(ch -> ch == '.').count();
        assertEquals(2, dotsInToken);
    }

    @Test(expected = UserRegistrationException.class)
    public void wrongEmailTest() throws UserRegistrationException {
        userServiceImp.createUser(new UserRegistrationDto("login", "password", "usergmail.com"));
    }

    @Test(expected = UserRegistrationException.class)
    public void wrongLoginTest() throws UserRegistrationException {
        userServiceImp.createUser(new UserRegistrationDto("!login", "password", "user@gmail.com"));
    }

    @Test(expected = InvalidUserData.class)
    public void checkUserDetailsPasswordDidntMatchAndShouldReturnException(){
        //given
        UserDto userDto = new UserDto("admin","admin");
        //when
        when(userRepository.findUserByLogin("admin")).thenReturn(Optional.of(new User()));
        String token = userServiceImp.checkUserDetails(userDto);
        //then
        assertNull(token);
    }

    @Test
    public void changePasswordTestShouldThrowExceptionBecauseNewAndOldPasswordAreTheSame(){
        //given
        User user = new User();
        user.setPassword("same");
        String token = "token";
        String expectedMessage = "Password need to be difference";
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("same","same");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(readToken.getLogin(token)).thenReturn("login");
        when(userRepository.findUserByLogin("login")).thenReturn(Optional.of(user));
        //when
        Exception exception = assertThrows(InvalidUserData.class, () -> {
            userServiceImp.changePassword(token,passwordChangeRequest);
        });
        //then
        String actualMessage = exception.getMessage();
        verify(userRepository,times(0)).save(any());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void changePasswordTestShouldThrownInvalidUserDataExceptionBecausePasswordDidntMatch(){
        //given
        String token = "token";
        String expectedMessage = "Password didnt match";
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("new","old");

        //when
        when(readToken.getLogin(token)).thenReturn("login");
        when(userRepository.findUserByLogin("login")).thenReturn(Optional.of(new User()));

        Exception exception = assertThrows(InvalidUserData.class, () -> {
            userServiceImp.changePassword(token,passwordChangeRequest);
        });
        //then
        verify(userRepository, times(0)).save(any());
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void changePasswordTestShouldReturnInvalidUserDataExceptionBecauseThereIsNoUserWithThisLogin(){
        //given
        String token = "token";
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("new","old");
        String expectedMessage = "There's no user with this login";

        //when
        when(readToken.getLogin(token)).thenReturn("login");
        when(userRepository.findUserByLogin("login")).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidUserData.class, () -> {
            userServiceImp.changePassword(token,passwordChangeRequest);
        });

        //then
        verify(passwordEncoder,times(0)).matches(anyString(),anyString());
        verify(passwordEncoder,times(0)).encode(anyString());
        verify(userRepository,times(0)).save(any());
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void changePasswordTestShouldBeSuccessfullyAllDataIsCorrect(){
        //given
        String password = "password";
        User user = new User();
        user.setPassword(password);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        String token = "token";
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("new","password");

        //when
        when(readToken.getLogin(token)).thenReturn("login");
        when(userRepository.findUserByLogin("login")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(passwordChangeRequest.getNewPassword())).thenReturn("hello");

        userServiceImp.changePassword(token,passwordChangeRequest);

        //then
        verify(userRepository,times(1)).save(user);
        verify(passwordEncoder,times(1)).encode(passwordChangeRequest.getNewPassword());
    }
}
