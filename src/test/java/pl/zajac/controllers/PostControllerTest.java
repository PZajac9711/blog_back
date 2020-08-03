package pl.zajac.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajac.model.entities.Comment;
import pl.zajac.model.entities.Post;
import pl.zajac.services.PostService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {
    @MockBean
    PostService postService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    Post post = new Post();
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
        when(postService.getAllPosts(page)).thenReturn(postList);
        //when
        mockMvc.perform(get("/api/posts")
                .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].body").value("body"));
    }
}
