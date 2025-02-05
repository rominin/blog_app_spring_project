package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(PostController.class)
public class PostControllerIntegrationMockTest {

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private TagService tagService;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPostById_successful() throws Exception {
        Post post = new Post("Post Title", "Post Content", "url", 1);

        when(postService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post));
    }

    @Test
    void testAddPost_shouldRedirectToFeed() throws Exception {
        mockMvc.perform(post("/post/new")
                        .param("title", "New Post")
                        .param("text", "This is a new post")
                        .param("imageUrl", "url")
                        .param("tags", "Tag1,Tag2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/feed"));

        verify(postService, times(1)).addPost(any(Post.class), eq(List.of("Tag1", "Tag2")));
    }

    @Test
    void testEditPost_shouldRedirectToPost() throws Exception {
        mockMvc.perform(post("/post/1/edit")
                        .param("title", "Updated Title")
                        .param("text", "Updated Content")
                        .param("imageUrl", "url")
                        .param("tags", "UpdatedTag"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));

        verify(postService, times(1)).updatePost(any(Post.class), eq(List.of("UpdatedTag")));
    }

    @Test
    void testDeletePost_shouldRedirectToFeed() throws Exception {
        mockMvc.perform(post("/post/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/feed"));

        verify(postService, times(1)).deletePost(1L);
    }

    @Test
    void testLikePost_successful() throws Exception {
        mockMvc.perform(post("/post/1/like"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));

        verify(postService, times(1)).incrementLikeCount(1L);
    }

}
