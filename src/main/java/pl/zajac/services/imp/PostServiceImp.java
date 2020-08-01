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
        Post post = new Post.Builder()
                .setImageUrl(postDto.getUrl())
                .setBody(postDto.getBody())
                .setTitle(postDto.getTitle())
                .setPublicationDate(java.time.LocalDateTime.now())
                .setAuthorName(GetUserNameFromJwt.getUserName(token))
                .setPublished(false)
                .build();
        postRepository.save(post);
    }

    @Override
    public List<Post> findAllPosts() {
        List<Post> allPosts = new ArrayList<>();
        this.postRepository.findAll().forEach(allPosts::add);
        return allPosts;
    }

    @Override
    public void changePostStatus(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        if(!post.isPresent()){
            //tutaj bedzie wyjatek
        }
        post.get().setPublished(!post.get().isPublished());
        this.postRepository.save(post.get());
    }

    @Override
    public void editPost(EditPostDto editPostDto) {
        Optional<Post> postOptional = this.postRepository.findById(Long.parseLong(editPostDto.getId()));
        if(!postOptional.isPresent()){
            //tutaj bedzie wyjatek
        }
        Post post = postOptional.get();
        post.setTitle(editPostDto.getTitle());
        post.setBody(editPostDto.getBody());
        post.setImageUrl(editPostDto.getUrl());
        this.postRepository.save(post);
    }

    @Override
    public void addComment(String content, Long id, String token) {
        Optional<Post> post = this.postRepository.findById(id);
        if(!post.isPresent()){
            //tutaj bedzie wyjatek
        }
        Comment comment = new Comment(content, GetUserNameFromJwt.getUserName(token));
        post.get().getComments().add(comment);
        this.postRepository.save(post.get());
    }

    @Override
    public List<PostDto> findPostsByWord(String word) {
        List<PostDto> postDto = new ArrayList<>();
        this.postRepository.findByWord(word).forEach(post -> {
            postDto.add(new PostDto(post.getImageUrl(), post.getTitle(), post.getBody()));
        });
        return postDto;
    }
}
