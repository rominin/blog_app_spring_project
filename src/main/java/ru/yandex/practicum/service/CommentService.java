package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;

import java.util.List;

@Service
public class CommentService {

    private final CommentDao commentDao;

    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentDao.findByPostId(postId);
    }

    public void addComment(Comment comment) {
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
