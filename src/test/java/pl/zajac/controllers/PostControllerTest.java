package pl.zajac.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajac.model.dto.EditPostDto;
import pl.zajac.model.dto.PostDto;
import pl.zajac.model.entities.Comment;
import pl.zajac.model.entities.Post;
import pl.zajac.model.exceptions.custom.PostNotFoundException;
import pl.zajac.model.security.jwt.JwtGenerate;
import pl.zajac.services.PostService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {
    private Post post = new Post();
    private JwtGenerate jwtGenerate = new JwtGenerate();
    private ObjectMapper mapper = new ObjectMapper();
    private final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpbiIsInJvbGVzIjoidXNlciIsImxvZ2luIjoia2ljYWoxIiwiaWF0IjoxNTk1NjY5MDk4LCJleHAiOjE1OTU2NzAwOTh9.ttelADhEN01n7lrGWeL-7s56oYhXnju1mKphYuTgSWY";
    private final String INVALID_TOKEN = "eyJhbGI1NiJ9.eyJzdWIiOjoxNTk1NjY5MDk4LCE1OTU2NzAwOTh9.ttelADhEN01n-7s56KphYuTgSWY";
    private final String ADMIN_TOKEN = jwtGenerate.generateToken("admin", "admin");
    private final String USER_TOKEN = jwtGenerate.generateToken("user", "user");
    @MockBean
    private PostService postService;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setPost() {
        this.post.setId(1L);
        this.post.setImageUrl("url");
        this.post.setBody("body");
        this.post.setTitle("title");
        this.post.setPublished(true);
        this.post.setAuthorName("admin");
        this.post.setComments(new ArrayList<Comment>());
        this.post.setPublicationDate(java.time.LocalDateTime.now());
    }

    @Test
    public void shouldGetFirstPage() throws Exception {

        //given
        int page = 1;
        List<Post> postList = new ArrayList<>(
                Arrays.asList(post, post)
        );
        //when
        when(postService.getAllPosts(page)).thenReturn(postList);
        mockMvc.perform(get("/api/posts")
                .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].body").value("body"));
    }

    @Test
    public void getSpecificPostTestWherePostExist() throws Exception {
        //given
        String request = "title1";
        //when
        when(postService.getSpecificPost(request)).thenReturn(this.post);
        mockMvc.perform(get("/api/posts/specific")
                .param("title", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(this.post.getBody()));
        //then
        verify(postService, times(1)).getSpecificPost(request);
    }

    @Test
    public void getSpecificPostTestWherePostNotExistShouldReturnPostNotFoundException() throws Exception {
        //given
        String request = "post";
        //when
        when(postService.getSpecificPost(request)).thenThrow(new PostNotFoundException("Oops seems like there's no post with this title"));
        mockMvc.perform(get("/api/posts/specific")
                .param("title", request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debugMessage").value("Oops seems like there's no post with this title"));
        //then
        verify(postService, times(1)).getSpecificPost(request);
    }

    @Test
    public void addPostTestShouldBeSuccessfullyBecauseTokenIsCorrectAndUserIsAdmin() throws Exception {
        //given
        PostDto postDto = new PostDto("url", "title", "body");
        //when
        doNothing().when(postService).addPost(postDto, ADMIN_TOKEN);
        mockMvc.perform(post("/api/posts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(postDto))
                .header("authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isCreated());
        //then
        verify(postService, times(1)).addPost(postDto, "Bearer " + ADMIN_TOKEN);
    }

    @Test
    public void addPostTestShouldFailBecauseTokenContainUserWitchAreNotAdmin() throws Exception {
        //given
        PostDto postDto = new PostDto("url", "title", "body");
        //when
        doNothing().when(postService).addPost(postDto, USER_TOKEN);
        mockMvc.perform(post("/api/posts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(postDto))
                .header("authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isUnauthorized());
        //then
        verify(postService, times(0)).addPost(any(), any());
    }

    @Test
    public void addPostTestShouldFailBecauseTokenIsExpired() throws Exception {
        //given
        PostDto postDto = new PostDto("url", "title", "body");
        //when
        doNothing().when(postService).addPost(postDto, EXPIRED_TOKEN);
        mockMvc.perform(post("/api/posts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(postDto))
                .header("authorization", "Bearer " + EXPIRED_TOKEN))
                .andExpect(status().isUnauthorized());
        //then
        verify(postService, times(0)).addPost(any(), any());
    }

    @Test
    public void addPostTestShouldFailBecauseTokenIsIncorrect() throws Exception {
        //given
        PostDto postDto = new PostDto("url", "title", "body");
        //when
        doNothing().when(postService).addPost(postDto, INVALID_TOKEN);
        mockMvc.perform(post("/api/posts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(postDto))
                .header("authorization", "Bearer " + INVALID_TOKEN))
                .andExpect(status().isUnauthorized());
        //then
        verify(postService, times(0)).addPost(any(), any());
    }

    @Test
    public void findAllTestShouldFailBecauseTokenIsIncorrect() throws Exception {
        //given
        List<Post> database = new ArrayList<>(Arrays.asList(
                this.post, this.post, this.post
        ));
        when(postService.findAllPosts()).thenReturn(database);
        //when
        mockMvc.perform(get("/api/posts/findAll")
                .header("authorization", "Bearer " + INVALID_TOKEN))
                .andExpect(status().isUnauthorized());
        //then
        verify(postService, times(0)).findAllPosts();
    }

    @Test
    public void findAllTestShouldFailBecauseTokenIsExpired() throws Exception {
        //given
        List<Post> database = new ArrayList<>(Arrays.asList(
                this.post, this.post, this.post
        ));
        when(postService.findAllPosts()).thenReturn(database);
        //when
        mockMvc.perform(get("/api/posts/findAll")
                .header("authorization", "Bearer " + EXPIRED_TOKEN))
                .andExpect(status().isUnauthorized());
        //then
        verify(postService, times(0)).findAllPosts();
    }

    @Test
    public void findAllTestShouldFailBecauseTokenContainUserWhoAreNotAdmin() throws Exception {
        //given
        List<Post> database = new ArrayList<>(Arrays.asList(
                this.post, this.post, this.post
        ));
        when(postService.findAllPosts()).thenReturn(database);
        //when
        mockMvc.perform(get("/api/posts/findAll")
                .header("authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isUnauthorized());
        //then
        verify(postService, times(0)).findAllPosts();
    }

    @Test
    public void findAllTestShouldBeSuccessfullyButThrowPostNotFoundException() throws Exception {
        //given
        when(postService.findAllPosts()).thenThrow(new PostNotFoundException("Seems like you got zero posts in database"));
        //when
        mockMvc.perform(get("/api/posts/findAll")
                .header("authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debugMessage").value("Seems like you got zero posts in database"));
        //then
        verify(postService, times(1)).findAllPosts();
    }

    @Test
    public void findAllTestShouldBeSuccessfully() throws Exception {
        //given
        List<Post> database = new ArrayList<>(Arrays.asList(
                this.post, this.post, this.post
        ));
        when(postService.findAllPosts()).thenReturn(database);
        //when
        mockMvc.perform(get("/api/posts/findAll")
                .header("authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        //then
        verify(postService, times(1)).findAllPosts();
    }

    @Test
    public void changePostStatusTestShouldBeSuccessfully() throws Exception {
        //given
        long id = 2L;
        doNothing().when(postService).changePostStatus(id);
        //when
        mockMvc.perform(get("/api/posts/changeStatus")
                .param("id", String.valueOf(id))
                .header("authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        //then
        verify(postService, times(1)).changePostStatus(id);
    }

    @Test
    public void changePostStatusTestShouldFailBecauseTokenIsExpired() throws Exception {
        //given
        long id = 2L;
        doNothing().when(postService).changePostStatus(id);
        //when
        mockMvc.perform(get("/api/posts/changeStatus")
                .param("id", String.valueOf(id))
                .header("authorization", "Bearer " + EXPIRED_TOKEN))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
        //then
        verify(postService, times(0)).changePostStatus(any());
    }

    @Test
    public void changePostStatusTestShouldFailBecauseTokenIsNotAdmin() throws Exception {
        //given
        long id = 2L;
        doNothing().when(postService).changePostStatus(id);
        //when
        mockMvc.perform(get("/api/posts/changeStatus")
                .param("id", String.valueOf(id))
                .header("authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
        //then
        verify(postService, times(0)).changePostStatus(any());
    }

    @Test
    public void editPostTestShouldBeSuccessfully() throws Exception {
        //given
        EditPostDto editPostDto = new EditPostDto("url", "title", "body", "2");
        doNothing().when(postService).editPost(editPostDto);
        //when
        mockMvc.perform(post("/api/posts/editPost")
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + ADMIN_TOKEN)
                .content(mapper.writeValueAsString(editPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        //then
        verify(postService, times(1)).editPost(editPostDto);
    }

    @Test
    public void editPostTestShouldFailBecauseWrongRole() throws Exception {
        //given
        EditPostDto editPostDto = new EditPostDto("url", "title", "body", "2");
        doNothing().when(postService).editPost(editPostDto);
        //when
        mockMvc.perform(post("/api/posts/editPost")
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + USER_TOKEN)
                .content(mapper.writeValueAsString(editPostDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
        //then
        verify(postService, times(0)).editPost(editPostDto);
    }

    @Test
    public void addCommentTestShouldBeSuccessfully() throws Exception {
        //given
        String comment = "hello";
        Long id = 2L;
        doNothing().when(postService).addComment(comment,id,USER_TOKEN);
        //when
        mockMvc.perform(get("/api/posts/addComment")
                .param("id",String.valueOf(id))
                .param("content",comment)
                .header("authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        //then
        verify(postService, times(1)).addComment(comment,id,"Bearer " +USER_TOKEN);
    }
    @Test
    public void addCommentTestShouldFailBecauseTokenIsWrong() throws Exception {
        //given
        String comment = "hello";
        Long id = 2L;
        doNothing().when(postService).addComment(comment,id,INVALID_TOKEN);
        //when
        mockMvc.perform(get("/api/posts/addComment")
                .param("id",String.valueOf(id))
                .param("content",comment)
                .header("authorization", "Bearer " + INVALID_TOKEN))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
        //then
        verify(postService, times(0)).addComment(any(),any(),any());
    }
    @Test
    public void searchPostsTestShouldBeSuccessfully() throws Exception {
        //given
        String word = "word";
        List<PostDto> postDtoList = new ArrayList<>();
        postDtoList.add(new PostDto("url","title","body"));
        postDtoList.add(new PostDto("url1","title","body"));
        when(postService.findPostsByWord(word)).thenReturn(postDtoList);
        //when
        mockMvc.perform(get("/api/posts/searchForPosts")
                .param("word",word))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
        //then
        verify(postService, times(1)).findPostsByWord(word);
    }
    @Test
    public void searchPostsTestShouldReturnException() throws Exception {
        //given
        String word = "word";
        when(postService.findPostsByWord(word)).thenThrow(new PostNotFoundException("Can't find posts matching to this pattern"));
        //when
        mockMvc.perform(get("/api/posts/searchForPosts")
                .param("word",word))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debugMessage").value("Can't find posts matching to this pattern"));
        //then
        verify(postService, times(1)).findPostsByWord(word);
    }
}
