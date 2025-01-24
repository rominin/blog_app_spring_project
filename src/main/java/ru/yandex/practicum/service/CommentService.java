package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;

@Service
public class CommentService {

    private final CommentDao commentDao;

    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public void addComment(Long postId, String text) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setText(text);
        commentDao.save(comment);
    }

    public void updateComment(Long id, String text) {
        Comment comment = commentDao.findById(id);
        if (comment != null) {
            comment.setText(text);
            commentDao.update(comment);
        }
    }

    public void deleteComment(Long id) {
        commentDao.delete(id);
    }

}
