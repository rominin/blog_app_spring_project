package ru.yandex.practicum.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.config.DataSourceConfiguration;
import ru.yandex.practicum.config.DaoIntegrationTestsConfiguration;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, DaoIntegrationTestsConfiguration.class})
@TestPropertySource(locations = "classpath:application-dao-test.properties")
public class JdbcNativePostDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostDao postDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM tags");

        jdbcTemplate.execute("INSERT INTO posts (id, title, text, image_url, like_count, created_at, updated_at) VALUES " +
                "(1, 'Title1', 'Text1', 'url', 5, NOW(), NOW())," +
                "(2, 'Title2', 'Text2', 'url', 10, NOW(), NOW())");

        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (1, 'Tag1'), (2, 'Tag2')");

        jdbcTemplate.execute("INSERT INTO post_tags (post_id, tag_id) VALUES (1, 1), (1, 2), (2, 1)");
    }

    @Test
    void testFindAll_successful() {
        List<Post> posts = postDao.findAll(1, 10);
        assertNotNull(posts);
        assertEquals(2, posts.size());

        Post firstPost = posts.getFirst();
        assertEquals("Title1", firstPost.getTitle());
        assertEquals(5, firstPost.getLikeCount());
    }

    @Test
    void testFindById_successful() {
        Post post = postDao.findById(1L);

        assertNotNull(post);
        assertEquals("Title1", post.getTitle());
        assertEquals("Text1", post.getText());
    }

    @Test
    void testFindById_notFound() {
        assertThrows(DataAccessException.class, () -> postDao.findById(3L));
    }

    @Test
    void testSave_successful() {
        Post newPost = new Post();
        newPost.setTitle("New Title");
        newPost.setText("New Text");
        newPost.setImageUrl("utl");
        Long postId = postDao.save(newPost);
        assertNotNull(postId);

        Post savedPost = postDao.findById(postId);
        assertNotNull(savedPost);
        assertEquals("New Title", savedPost.getTitle());
        assertEquals("New Text", savedPost.getText());
    }

    @Test
    void testUpdate_successful() {
        Post post = postDao.findById(1L);
        assertNotNull(post);
        post.setTitle("Updated Title");
        post.setText("Updated Text");
        postDao.update(post);

        Post updatedPost = postDao.findById(1L);
        assertNotNull(updatedPost);
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Updated Text", updatedPost.getText());
    }

    @Test
    void testDelete_successful() {
        postDao.delete(1L);

        assertThrows(DataAccessException.class, () -> postDao.findById(1L));
    }

    @Test
    void testFindPostsByTag_successful() {
        List<Post> posts = postDao.findPostsByTag(1L, 1, 10);

        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertEquals("Title1", posts.get(0).getTitle());
    }

    @Test
    void testAddTagToPost_successful() {
        postDao.addTagToPost(2L, 2L);
        List<Post> posts = postDao.findPostsByTag(2L, 1, 10);

        assertEquals("Title2", posts.get(1).getTitle());
    }

    @Test
    void testIncrementLikeCount_successful() {
        int likeCount = postDao.findById(1L).getLikeCount();
        postDao.incrementLikeCount(1L);
        Post post = postDao.findById(1L);

        assertNotNull(post);
        assertEquals(++likeCount, post.getLikeCount());
    }

    @Test
    void testClearTags_successful() {
        postDao.clearTags(1L);
        List<Tag> tags = postDao.findById(1L).getTags();

        assertTrue(tags.isEmpty());
    }

}
