package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.TagDao;
import ru.yandex.practicum.model.Tag;

import java.util.List;

@Service
public class TagService {

    private TagDao tagDao;

    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public List<Tag> getAllTags() {
        return tagDao.findAll();
    }

    public List<Tag> getTagsByPostId(Long postId) {
        return tagDao.findTagsByPostId(postId);
    }

}
