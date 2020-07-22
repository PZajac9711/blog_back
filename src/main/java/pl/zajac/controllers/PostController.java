package pl.zajac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zajac.model.entities.Post;
import pl.zajac.services.PostService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class PostController {
    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/posts")
    public List<Post> getAllPosts(){
        return this.postService.getAllPosts();
    }
}
