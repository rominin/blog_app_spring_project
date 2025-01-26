package ru.yandex.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"ru.yandex.practicum.service"})
@Import({ru.yandex.practicum.dao.CommentDaoImpl.class,
        ru.yandex.practicum.dao.PostDaoImpl.class,
        ru.yandex.practicum.dao.TagDaoImpl.class})
public class DaoIntegrationTestsConfiguration {

    @Bean
    public String schema() {
        return "schema-dao-test.sql";
    }

}
