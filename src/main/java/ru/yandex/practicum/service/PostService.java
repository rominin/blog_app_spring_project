package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.TagDao;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostDao postDao;
    private final TagDao tagDao;

    public PostService(PostDao postDao, TagDao tagDao) {
        this.postDao = postDao;
        this.tagDao = tagDao;
    }

    public List<Post> getPosts(int page, int size) {
        return postDao.findAll(page, size);
    }

    public Post getPostById(Long id) {
        Post post = postDao.findById(id);
        post.setTags(tagDao.findTagsByPostId(id));
        return post;
    }

    public void addPost(Post post, List<String> tagNames) {
        Long postId = postDao.save(post);

        for (String tagName : tagNames) {
            Tag tag = tagDao.findByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tagDao.save(tag);
            }
            postDao.addTagToPost(postId, tag.getId());
        }
    }

    public void updatePost(Post post) {
        postDao.update(post);
    }

    public void deletePost(Long id) {
        postDao.delete(id);
    }

    public List<Post> getPostsByTag(Long tagId) {
        return postDao.findPostsByTag(tagId);
    }

    public List<Post> getPostsByTagName(String tagName, int page, int size) {
        Tag tag = tagDao.findByName(tagName);
        if (tag == null) {
            return new ArrayList<>();
        }
        return postDao.findPostsByTag(tag.getId(), page, size);
    }


}
