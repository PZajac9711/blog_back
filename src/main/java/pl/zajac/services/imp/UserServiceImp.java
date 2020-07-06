package pl.zajac.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zajac.model.entities.User;
import pl.zajac.model.entities.UserProfile;
import pl.zajac.model.repository.UserRepository;
import pl.zajac.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        UserProfile userProfile = new UserProfile();
        userProfile.setTest("asddd");
        user.setUserProfile(userProfile);
        this.userRepository.save(user);
    }

    @Override
    public void save2() {
        Optional<User> user1 = this.userRepository.findById(1L);
        user1.get().setLogin("hohohoh");
        user1.get().getUserProfile().setTest("abababa");
        System.out.println(user1.get().getId());
        this.userRepository.save(user1.get());
    }

    @Override
    public void save3() {
        Optional<User> user1 = this.userRepository.findById(1L);
        this.userRepository.delete(user1.get());
    }

    private boolean validLogin(String login){
        return false;
    }
    private boolean validPassword(String password){
        return false;
    }
    private boolean validEmail(String email){
        return false;
    }
    private boolean validAll(){
        return false;
    }
}
