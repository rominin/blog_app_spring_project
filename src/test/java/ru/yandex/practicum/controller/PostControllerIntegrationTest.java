package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.util.Collections;
import java.util.Comparator;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-mvc-test.properties")
public class PostControllerIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPostById_successful() throws Exception {
        Post post = new Post("Post Title", "Post Content", "url", 1);

        postService.addPost(post, Collections.emptyList());
        long lastPostId = postService.getPosts(1, 50).stream().sorted(Comparator.comparing(Post::getId)).toList().getLast().getId();

        mockMvc.perform(get("/post/" + lastPostId))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", hasProperty("title", is("Post Title"))));
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
    }

    @Test
    void testEditPost_shouldRedirectToPost() throws Exception {
        mockMvc.perform(post("/post/5/edit")
                        .param("title", "Some New Updated Title")
                        .param("text", "Some New Updated Content")
                        .param("imageUrl", "url")
                        .param("tags", "SomeNewUpdatedTag"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/5"));
    }

    @Test
    void testDeletePost_shouldRedirectToFeed() throws Exception {
        mockMvc.perform(post("/post/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/feed"));
    }

    @Test
    void testLikePost_successful() throws Exception {
        mockMvc.perform(post("/post/1/like"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));
    }

}
