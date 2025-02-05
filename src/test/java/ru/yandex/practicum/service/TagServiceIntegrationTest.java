package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.model.Tag;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TagServiceIntegrationTest {

    @Autowired
    private TagService tagService;

    @Test
    void testGetAllTags_success() {
        List<Tag> tags = tagService.getAllTags();
        assertThat(tags).isNotNull();
        assertThat(tags).hasSize(7);
        assertThat(tags).extracting(Tag::getName).contains("Java", "Concurrent", "Serialization", "Spring", "Security", "Cloud", "Yandex");
    }

    @Test
    void testFindTagByName_shouldReturnTagWhenExists() {
        Tag tag = tagService.findTagByName("Java");
        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo("Java");
    }

    @Test
    void testFindTagByName_shouldReturnNullWhenTagDoesNotExist() {
        Tag tag = tagService.findTagByName("Blank_tag");
        assertThat(tag).isNull();
    }

}
