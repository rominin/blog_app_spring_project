package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Comment;

import java.util.List;

public interface CommentDao {
    List<Comment> findByPostId(Long postId);
    void save(Comment comment);
    void update(Comment comment);
    void delete(Long id);
    Comment findById(Long id);
}
