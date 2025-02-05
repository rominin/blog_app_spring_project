package ru.yandex.practicum.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.model.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class JdbcNativeCommentDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentDao commentDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text, created_at, updated_at) VALUES (1, 1, 'Comment 1 for Post 1', NOW(), NOW())");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text, created_at, updated_at) VALUES (2, 1, 'Comment 2 for Post 1', NOW(), NOW())");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text, created_at, updated_at) VALUES (3, 2, 'Comment 1 for Post 2', NOW(), NOW())");
    }

    @Test
    void testFindById_successful() {
        Comment comment = commentDao.findById(1L);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(1L);
        assertThat(comment.getText()).isEqualTo("Comment 1 for Post 1");
        assertThat(comment.getPostId()).isEqualTo(1L);
    }


    @Test
    void testSave_successful() {
        Comment comment = new Comment();
        comment.setPostId(1L);
        comment.setText("New Comment");
        commentDao.save(comment);
        List<Comment> comments = jdbcTemplate.query(
                "SELECT * FROM comments WHERE text = 'New Comment'",
                commentRowMapper
        );

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getText()).isEqualTo("New Comment");
    }

    @Test
    void testUpdate_successful() {
        Comment comment = commentDao.findById(1L);
        comment.setText("Updated Comment");
        commentDao.update(comment);
        Comment updatedComment = commentDao.findById(1L);

        assertThat(updatedComment.getText()).isEqualTo("Updated Comment");
        assertThat(updatedComment.getUpdatedAt()).isAfter(updatedComment.getCreatedAt());
    }

    @Test
    void testDelete_successful() {
        commentDao.delete(1L);
        List<Comment> comments = jdbcTemplate.query(
                "SELECT * FROM comments WHERE id = 1",
                commentRowMapper
        );

        assertThat(comments).isEmpty();
    }

    private final RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
        Comment c = new Comment();
        c.setId(rs.getLong("id"));
        c.setPostId(rs.getLong("post_id"));
        c.setText(rs.getString("text"));
        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        c.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return c;
    };

}