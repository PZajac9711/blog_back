package pl.zajac.services.imp;

import javafx.geometry.Pos;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.zajac.model.dto.EditPostDto;
import pl.zajac.model.dto.PostDto;
import pl.zajac.model.entities.Comment;
import pl.zajac.model.entities.Post;
import pl.zajac.model.exceptions.custom.PostNotFoundException;
import pl.zajac.model.repository.PostRepository;
import pl.zajac.model.security.jwt.JwtGenerate;
import pl.zajac.model.security.jwt.ReadToken;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImpTest {
    @InjectMocks
    PostServiceImp postServiceImp;
    @Mock
    PostRepository postRepository;
    @Mock
    ReadToken readToken;

    List<Post> database = new ArrayList<>();

    @Before
    public void setUp() {
        Post post = new Post();
        post.setId(1L);
        post.setImageUrl("url");
        post.setBody("body");
        post.setTitle("title");
        post.setPublished(true);
        post.setAuthorName("admin");
        post.setComments(new ArrayList<Comment>());
        post.setPublicationDate(java.time.LocalDateTime.now());
        this.database.add(post);
        this.database.add(post);
    }

    @Test
    public void getAllPostsTestShouldReturnTwoElements() {
        //given
        int page = 0;
        when(postRepository.findAllPublished(any())).thenReturn(database);
        //when
        List<Post> postList = postServiceImp.getAllPosts(page);
        //then
        verify(postRepository, times(1)).findAllPublished(any());
        assertFalse(postList.isEmpty());
        assertEquals(2, postList.size());
    }

    @Test
    public void getSpecificPostTestShouldBeSuccessfullyAndReturnSpecificPost() {
        //given
        String title = "title";
        when(postRepository.findByTitle(title)).thenReturn(Optional.of(database.get(0)));
        //when
        Post post = postServiceImp.getSpecificPost(title);
        //then
        assertNotNull(post);
        assertEquals("url", post.getImageUrl());
    }

    @Test(expected = PostNotFoundException.class)
    public void getSpecificPostTestShouldThrowExceptionBecauseNoPostReturnFromDatabase() {
        //given
        String title = "title";
        when(postRepository.findByTitle(title)).thenReturn(Optional.empty());
        //when
        Post post = postServiceImp.getSpecificPost(title);
        //then
        assertNull(post);
    }

    //Dlaczego jak tworzymy mocka z ReadToken w przypadku kiedy ReadToken nie jest componentem i nie jest wstrzykiwany
    //tylko normalnie inocjowany przez new to mock.thenReturn wywoluje pierwotna metode ???  :((((((
    @Test
    public void addPostTestShouldBeSuccessfully() {
        //given
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        PostDto postDto = new PostDto("url", "title", "body");
        //when
        doReturn("admin").when(readToken).getLogin(anyString());
        postServiceImp.addPost(postDto, "Token");
        //then
        verify(postRepository).save(captor.capture());
        assertEquals(postDto.getUrl(), captor.getValue().getImageUrl());
        assertEquals("admin", captor.getValue().getAuthorName());
    }

    @Test
    public void findAllPostsTestShouldBeSuccessfullyBecauseWeGotPostsInDatabase() {
        //when
        when(postRepository.findAll()).thenReturn(database);
        List<Post> postList = postServiceImp.findAllPosts();
        //then
        assertNotNull(postList);
        assertEquals(2, postList.size());
        assertTrue(database.containsAll(postList) && postList.containsAll(database));
    }

    @Test(expected = PostNotFoundException.class)
    public void findAllPostsTestShouldReturnExceptionBecauseThereIsNoPostsInDatabase() {
        //when
        when(postRepository.findAll()).thenReturn(new ArrayList<>());
        List<Post> postList = postServiceImp.findAllPosts();
        //then
        assertNull(postList);
    }

    @Test
    public void changePostStatusTestShouldChangePostStatus() {
        //given
        long id = 1;
        boolean startIsPublishedStatus = this.database.get((int) id).isPublished();
        //when
        when(postRepository.findById(id)).thenReturn(Optional.of(this.database.get((int) id)));
        postServiceImp.changePostStatus(id);
        //then
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());

        assertTrue(startIsPublishedStatus != captor.getValue().isPublished());
    }

    @Test(expected = PostNotFoundException.class)
    public void changePostStatusTestShouldThrowExceptionBecauseNoPostFoundInDatabase(){
        //when
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        postServiceImp.changePostStatus(anyLong());
    }

    @Test(expected = PostNotFoundException.class)
    public void editPostTestShouldThrowExceptionBecauseThereIsNoPostInDatabase(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        postServiceImp.editPost(new EditPostDto("t1","t2","t3","1"));
    }

    @Test
    public void editPostTestShouldChangeTitleBodyAndUrl(){
        //given
        EditPostDto editPostDto = new EditPostDto("t1","t2","t3","1");
        //when
        when(postRepository.findById(Long.parseLong(editPostDto.getId()))).thenReturn(Optional.of(database.get(1)));
        postServiceImp.editPost(editPostDto);
        //then
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());

        assertEquals(captor.getValue().getTitle(), editPostDto.getTitle());
        assertEquals(captor.getValue().getBody(), editPostDto.getBody());
        assertEquals(captor.getValue().getImageUrl(), editPostDto.getUrl());
    }

    @Test(expected = PostNotFoundException.class)
    public void addCommentTestShouldThrowPostNotFoundException(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        postServiceImp.addComment("", 1L,"anyString()");
    }

    @Test
    public void addCommentShouldAddComment(){
        //wehn
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(database.get(1)));
        when(readToken.getLogin(anyString())).thenReturn("admin");
        postServiceImp.addComment("comment",1L, "Token");
        //then
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());

        assertTrue(captor.getValue().getComments().size() > 0);
        assertTrue(captor.getValue().getComments().get(0).getUserName().equals("admin"));
    }

    @Test
    public void findPostsByWordTestShouldReturnElementsEqualsToDatabase(){
        //when
        when(postRepository.findByWord(anyString())).thenReturn(database);
        List<PostDto> postDtoList = postServiceImp.findPostsByWord("word");
        //then
        assertEquals(postDtoList.size(), database.size());
    }
    @Test(expected = PostNotFoundException.class)
    public void findPostsByWordTestShouldThrowPotNotFoundException(){
        //when
        when(postRepository.findByWord(anyString())).thenReturn(new ArrayList<>());
        List<PostDto> postDtoList = postServiceImp.findPostsByWord("word");
        //then
        assertNull(postDtoList);
    }
}
