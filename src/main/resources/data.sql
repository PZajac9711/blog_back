DROP TABLE IF EXISTS profiles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;

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


INSERT INTO users (login, password, email,role) VALUES ('kicaj','$2a$12$z1otQYIj2GorwKglj6na0usit7XlphlJGH9einp.4/fPFybFBs9.2','kicaj123@wp.pl','admin');
