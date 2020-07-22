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
VALUES ('https://www.wallpapertip.com/wmimgs/3-35494_beautiful-wallpaper-full-hd.jpg','title ze spacja','<p class="lead">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ducimus, vero, obcaecati, aut, error quam sapiente nemo saepe quibusdam sit excepturi nam quia corporis eligendi eos magni recusandae laborum minus inventore?</p>

      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ut, tenetur natus doloremque laborum quos iste ipsum rerum obcaecati impedit odit illo dolorum ab tempora nihil dicta earum fugiat. Temporibus, voluptatibus.</p>

      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Eos, doloribus, dolorem iusto blanditiis unde eius illum consequuntur neque dicta incidunt ullam ea hic porro optio ratione repellat perspiciatis. Enim, iure!</p>

      <blockquote class="blockquote">
        <p class="mb-0">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
        <footer class="blockquote-footer">Someone famous in
          <cite title="Source Title">Source Title</cite>
        </footer>
      </blockquote>

      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Error, nostrum, aliquid, animi, ut quas placeat totam sunt tempora commodi nihil ullam alias modi dicta saepe minima ab quo voluptatem obcaecati?</p>

      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Harum, dolor quis. Sunt, ut, explicabo, aliquam tenetur ratione tempore quidem voluptates cupiditate voluptas illo saepe quaerat numquam recusandae? Qui, necessitatibus, est!</p>
      <hr>',CURRENT_DATE,'admin');
