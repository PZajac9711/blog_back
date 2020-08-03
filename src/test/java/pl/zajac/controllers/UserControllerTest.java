package pl.zajac.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajac.model.dto.UserDto;
import pl.zajac.model.dto.UserRegistrationDto;
import pl.zajac.model.entities.User;
import pl.zajac.model.exceptions.custom.InvalidUserData;
import pl.zajac.model.exceptions.custom.UserRegistrationException;
import pl.zajac.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    UserService userService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateNewUser() throws Exception {
        //given
        UserRegistrationDto request = new UserRegistrationDto("ala123", "ala123", "ala123@gmail.com");

        //when
        mockMvc.perform(post("/api/registration")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());

        //then
        verify(userService, times(1)).createUser(request);
    }

    @Test
    public void shouldNotCreateNewUserBecauseLoginIsTaken() throws Exception {
        //given
        UserRegistrationDto request = new UserRegistrationDto("admin", "admin", "admin@gmail.com");
        doThrow(new UserRegistrationException("User already exist, login and email need to be unique"))
                .when(userService).createUser(request);
        //when
        mockMvc.perform(post("/api/registration")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debugMessage").value("User already exist, login and email need to be unique"));

        //then
        verify(userService, times(1)).createUser(request);
    }

    @Test
    public void shouldNotCreateNewUserBecauseLoginIsBad() throws Exception {
        //given
        UserRegistrationDto request = new UserRegistrationDto("admin!", "admin", "admin@gmail.com");
        doThrow(new UserRegistrationException("Wrong login"))
                .when(userService).createUser(request);
        //when
        mockMvc.perform(post("/api/registration")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debugMessage").value("Wrong login"));

        //then
        verify(userService, times(1)).createUser(request);
    }

    @Test
    public void shouldNotCreateNewUserBecauseEmailIsBad() throws Exception {
        //given
        UserRegistrationDto request = new UserRegistrationDto("admin!", "admin", "admin@gmail.com");
        doThrow(new UserRegistrationException("Wrong email"))
                .when(userService).createUser(request);
        //when
        mockMvc.perform(post("/api/registration")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debugMessage").value("Wrong email"));

        //then
        verify(userService, times(1)).createUser(request);
    }

    @Test
    public void shouldReturnTokenAllDataIsCorrect() throws Exception {
        //given
        UserDto request = new UserDto("admin", "admin");
        when(userService.checkUserDetails(request)).thenReturn("token");
        //when
        mockMvc.perform(post("/api/authenticate")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
        //then
        verify(userService,times(1)).checkUserDetails(request);
    }
    @Test
    public void shouldNotReturnTokenLoginIsIncorrect() throws Exception {
        //given
        UserDto request = new UserDto("admin", "admin");
        when(userService.checkUserDetails(request)).thenThrow(new InvalidUserData("No user with this login"));
        //when
        mockMvc.perform(post("/api/authenticate")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debugMessage").value("No user with this login"));
        //then
        verify(userService,times(1)).checkUserDetails(request);
    }
    @Test
    public void shouldNotReturnTokenPasswordIsIncorrect() throws Exception {
        //given
        UserDto request = new UserDto("admin", "admin");
        when(userService.checkUserDetails(request)).thenThrow(new InvalidUserData("Wrong password"));
        //when
        mockMvc.perform(post("/api/authenticate")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debugMessage").value("Wrong password"));
        //then
        verify(userService,times(1)).checkUserDetails(request);
    }
}
