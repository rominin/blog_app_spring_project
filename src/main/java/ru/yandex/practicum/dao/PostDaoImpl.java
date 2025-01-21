package ru.yandex.practicum.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PostDaoImpl implements PostDao {

    private final JdbcTemplate jdbcTemplate;

    public PostDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM posts ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Post> posts = jdbcTemplate.query(sql, new PostRowMapper(), size, offset);

        for (Post post : posts) {
            post.setTags(findTagsByPostId(post.getId()));
        }

        return posts;
    }

    @Override
    public Post findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        Post post = jdbcTemplate.queryForObject(sql, new PostRowMapper(), id);
        if (post != null) {
            post.setTags(findTagsByPostId(post.getId()));
        }
        return post;
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO posts (title, text, image_url, like_count, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, NOW(), NOW())";
        jdbcTemplate.update(sql, post.getTitle(), post.getText(), post.getImageUrl(), post.getLikeCount());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Post> findPostsByTag(Long tagId) {
        String sql = "SELECT p.* FROM posts p " +
                "JOIN post_tags pt ON p.id = pt.post_id " +
                "WHERE pt.tag_id = ? ORDER BY p.created_at DESC";
        return jdbcTemplate.query(sql, new PostRowMapper(), tagId);
    }

    private List<Tag> findTagsByPostId(Long postId) {
        String sql = "SELECT t.* FROM tags t " +
                "JOIN post_tags pt ON t.id = pt.tag_id " +
                "WHERE pt.post_id = ?";
        return jdbcTemplate.query(sql, new TagRowMapper(), postId);
    }

    private static class PostRowMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setText(rs.getString("text"));
            post.setImageUrl(rs.getString("image_url"));
            post.setLikeCount(rs.getInt("like_count"));
            post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            post.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return post;
        }
    }

    private static class TagRowMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag tag = new Tag();
            tag.setId(rs.getLong("id"));
            tag.setName(rs.getString("name"));
            return tag;
        }
    }
}
