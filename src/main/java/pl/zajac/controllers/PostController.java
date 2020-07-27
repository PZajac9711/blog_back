package pl.zajac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zajac.model.dto.PostDto;
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
    public List<Post> getAllPosts(@RequestParam(defaultValue = "0") int page) {
        return this.postService.getAllPosts(page);
    }

    @GetMapping(value = "/posts/specific")
    public Post getSpecificPost(@RequestParam String title) {
        return this.postService.getSpecificPost(title);
    }

    @GetMapping(value = "/posts/asd")
    public String asd() {
        return "asd";
    }

    @PostMapping(value = "/posts/add")
    public ResponseEntity<Void> addPost(@RequestBody PostDto postDto, @RequestHeader("Authorization") String token) {
        postService.addPost(postDto, token);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
