package ru.yandex.practicum.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    private final JdbcTemplate jdbcTemplate;

    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> findAll() {
        String sql = "SELECT * FROM tags ORDER BY name ASC";
        return jdbcTemplate.query(sql, new TagRowMapper());
    }

    @Override
    public List<Tag> findTagsByPostId(Long postId) {
        String sql = "SELECT t.* FROM tags t " +
                "JOIN post_tags pt ON t.id = pt.tag_id " +
                "WHERE pt.post_id = ?";
        return jdbcTemplate.query(sql, new TagRowMapper(), postId);
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
