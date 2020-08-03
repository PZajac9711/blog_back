package pl.zajac.model.dto;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "email='" + email + '\'' +
                '}' + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserRegistrationDto that = (UserRegistrationDto) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email);
    }
}
