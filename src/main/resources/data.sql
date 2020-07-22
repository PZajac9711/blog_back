DROP TABLE IF EXISTS profiles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS post;
CREATE TABLE users(
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);
CREATE TABLE profiles(
    user_id INT(6) UNSIGNED,
    test VARCHAR(200),
    FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE posts(
    id INT(6) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    image_url VARCHAR(355),
    title VARCHAR(255),
    body TEXT,
    publication_date DATE,
    author_name VARCHAR(255)
);

INSERT INTO users (login, password, email,role) VALUES ('kicaj','$2a$12$z1otQYIj2GorwKglj6na0usit7XlphlJGH9einp.4/fPFybFBs9.2','kicaj123@wp.pl','admin');
INSERT INTO posts (image_url, title, body, publication_date, author_name)
VALUES ('https://www.wallpapertip.com/wmimgs/3-35494_beautiful-wallpaper-full-hd.jpg','title1','body',CURRENT_DATE,'admin');
INSERT INTO posts (image_url, title, body, publication_date, author_name)
VALUES ('https://www.wallpapertip.com/wmimgs/3-35494_beautiful-wallpaper-full-hd.jpg','title2','body',CURRENT_DATE,'admin');
INSERT INTO posts (image_url, title, body, publication_date, author_name)
VALUES ('https://www.wallpapertip.com/wmimgs/3-35494_beautiful-wallpaper-full-hd.jpg','title3','body',CURRENT_DATE,'admin');
INSERT INTO posts (image_url, title, body, publication_date, author_name)
VALUES ('https://www.wallpapertip.com/wmimgs/3-35494_beautiful-wallpaper-full-hd.jpg','title4','body',CURRENT_DATE,'admin');
INSERT INTO posts (image_url, title, body, publication_date, author_name)
VALUES ('https://www.wallpapertip.com/wmimgs/3-35494_beautiful-wallpaper-full-hd.jpg','title5','body',CURRENT_DATE,'admin');
