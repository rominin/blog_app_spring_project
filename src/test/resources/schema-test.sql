CREATE TABLE if NOT EXISTS posts (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    image_url VARCHAR(255),
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE if NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
    );

CREATE TABLE if NOT EXISTS tags (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
    );

CREATE TABLE if NOT EXISTS post_tags (
    post_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
    );


DELETE FROM post_tags;
DELETE FROM tags;
DELETE FROM comments;
DELETE FROM posts;


INSERT INTO posts (title, text, image_url, like_count) VALUES
    ('Overview of the java.util.concurrent', 'The java.util.concurrent contains way too many features to discuss in a single write-up. In this article, we will mainly focus on some of the most useful utilities from this package like:

Executor
ExecutorService
ScheduledExecutorService
Future
CountDownLatch
CyclicBarrier
Semaphore
ThreadFactory
BlockingQueue
DelayQueue
Locks
Phaser
You can also find many dedicated articles to individual classes here.', 'https://miro.medium.com/v2/resize:fit:764/1*_46dcZQC4bFk_rrj_YwP9Q.png', 5),
    ('Guide to the Synchronized Keyword in Java', 'In this brief article, we explored different ways of using the synchronized keyword to achieve thread synchronization.

We also learned how a race condition can impact our application and how synchronization helps us avoid that.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVuA64QElzqN1zsKr8LASS_u58UzcXu8PZFQ&s', 3),
    ('Guide to the Volatile Keyword in Java', 'TThe compiler, runtime, or processors may apply all sorts of optimizations. Although these optimizations are usually beneficial, typically without necessary synchronizations, they can cause subtle issues in the form of unexpected results.

Caching and reordering are optimizations that may surprise us in concurrent contexts when we don’t synchronize code correctly. Java and the JVM provide many ways to control memory order, and a volatile field is one of them.

This tutorial focuses on Java’s foundational but often misunderstood concept, the volatile field. First, we’ll start with some background about how the underlying computer architecture works, and then we’ll get familiar with memory order in Java. Further, we’ll understand the challenges of concurrency in multiprocessor shared architecture and how volatile fields help fix them.', 'https://jenkov.com/images/java-concurrency/java-volatile-1.png', 13),
    ('Guide to Choosing Between Protocol Buffers and JSON', 'Protocol Buffers (Protobuf) and JSON are popular data serialization formats but differ significantly in readability, performance, efficiency, and size.

In this tutorial, we’ll compare these formats and explore their trade-offs. This will help us make informed decisions based on the use case when we need to choose one over the other.', 'https://images.ctfassets.net/23aumh6u8s0i/3DV4W20jYqG30RFAKfHR8R/54e80d22b5e6583baa3f432b647c1bbb/compressed-env-sizes', 12),
    ('Securing Spring Boot API With API Key and Secret', 'Security plays a vital role in REST API development. An insecure REST API can provide direct access to sensitive data on back-end systems. So, organizations need to pay attention to API Security. Spring Security provides various mechanisms to secure our REST APIs. One of them is API keys. An API key is a token that a client provides when invoking API calls. In this tutorial, we’ll discuss the implementation of API key-based authentication in Spring Security.', 'https://miro.medium.com/v2/resize:fit:1400/0*c06CRsVMEz7U4s25', 5),
    ('FIFO Queue Support in Spring Cloud AWS', 'FIFO (First-In-First-Out) queues in AWS SQS are designed to ensure that messages are processed in the exact order they are sent and that each message is delivered only once.

Spring Cloud AWS v3 supports this functionality with easy-to-use abstractions that allow developers to handle FIFO queue features like message ordering and deduplication with minimal boilerplate code.

In this tutorial, we’ll explore three practical use cases of FIFO queues in the context of a financial transaction processing system:

Ensuring strict message ordering for transactions within the same account
Processing transactions from different accounts in parallel while maintaining FIFO semantics for each account
Handling message retries in case of processing failures, ensuring that retries respect the original message order
We’ll demonstrate these scenarios by setting up an event-driven application and creating live tests to assert the behavior is as expected, leveraging the environment and test setup from the Spring Cloud AWS SQS V3 introductory article.', 'https://images.javatpoint.com/tutorial/spring-cloud/images/spring-cloud.png', 6),
    ('Разработка программного обеспечения', 'Разрабо́тка програ́ммного обеспе́чения (англ. software development) — деятельность по созданию нового программного обеспечения[1].

Разработка программного обеспечения как инженерная дисциплина является составной частью (областью) программной инженерии, наряду с дисциплинами, отвечающими за функционирование и сопровождение программных продуктов[2].', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjCnWUbkEljpS70ecGxM_TAIMm8nRWdj1dUQ&s', 0),
    ('Рандомный заголовок', 'Здесь должен располагаться какой-либо текст', 'это то же что и отсутствие изображения', 9),
    ('Другой пост-пустышка', 'У этого поста есть какие-то комментарии', 'картинка не предоставлена', 12),
    ('Фронтенд или бэкенд: по какому пути в разработке пойти', 'В вакансиях часто встречаются запросы конкретно на фронтенд- или бэкенд-разработчика, так как эти слои пишут по разным принципам и часто на разных языках программирования. Попробуем разобраться, в чем разница между frontend- backend-разработкой и как они взаимодействуют друг с другом. Читайте полную статью здесь https://practicum.yandex.ru/blog/chem-otlichaetsya-backend-i-frontend/', 'https://avatars.mds.yandex.net/get-lpc/10704932/73ffff80-5395-4fec-824d-15a206145fae/lqip_q70', 0),
    ('Прогноз погоды', 'Значительная облачность. Местами возможны заморозки. Понижение 0C. Ветер ВЮВ и переменный. Влажность97%.', 'https://ssl.gstatic.com/onebox/weather/64/partly_cloudy.png', 17);

INSERT INTO tags (name) VALUES ('Java'), ('Concurrent'), ('Serialization'), ('Spring'), ('Security'), ('Cloud'), ('Yandex');

INSERT INTO post_tags (post_id, tag_id) VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 2),
    (3, 1),
    (3, 2),
    (4, 3),
    (5, 4),
    (5, 5),
    (6, 4),
    (6, 6),
    (10, 7);

INSERT INTO comments (post_id, text) VALUES
    (1, 'Comment1'),
    (1, 'Comment2'),
    (9, 'Комментарий первый'),
    (9, 'Комментарий второй'),
    (9, 'И даже третий'),
    (10, 'Делать что угодно, главное чтобы не пришлось разрабатывать UI');