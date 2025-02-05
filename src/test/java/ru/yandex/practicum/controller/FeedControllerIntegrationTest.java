package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.service.PostService;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-mvc-test.properties")
public class FeedControllerIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFeed_success() throws Exception {
        List<Post> posts = List.of(
                new Post("Title1", "Text1", "url", 1),
                new Post("Title2", "Text2", "url", 2)
        );
        postService.addPost(posts.getFirst(), Collections.emptyList());
        postService.addPost(posts.getLast(), Collections.emptyList());

        mockMvc.perform(get("/feed")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", hasItem(hasProperty("title", is("Title1")))))
                .andExpect(model().attribute("posts", hasItem(hasProperty("title", is("Title2")))));
    }

    @Test
    void testFeed_withTagFilter_success() throws Exception {
        String tagName = "BrandNewTestTag";
        Tag tag = new Tag(tagName);
        List<Post> filteredPosts = List.of(
                new Post("Filtered Title", "Filtered Text", "url", 1)
        );
        postService.addPost(filteredPosts.getFirst(), List.of(tagName));

        mockMvc.perform(get("/feed")
                        .param("tag", tagName)
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", hasItem(hasProperty("title", is("Filtered Title")))));
    }

    @Test
    void testFeed_withNonExistentTag_success() throws Exception {
        String tagName = "NonExistentTag";

        mockMvc.perform(get("/feed")
                        .param("tag", tagName)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", List.of()))
                .andExpect(model().attribute("message", "No posts found for tag: " + tagName));
    }

}
