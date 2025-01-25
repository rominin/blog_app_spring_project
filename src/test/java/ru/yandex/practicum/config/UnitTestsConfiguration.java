package ru.yandex.practicum.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.TagDao;

@Configuration
@ComponentScan({"ru.yandex.practicum.service"})
public class UnitTestsConfiguration {

    @Bean
    public CommentDao mockCommentDao() {
        return Mockito.mock(CommentDao.class);
    }

    @Bean
    public PostDao mockPostDao() {
        return Mockito.mock(PostDao.class);
    }

    @Bean
    public TagDao mockTagDao() {
        return Mockito.mock(TagDao.class);
    }

}
