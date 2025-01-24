package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Comment;

public interface CommentDao {
    void save(Comment comment);
    void update(Comment comment);
    void delete(Long id);
    Comment findById(Long id);
}
