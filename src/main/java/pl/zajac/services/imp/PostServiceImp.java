package pl.zajac.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zajac.model.entities.Post;
import pl.zajac.model.repository.PostRepository;
import pl.zajac.services.PostService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImp implements PostService {
    private PostRepository postRepository;

    @Autowired
    public PostServiceImp(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        this.postRepository.findAll().forEach(posts::add);
        return posts;
    }
}
