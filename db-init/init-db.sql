DROP DATABASE IF EXISTS minimalnotepad;
CREATE DATABASE minimalnotepad;

USE minimalnotepad;

CREATE TABLE languages
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(20) NOT NULL,
    country VARCHAR(20) NOT NULL
);

CREATE TABLE users
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    language_id INT NOT NULL DEFAULT 1,
    FOREIGN KEY (language_id) REFERENCES languages (id)
);

CREATE TABLE groups
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    user_id     INT         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE user_groups
(
    user_id  INT,
    group_id INT,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE TABLE notes
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(100) NOT NULL,
    text       TEXT         NOT NULL,
    colour     VARCHAR(20)  NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id    INT          NOT NULL,
    group_id   INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE TABLE categories
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
CREATE TABLE categories_localizations
(
    language_id INT NOT NULL,
    category_id INT NOT NULL,
    translation VARCHAR(100) NOT NULL,
    FOREIGN KEY (language_id) REFERENCES languages(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE note_categories
(
    note_id     INT,
    category_id INT,
    PRIMARY KEY (note_id, category_id),
    FOREIGN KEY (note_id) REFERENCES notes (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE figures
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    link    VARCHAR(255) NOT NULL,
    note_id INT,
    FOREIGN KEY (note_id) REFERENCES notes (id)
);

-- Languages
INSERT INTO languages (id, name, country)
VALUES (1,'en','US'),
       (2,'fi','FI'),
       (3,'ru','RU'),
       (4,'zh','CN');

INSERT INTO users (username, email, password)
VALUES ('admin', 'admin@example.com', 'admin');

-- Users
INSERT INTO users (id, username, email, password)
VALUES (2, 'John', 'john1@example.com', 'admin'),
       (3, 'Jane', 'jane@gmail.com', 'password'),
       (4, 'Charlie', 'charlie@example.com', 'test123');


-- Groups
INSERT INTO groups (id, name, description, user_id)
VALUES (1, 'ICT23-SW', 'Notes for SEP1 class.', 1),
       (2, 'IT Team', 'Work-related discussions and updates.', 2),
       (3, 'Teacher Group', 'Discussion group for teachers.', 3);

-- Notes
INSERT INTO notes (id, title, text, colour, created_at, updated_at, user_id, group_id)
VALUES (1, 'Meeting Notes', 'Discussed project roadmap.', '#FFFFFF', '2025-01-31 10:30:00', '2025-01-31 12:00:00', 1,
        1),
       (2, 'Team Meeting', 'Discuss project updates.', '#FFFFFF', '2025-01-30 08:15:00', '2025-01-31 09:00:00', 2, 3),
       (3, 'Research Task', 'Read articles on market trends.', '#FFFFFF', '2025-01-29 07:00:00', '2025-01-30 07:30:00',
        3, 1),
       (4, 'AI Presentation', 'Work on introduction section.', '#FFFFFF', '2025-01-28 21:45:00', '2025-01-30 22:10:00',
        1, 2);


-- Is Part Of (User-Group Relationship)
INSERT INTO user_groups (user_id, group_id)
VALUES (1, 3),
       (1, 2),
       (2, 3),
       (3, 1);

-- Figures
INSERT INTO figures (id, link, note_id)
VALUES (1, 'www.figurelink1.com', 1),
       (2, 'www.figurelink2.com', 2),
       (3, 'www.figurelink3.com', 3),
       (4, 'www.figurelink4.com', 4);

-- Categories
INSERT INTO categories (id, name)
VALUES (1, 'Study'),
       (2, 'Meeting'),
       (3, 'Programming');

-- Is Tagged (Note-Category Relationship)
INSERT INTO note_categories (note_id, category_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 1);

INSERT INTO categories_localizations(language_id, category_id, translation)
VALUES (2,1,'Opiskella'),
       (2,2,'Kokous'),
       (2,3,'Ohjelmointi'),
       (3,1,'изучать'),
       (3,2,'встреча'),
       (3,3,'программирование'),
       (4,1,'学习'),
       (4,2,'会议'),
       (4,3,'编程');