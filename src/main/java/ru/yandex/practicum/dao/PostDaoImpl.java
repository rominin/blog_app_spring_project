package ru.yandex.practicum.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class PostDaoImpl implements PostDao {

    private final JdbcTemplate jdbcTemplate;

    public PostDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll(int page, int size) {
        String sql = "SELECT * FROM posts ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Post> posts = jdbcTemplate.query(sql, postRowMapper, size, (page - 1) * size);

        for (Post post : posts) {
            String commentCountSql = "SELECT COUNT(*) FROM comments WHERE post_id = ?";
            int commentCount = jdbcTemplate.queryForObject(commentCountSql, Integer.class, post.getId());
            post.setCommentCount(commentCount);

            String tagSql = "SELECT t.id, t.name FROM tags t " +
                    "JOIN post_tags pt ON t.id = pt.tag_id WHERE pt.post_id = ?";
            List<Tag> tags = jdbcTemplate.query(tagSql, tagRowMapper, post.getId());
            post.setTags(tags);
        }

        return posts;
    }

    @Override
    public Post findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        Post post = jdbcTemplate.queryForObject(sql, postRowMapper, id);

        String commentSql = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at DESC";
        List<Comment> comments = jdbcTemplate.query(commentSql, (rs, rowNum) -> {
            Comment comment = new Comment();
            comment.setId(rs.getLong("id"));
            comment.setPostId(rs.getLong("post_id"));
            comment.setText(rs.getString("text"));
            comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            comment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return comment;
        }, id);
        post.setComments(comments);

        post.setCommentCount(comments.size());

        String tagSql = "SELECT t.id, t.name FROM tags t " +
                "JOIN post_tags pt ON t.id = pt.tag_id WHERE pt.post_id = ?";
        List<Tag> tags = jdbcTemplate.query(tagSql, tagRowMapper, id);
        post.setTags(tags);

        return post;
    }

    @Override
    public Long save(Post post) {
        String sql = "INSERT INTO posts (title, text, image_url, like_count, created_at, updated_at) " +
                "VALUES (?, ?, ?, 0, NOW(), NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setString(3, post.getImageUrl());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET title = ?, text = ?, image_url = ?, like_count = ? WHERE id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getText(), post.getImageUrl(), post.getLikeCount(), post.getId());
    }

    @Override
    public void delete(Long id) {
        String deleteTagsSql = "DELETE FROM post_tags WHERE post_id = ?";
        jdbcTemplate.update(deleteTagsSql, id);

        String deleteCommentsSql = "DELETE FROM comments WHERE post_id = ?";
        jdbcTemplate.update(deleteCommentsSql, id);

        String deletePostSql = "DELETE FROM posts WHERE id = ?";
        jdbcTemplate.update(deletePostSql, id);
    }

    @Override
    public List<Post> findPostsByTag(Long tagId, int page, int size) {
        String sql = "SELECT p.* FROM posts p " +
                "JOIN post_tags pt ON p.id = pt.post_id " +
                "WHERE pt.tag_id = ? " +
                "ORDER BY p.created_at DESC LIMIT ? OFFSET ?";
        int offset = (page - 1) * size;

        List<Post> posts = jdbcTemplate.query(sql, postRowMapper, tagId, size, offset);

        for (Post post : posts) {
            String tagSql = "SELECT t.id, t.name FROM tags t " +
                    "JOIN post_tags pt ON t.id = pt.tag_id WHERE pt.post_id = ?";
            List<Tag> tags = jdbcTemplate.query(tagSql, tagRowMapper, post.getId());
            post.setTags(tags);

            String commentCountSql = "SELECT COUNT(*) FROM comments WHERE post_id = ?";
            int commentCount = jdbcTemplate.queryForObject(commentCountSql, Integer.class, post.getId());
            post.setCommentCount(commentCount);
        }

        return posts;
    }

    @Override
    public void addTagToPost(Long postId, Long tagId) {
        String sql = "INSERT INTO post_tags (post_id, tag_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, postId, tagId);
    }

    @Override
    public void incrementLikeCount(Long id) {
        String sql = "UPDATE posts SET like_count = like_count + 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void clearTags(Long postId) {
        String sql = "DELETE FROM post_tags WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }


    private final RowMapper<Post> postRowMapper = (rs, rowNum) -> {
        Post post = new Post();
        post.setId(rs.getLong("id"));
        post.setTitle(rs.getString("title"));
        post.setText(rs.getString("text"));
        post.setImageUrl(rs.getString("image_url"));
        post.setLikeCount(rs.getInt("like_count"));
        post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        post.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return post;
    };

    private final RowMapper<Tag> tagRowMapper = (rs, rowNum) -> {
        Tag tag = new Tag();
        tag.setId(rs.getLong("id"));
        tag.setName(rs.getString("name"));
        return tag;
    };
}
