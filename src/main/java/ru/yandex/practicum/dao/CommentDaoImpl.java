package ru.yandex.practicum.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CommentDaoImpl implements CommentDao {

    private final JdbcTemplate jdbcTemplate;

    public CommentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, commentRowMapper, postId);
    }

    @Override
    public Comment findById(Long id) {
        String sql = "SELECT * FROM comments WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, commentRowMapper, id);
    }

    @Override
    public void save(Comment comment) {
        String sql = "INSERT INTO comments (post_id, text) VALUES (?, ?)";
        jdbcTemplate.update(sql, comment.getPostId(), comment.getText());
    }

    @Override
    public void update(Comment comment) {
        String sql = "UPDATE comments SET text = ? WHERE id = ?";
        jdbcTemplate.update(sql, comment.getText(), comment.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private final RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
        Comment comment = new Comment();
        comment.setId(rs.getLong("id"));
        comment.setPostId(rs.getLong("post_id"));
        comment.setText(rs.getString("text"));
        comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        comment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return comment;
    };
}
