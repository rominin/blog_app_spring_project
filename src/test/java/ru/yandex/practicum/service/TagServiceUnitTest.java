package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.config.UnitTestsConfiguration;
import ru.yandex.practicum.dao.TagDao;
import ru.yandex.practicum.model.Tag;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnitTestsConfiguration.class)
public class TagServiceUnitTest {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagService tagService;

    @BeforeEach
    void resetMocks() {
        reset(tagDao);
    }

    @Test
    void testGetAllTags_success() {
        List<Tag> tags = Arrays.asList(
                new Tag("Tag1"),
                new Tag("Tag2")
        );

        when(tagDao.findAll()).thenReturn(tags);

        List<Tag> result = tagService.getAllTags();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tag1", result.get(0).getName());
        assertEquals("Tag2", result.get(1).getName());
        verify(tagDao, times(1)).findAll();
    }

    @Test
    void testGetAllTags_failure() {
        when(tagDao.findAll()).thenReturn(List.of());

        List<Tag> result = tagService.getAllTags();
        assertTrue(result.isEmpty());
        verify(tagDao, times(1)).findAll();
    }

    @Test
    void testFindTagByName_shouldReturnTagWhenExists() {
        String tagName = "Tag1";
        Tag mockTag = new Tag(tagName);

        when(tagDao.findByName(tagName)).thenReturn(mockTag);

        Tag result = tagService.findTagByName(tagName);
        assertNotNull(result);
        assertEquals(tagName, result.getName());
        verify(tagDao, times(1)).findByName(tagName);
    }

    @Test
    void testFindTagByName_shouldReturnNullWhenTagDoesNotExist() {
        String tagName = "Tag1";

        when(tagDao.findByName(tagName)).thenReturn(null);

        Tag result = tagService.findTagByName(tagName);
        assertNull(result);
        verify(tagDao, times(1)).findByName(tagName);
    }

}
