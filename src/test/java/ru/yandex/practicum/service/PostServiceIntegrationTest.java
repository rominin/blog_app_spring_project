package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.config.DataSourceConfiguration;
import ru.yandex.practicum.config.IntegrationTestsConfiguration;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, IntegrationTestsConfiguration.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Test
    void testGetPosts_success() {
        List<Post> posts = postService.getPosts(1, 10);

        assertThat(posts).isNotNull();
        assertThat(posts).hasSizeGreaterThan(0);
    }

    @Test
    void testGetPostById_success() {
        Post post = postService.getPostById(8L);

        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo("Рандомный заголовок");
    }

    @Test
    void testGetPostById_failure() {
        assertThrows(DataAccessException.class, () -> postService.getPostById(999L));
    }

    @Test
    void testAddPost_success() {
        Post newPost = new Post();
        newPost.setTitle("New Post");
        newPost.setText("This is a new post.");
        newPost.setImageUrl("url");
        List<String> tags = List.of("NewTag1", "NewTag2");
        postService.addPost(newPost, tags);
        List<Post> posts = postService.getPosts(1, 10);
        assertThat(posts).extracting(Post::getTitle).contains("New Post");
        Post createdPost = posts.stream()
                .filter(post -> post.getTitle().equals("New Post"))
                .findFirst()
                .orElse(null);

        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getTags()).extracting(Tag::getName).contains("NewTag1", "NewTag2");
    }

    @Test
    void testUpdatePost_success() {
        Post post = postService.getPostById(5L);
        post.setTitle("Updated Title");
        post.setText("Updated Text");
        postService.updatePost(post, List.of("UpdatedTag1", "UpdatedTag2"));
        Post updatedPost = postService.getPostById(5L);

        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.getText()).isEqualTo("Updated Text");
        assertThat(updatedPost.getTags()).extracting(Tag::getName).contains("UpdatedTag1", "UpdatedTag2");
    }

    @Test
    void testDeletePost_success() {
        postService.deletePost(1L);
        List<Post> posts = postService.getPosts(1, 10);

        assertThat(posts).extracting(Post::getId).doesNotContain(1L);
    }

    @Test
    void testIncrementLikeCount_success() {
        int likeCount = postService.getPostById(1L).getLikeCount();
        postService.incrementLikeCount(1L);
        Post post = postService.getPostById(1L);

        assertThat(post.getLikeCount()).isEqualTo(++likeCount);
    }

}
