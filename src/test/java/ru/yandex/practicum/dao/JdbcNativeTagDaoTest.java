package ru.yandex.practicum.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.config.DataSourceConfiguration;
import ru.yandex.practicum.config.DaoIntegrationTestsConfiguration;
import ru.yandex.practicum.model.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, DaoIntegrationTestsConfiguration.class})
@TestPropertySource(locations = "classpath:application-dao-test.properties")
public class JdbcNativeTagDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TagDao tagDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM tags");

        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (1, 'Tag1')");
        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (2, 'Tag2')");
        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (3, 'Tag3')");

        jdbcTemplate.execute("INSERT INTO post_tags (post_id, tag_id) VALUES (1, 1)");
        jdbcTemplate.execute("INSERT INTO post_tags (post_id, tag_id) VALUES (1, 2)");
        jdbcTemplate.execute("INSERT INTO post_tags (post_id, tag_id) VALUES (2, 3)");
    }

    @Test
    void testFindAll_successful() {
        List<Tag> tags = tagDao.findAll();

        assertNotNull(tags);
        assertEquals(3, tags.size());
        assertEquals("Tag1", tags.get(0).getName());
        assertEquals("Tag2", tags.get(1).getName());
        assertEquals("Tag3", tags.get(2).getName());
    }

    @Test
    void testFindByName_successful() {
        Tag tag = tagDao.findByName("Tag1");

        assertNotNull(tag);
        assertEquals(1L, tag.getId());
        assertEquals("Tag1", tag.getName());
    }

    @Test
    void testFindByName_notFound() {
        Tag tag = tagDao.findByName("NonexistentTag");

        assertNull(tag);
    }

    @Test
    void testSave_successful() {
        Tag newTag = new Tag();
        newTag.setName("Tag4");
        Long tagId = tagDao.save(newTag);
        assertNotNull(tagId);

        Tag savedTag = tagDao.findByName("Tag4");
        assertNotNull(savedTag);
        assertEquals(tagId, savedTag.getId());
        assertEquals("Tag4", savedTag.getName());
    }

    @Test
    void testFindTagsByPostId_successful() {
        List<Tag> tags = tagDao.findTagsByPostId(1L);

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertEquals("Tag1", tags.get(0).getName());
        assertEquals("Tag2", tags.get(1).getName());
    }

    @Test
    void testFindTagsByPostId_failure() {
        List<Tag> tags = tagDao.findTagsByPostId(5L);

        assertNotNull(tags);
        assertTrue(tags.isEmpty());
    }

}
