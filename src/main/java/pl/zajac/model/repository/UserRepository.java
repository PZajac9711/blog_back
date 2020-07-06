package pl.zajac.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.zajac.model.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
}
