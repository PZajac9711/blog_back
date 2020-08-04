package pl.zajac.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.zajac.model.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    List<User> findUserByLoginOrEmail(String login, String email);
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByEmail(String email);
}
