DROP DATABASE IF EXISTS minimalnotepad;
CREATE DATABASE minimalnotepad;

USE minimalnotepad;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE groups (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        description VARCHAR(255)
);

CREATE TABLE user_groups (
                             user_id INT,
                             group_id INT,
                             PRIMARY KEY (user_id, group_id),
                             FOREIGN KEY (user_id) REFERENCES users(id),
                             FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE notes (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       text TEXT NOT NULL,
                       colour VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       user_id INT NOT NULL,
                       group_id INT NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES users(id),
                       FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(50) NOT NULL
);

CREATE TABLE note_categories (
                                 note_id INT,
                                 category_id INT,
                                 PRIMARY KEY (note_id, category_id),
                                 FOREIGN KEY (note_id) REFERENCES notes(id),
                                 FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE figures (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         link VARCHAR(255) NOT NULL,
                         note_id INT,
                         FOREIGN KEY (note_id) REFERENCES notes(id)
);

INSERT INTO users (username, email, password) VALUES ('admin', 'admin@example.com', 'admin');
