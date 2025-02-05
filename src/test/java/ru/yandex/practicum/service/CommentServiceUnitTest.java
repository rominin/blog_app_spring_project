package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = CommentService.class)
public class CommentServiceUnitTest {

    @MockitoBean
    private CommentDao commentDao;

    @Autowired
    private CommentService commentService;

    @Test
    void testAddComment_success() {
        Long postId = 1L;
        String text = "New Comment";
        assertDoesNotThrow(() -> commentService.addComment(postId, text));
        verify(commentDao, times(1)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_shouldUpdateCommentWhenExists() {
        Long commentId = 1L;
        String newText = "Updated Comment";
        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setText("Old Comment");

        when(commentDao.findById(commentId)).thenReturn(existingComment);

        assertDoesNotThrow(() -> commentService.updateComment(commentId, newText));
        verify(commentDao, times(1)).update(any(Comment.class));
        assertEquals(newText, existingComment.getText());
    }

    @Test
    void testUpdateComment_shouldDoNothingIfCommentDoesNotExist() {
        Long commentId = 1L;
        String newText = "Updated Comment";

        when(commentDao.findById(commentId)).thenReturn(null);

        assertDoesNotThrow(() -> commentService.updateComment(commentId, newText));
        verify(commentDao, never()).update(any(Comment.class));
    }

    @Test
    void testDeleteComment_success() {
        Long commentId = 1L;
        assertDoesNotThrow(() -> commentService.deleteComment(commentId));
        verify(commentDao, times(1)).delete(commentId);
    }

}
