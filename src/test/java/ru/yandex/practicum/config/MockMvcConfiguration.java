package ru.yandex.practicum.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.dao.CommentDao;
import ru.yandex.practicum.dao.PostDao;
import ru.yandex.practicum.dao.TagDao;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

@Configuration
@ComponentScan({"ru.yandex.practicum.service"})
public class MockMvcConfiguration {

    @Bean
    @Primary
    public PostService mockPostService() {
        return Mockito.mock(PostService.class);
    }

    @Bean
    @Primary
    public TagService mockTagService() {
        return Mockito.mock(TagService.class);
    }

    @Bean
    @Primary
    public CommentService mockCommentService() {
        return Mockito.mock(CommentService.class);
    }

    @Bean
    @Primary
    public CommentDao mockCommentDao() {
        return Mockito.mock(CommentDao.class);
    }

    @Bean
    @Primary
    public PostDao mockPostDao() {
        return Mockito.mock(PostDao.class);
    }

    @Bean
    @Primary
    public TagDao mockTagDao() {
        return Mockito.mock(TagDao.class);
    }

}
