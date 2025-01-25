package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.config.UnitTestsConfiguration;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.model.Comment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnitTestsConfiguration.class)
public class CommentServiceUnitTest {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void resetMocks() {
        reset(commentDao);
    }

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
