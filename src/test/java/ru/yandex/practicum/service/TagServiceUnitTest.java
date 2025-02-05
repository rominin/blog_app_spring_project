package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.dao.TagDao;
import ru.yandex.practicum.model.Tag;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = TagService.class)
public class TagServiceUnitTest {

    @MockitoBean
    private TagDao tagDao;

    @Autowired
    private TagService tagService;

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
