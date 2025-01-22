package ru.yandex.practicum.dao;

import org.springframework.dao.EmptyResultDataAccessException;
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
        String sql = "SELECT * FROM tags ORDER BY name";
        return jdbcTemplate.query(sql, tagRowMapper);
    }

    @Override
    public Tag findById(Long id) {
        String sql = "SELECT * FROM tags WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, tagRowMapper, id);
    }

    @Override
    public Tag findByName(String name) {
        String sql = "SELECT * FROM tags WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Tag tag = new Tag();
                tag.setId(rs.getLong("id"));
                tag.setName(rs.getString("name"));
                return tag;
            }, name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void save(Tag tag) {
        String sql = "INSERT INTO tags (name) VALUES (?)";
        jdbcTemplate.update(sql, tag.getName());
    }

    @Override
    public List<Tag> findTagsByPostId(Long postId) {
        String sql = "SELECT t.* FROM tags t " +
                "JOIN post_tags pt ON t.id = pt.tag_id " +
                "WHERE pt.post_id = ?";
        return jdbcTemplate.query(sql, tagRowMapper, postId);
    }

    private final RowMapper<Tag> tagRowMapper = (rs, rowNum) -> {
        Tag tag = new Tag();
        tag.setId(rs.getLong("id"));
        tag.setName(rs.getString("name"));
        return tag;
    };
}
