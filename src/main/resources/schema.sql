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
    ('Title1', 'Text1 here', 'localhost:...', 1),
    ('Title2', 'Text2 here', 'localhost:...', 2);

INSERT INTO tags (name) VALUES ('Tag1'), ('Tag2'), ('Tag3');

INSERT INTO post_tags (post_id, tag_id) VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3);

INSERT INTO comments (post_id, text) VALUES
    (1, 'Comment1'),
    (1, 'Comment2'),
    (2, 'Comment3');