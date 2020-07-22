package pl.zajac.model.repository;

import org.springframework.data.repository.CrudRepository;
import pl.zajac.model.entities.Post;

public interface PostRepository extends CrudRepository<Post,Long> {
}
