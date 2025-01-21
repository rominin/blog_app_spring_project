package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.model.Post;

import java.util.List;

@Service
public class PostService {

    private final PostDao postDao;

    public PostService(final PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getPosts(int page, int size) {
        return postDao.findAll(page, size);
    }

    public Post getPostById(Long id) {
        return postDao.findById(id);
    }

    public void addPost(Post post) {
        postDao.save(post);
    }

    public void updatePost(Post post) {
        postDao.save(post);
    }

    public void deletePost(Long id) {
        postDao.delete(id);
    }

    public List<Post> getPostsByTag(Long tagId) {
        return postDao.findPostsByTag(tagId);
    }

}
