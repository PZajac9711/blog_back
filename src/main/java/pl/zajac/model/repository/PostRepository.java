package pl.zajac.model.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.zajac.model.entities.Post;

import java.awt.print.Pageable;
import java.util.List;

public interface PostRepository extends CrudRepository<Post,Long>, PagingAndSortingRepository<Post,Long> {
    Post findByTitle(String title);
    @Query("SELECT u FROM Post u WHERE u.isPublished=true")
    List<Post> findAllPublished(PageRequest pageRequest);
}
