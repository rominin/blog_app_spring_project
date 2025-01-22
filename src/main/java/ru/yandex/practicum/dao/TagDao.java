package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Tag;

import java.util.List;

public interface TagDao {
    List<Tag> findAll();
    Tag findById(Long id);
    Tag findByName(String name);
    void save(Tag tag);
    List<Tag> findTagsByPostId(Long postId);
}
