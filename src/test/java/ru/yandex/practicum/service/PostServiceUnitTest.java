package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.config.UnitTestsConfiguration;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.TagDao;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnitTestsConfiguration.class)
public class PostServiceUnitTest {

    @Autowired
    private PostDao postDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private PostService postService;

    @BeforeEach
    void resetMocks() {
        reset(postDao, tagDao);
    }

    @Test
    void testGetPosts_success() {
        List<Post> posts = List.of(
                new Post("Title1", "url1", "Text1", 5),
                new Post("Title2", "url2", "Text2", 10)
        );

        when(postDao.findAll(1, 10)).thenReturn(posts);

        List<Post> result = postService.getPosts(1, 10);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(postDao, times(1)).findAll(1, 10);
    }

    @Test
    void testGetPostById_success() {
        Long postId = 1L;
        Post mockPost = new Post("Title", "url", "Text", 5);

        when(postDao.findById(postId)).thenReturn(mockPost);
        when(tagDao.findTagsByPostId(postId)).thenReturn(List.of(new Tag("Tag1"), new Tag("Tag2")));

        Post result = postService.getPostById(postId);
        assertNotNull(result);
        assertEquals(2, result.getTags().size());
        verify(postDao, times(1)).findById(postId);
        verify(tagDao, times(1)).findTagsByPostId(postId);
    }

    @Test
    void testAddPost_success() {
        Post post = new Post("Title", "url", "Text", 0);
        List<String> tagNames = List.of("Tag1", "Tag2");
        Long postId = 1L;

        when(postDao.save(post)).thenReturn(postId);
        when(tagDao.findByName(anyString())).thenReturn(null);
        when(tagDao.save(any(Tag.class))).thenReturn(1L);

        postService.addPost(post, tagNames);
        verify(postDao, times(1)).save(post);
        verify(tagDao, times(2)).findByName(anyString());
        verify(tagDao, times(2)).save(any(Tag.class));
        verify(postDao, times(2)).addTagToPost(eq(postId), anyLong());
    }

    @Test
    void testUpdatePost_success() {
        // Arrange
        Post post = new Post("Updated Title", "url", "Updated Text", 0);
        List<String> tagNames = List.of("Tag1", "Tag2");

        when(tagDao.findByName(anyString())).thenReturn(null);
        when(tagDao.save(any(Tag.class))).thenReturn(1L);

        postService.updatePost(post, tagNames);
        verify(postDao, times(1)).update(post);
        verify(postDao, times(1)).clearTags(post.getId());
        verify(tagDao, times(2)).findByName(anyString());
        verify(tagDao, times(2)).save(any(Tag.class));
        verify(postDao, times(2)).addTagToPost(eq(post.getId()), anyLong());
    }

    @Test
    void testDeletePost_success() {
        Long postId = 1L;

        postService.deletePost(postId);
        verify(postDao, times(1)).delete(postId);
    }

    @Test
    void testGetPostsByTagName_success() {
        String tagName = "Tag1";
        Tag mockTag = new Tag(tagName);
        List<Post> post = List.of(new Post("Title", "url", "Text", 5));

        when(tagDao.findByName(tagName)).thenReturn(mockTag);
        when(postDao.findPostsByTag(mockTag.getId(), 1, 10)).thenReturn(post);

        List<Post> result = postService.getPostsByTagName(tagName, 1, 10);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tagDao, times(1)).findByName(tagName);
        verify(postDao, times(1)).findPostsByTag(mockTag.getId(), 1, 10);
    }

    @Test
    void testIncrementLikeCount_success() {
        Long postId = 1L;

        postService.incrementLikeCount(postId);
        verify(postDao, times(1)).incrementLikeCount(postId);
    }


}
