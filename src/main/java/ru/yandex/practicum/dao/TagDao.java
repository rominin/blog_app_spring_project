package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Tag;

import java.util.List;

public interface TagDao {
    List<Tag> findAll();
    List<Tag> findTagsByPostId(Long postId);
}
