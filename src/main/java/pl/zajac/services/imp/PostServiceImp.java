package pl.zajac.services.imp;

import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.zajac.model.dto.EditPostDto;
import pl.zajac.model.dto.PostDto;
import pl.zajac.model.entities.Comment;
import pl.zajac.model.entities.Post;
import pl.zajac.model.repository.PostRepository;
import pl.zajac.model.security.jwt.GetUserNameFromJwt;
import pl.zajac.services.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImp implements PostService {
    private final int MAX_POSTS_ON_SINGLE_PAGE = 2;
    private PostRepository postRepository;

    @Autowired
    public PostServiceImp(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAllPosts(int page) {
        PageRequest sortedByDate = PageRequest.of(page, MAX_POSTS_ON_SINGLE_PAGE, Sort.by("id"));
        List<Post> posts = new ArrayList<>();
        this.postRepository.findAllPublished(sortedByDate).forEach(posts::add);
        return posts;
    }

    @Override
    public Post getSpecificPost(String title) {
        return this.postRepository.findByTitle(title);
    }

    @Override
    public void addPost(PostDto postDto, String token) {
        Post post = new Post();
        post.setImageUrl(postDto.getUrl());
        post.setBody(postDto.getBody());
        post.setTitle(postDto.getTitle());
        post.setPublicationDate(java.time.LocalDateTime.now());
        post.setAuthorName(GetUserNameFromJwt.getUserName(token));
        post.setPublished(true);
        postRepository.save(post);
    }

    @Override
    public List<Post> findAllPosts() {
        List<Post> allPosts = new ArrayList<>();
        this.postRepository.findAll().forEach(allPosts::add);
        return allPosts;
    }

    @Override
    public void changePostStatus(String id) {
        Long postId = Long.parseLong(id);
        Optional<Post> post = this.postRepository.findById(postId);
        post.get().setPublished(!post.get().isPublished());
        this.postRepository.save(post.get());
    }

    @Override
    public void editPost(EditPostDto editPostDto) {
        Optional<Post> post = this.postRepository.findById(Long.parseLong(editPostDto.getId()));
        post.get().setTitle(editPostDto.getTitle());
        post.get().setBody(editPostDto.getBody());
        post.get().setImageUrl(editPostDto.getUrl());
        this.postRepository.save(post.get());
    }

    @Override
    public void addComment(String content, Long id, String token) {
        Optional<Post> post = this.postRepository.findById(id);
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserName(GetUserNameFromJwt.getUserName(token));
        post.get().getComments().add(comment);
        this.postRepository.save(post.get());
    }
}
