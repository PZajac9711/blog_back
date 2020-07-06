package pl.zajac.model.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "profiles")
public class UserProfile implements Serializable {
    @Id
    @Column(name = "user_id")
    private Long userId;

    private String test;
    @OneToOne
    @MapsId
    private User user;

    public Long getUserId() {
        return userId;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
