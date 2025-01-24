package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Tag;

import java.util.List;

public interface TagDao {
    List<Tag> findAll();
    Tag findByName(String name);
    Long save(Tag tag);
    List<Tag> findTagsByPostId(Long postId);
}
