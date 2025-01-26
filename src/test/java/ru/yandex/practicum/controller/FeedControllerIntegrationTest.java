package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.config.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(classes = {WebConfiguration.class, MockMvcConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-mvc-test.properties")
public class FeedControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testFeed_success() throws Exception {
        List<Post> posts = List.of(
                new Post("Title1", "Text1", "url", 1),
                new Post("Title2", "Text2", "url", 2)
        );
        when(postService.getPosts(1, 10)).thenReturn(posts);

        mockMvc.perform(get("/feed")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", posts));
    }

    @Test
    void testFeed_withTagFilter_success() throws Exception {
        String tagName = "Java";
        Tag tag = new Tag(tagName);
        List<Post> filteredPosts = List.of(
                new Post("Filtered Title", "Filtered Text", "url", 1)
        );
        when(tagService.findTagByName(tagName)).thenReturn(tag);
        when(postService.getPostsByTagName(tagName, 1, 10)).thenReturn(filteredPosts);

        mockMvc.perform(get("/feed")
                        .param("tag", tagName)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("posts", filteredPosts))
                .andExpect(model().attribute("tag", tagName));
    }

    @Test
    void testFeed_withNonExistentTag_success() throws Exception {
        String tagName = "NonExistentTag";
        when(tagService.findTagByName(tagName)).thenReturn(null);

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
