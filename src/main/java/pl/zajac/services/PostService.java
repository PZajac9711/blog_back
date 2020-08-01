package pl.zajac.services;

import pl.zajac.model.dto.EditPostDto;
import pl.zajac.model.dto.PostDto;
import pl.zajac.model.entities.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts(int page);
    Post getSpecificPost(String title);
    void addPost(PostDto postDto,String token);
    List<Post> findAllPosts();
    void changePostStatus(Long id);
    void editPost(EditPostDto editPostDto);
    void addComment(String content, Long id, String token);
    List<PostDto> findPostsByWord(String word);
}
