package pl.zajac.model.dto;

public class UserRegistrationDto extends UserDto{
    private String email;

    public UserRegistrationDto(String login, String password, String email) {
        super(login, password);
        this.email = email;
    }

    public UserRegistrationDto() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
