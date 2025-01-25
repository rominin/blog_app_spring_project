package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.config.DataSourceConfiguration;
import ru.yandex.practicum.config.IntegrationTestsConfiguration;
import ru.yandex.practicum.model.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, IntegrationTestsConfiguration.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Test
    void testAddComment_success() {
        commentService.addComment(1L, "New Comment");
        List<Comment> comments = postService.getPostById(1L).getComments();
        assertThat(comments).hasSize(3);
        assertThat(comments.getFirst().getText()).isEqualTo("New Comment");
    }

    @Test
    public void testUpdateComment_shouldUpdateCommentWhenExists() {
        commentService.addComment(1L, "New Comment");
        commentService.updateComment(postService.getPostById(1L).getComments().getFirst().getId(), "Updated Comment");
        Comment updatedComment = postService.getPostById(1L).getComments().getFirst();
        assertThat(updatedComment.getText()).isEqualTo("Updated Comment");
    }

    @Test
    public void testDeleteComment_success() {
        int commentsCount = postService.getPostById(1L).getComments().size();
        commentService.addComment(1L, "New Comment");
        commentService.deleteComment(postService.getPostById(1L).getComments().getFirst().getId());
        List<Comment> comments = postService.getPostById(1L).getComments();
        assertThat(comments).hasSize(commentsCount);
    }

}
