package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Post;

import java.util.List;

public interface PostDao {
    List<Post> findAll(int page, int size);
    Post findById(Long id);
    void save(Post post);
    void update(Post post);
    void delete(Long id);
    List<Post> findPostsByTag(Long tagId);
}
