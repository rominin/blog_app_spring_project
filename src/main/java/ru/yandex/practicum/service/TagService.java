package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.TagDao;
import ru.yandex.practicum.model.Tag;

import java.util.List;

@Service
public class TagService {

    private final TagDao tagDao;

    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public List<Tag> getAllTags() {
        return tagDao.findAll();
    }

    public Tag findTagByName(String name) {
        return tagDao.findByName(name);
    }

}
