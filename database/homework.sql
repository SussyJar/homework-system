CREATE DATABASE homework_system;
USE homework_system;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('student','teacher','admin') NOT NULL,
    name VARCHAR(100),
    email VARCHAR(100),
    status ENUM('active','disabled') DEFAULT 'active'
);


SELECT * FROM homework;

-- CREATE TABLE login_logs (
--     log_id INT AUTO_INCREMENT PRIMARY KEY,
--     user_id INT NOT NULL,
--     role VARCHAR(20),
--     login_time DATETIME DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (user_id) REFERENCES users(user_id)
-- );

CREATE TABLE login_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

UPDATE login_logs
SET role = TRIM(role);
DESCRIBE login_logs;
ALTER TABLE login_logs
MODIFY role VARCHAR(20) AFTER user_id;

SHOW VARIABLES LIKE 'character_set%';
SHOW VARIABLES LIKE 'collation%';



CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    teacher_id INT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES users(user_id)
);
ALTER TABLE courses
MODIFY teacher_id INT NULL;

SELECT user_id, username, name, role
FROM users
WHERE role = 'student';
SELECT user_id, username, name, role
FROM users
WHERE role = 'student';

DESC courses;
DESC homework;
DESC submissions;


CREATE TABLE courses_users (
    course_id INT,
    user_id INT,
    PRIMARY KEY (course_id, user_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
DESCRIBE homework_reminders;
SELECT * FROM courses_users WHERE user_id = 4;

SELECT * FROM homework;

SELECT * FROM courses_users;


INSERT INTO courses_users VALUES (1, 3);
INSERT INTO courses_users VALUES (1, 4);


CREATE TABLE homework (
    homework_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    teacher_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    difficulty VARCHAR(20),
    deadline DATETIME NOT NULL,
    allowed_format VARCHAR(50),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (teacher_id) REFERENCES users(user_id)
);

DESCRIBE homework;
DESCRIBE courses;
Describe courses_users;

CREATE TABLE submissions (
    submission_id INT AUTO_INCREMENT PRIMARY KEY,
    homework_id INT NOT NULL,
    student_id INT NOT NULL,
    file_path VARCHAR(255),
    submit_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('submitted','late','resubmitted') DEFAULT 'submitted',
    FOREIGN KEY (homework_id) REFERENCES homework(homework_id),
    FOREIGN KEY (student_id) REFERENCES users(user_id)
);

ALTER TABLE submissions
DROP COLUMN role,
ADD COLUMN rubric TEXT VARCHAR(50);

CREATE TABLE grades (
    grade_id INT AUTO_INCREMENT PRIMARY KEY,
    submission_id INT NOT NULL,
    score INT,
    feedback TEXT,
    graded_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    released BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (submission_id) REFERENCES submissions(submission_id)
);

CREATE TABLE homework_reminders (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY,
    homework_id INT NOT NULL,
    student_id INT NOT NULL,
    remind_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (homework_id) REFERENCES homework(homework_id),
    FOREIGN KEY (student_id) REFERENCES users(user_id),

    UNIQUE (homework_id, student_id)
);

DESCRIBE submissions;




INSERT INTO users (username, password, role, name)
VALUES ('admin', 'admin123', 'admin', 'System Admin');

INSERT INTO users (username, password, role, name)
VALUES ('Yatno', 'teacher123', 'teacher', 'yanto');

INSERT INTO users (username, password, role, name)
VALUES ('BudiDisini', 'student123', 'student', 'Budi');

DELETE FROM users;

INSERT INTO users (username, password, role, name, status)
VALUES
('admin', 'admin123', 'admin', 'System Admin', 'active'),
('Yanto', 'teacher123', 'teacher', 'Yanto', 'active'),
('BudiDisini', 'student123', 'student', 'Budi', 'active');

SELECT username, password, status
FROM users;

-- reset auto increment
TRUNCATE TABLE users;

DELETE FROM login_logs;
DELETE FROM users;

ALTER TABLE users AUTO_INCREMENT = 1;

-- login_logs check
SELECT * FROM login_logs;

-- homework check
SELECT * FROM homework;

-- users check
SELECT user_id, role FROM users;

-- courses check
SELECT * FROM courses;



-- check users
SELECT user_id, username, role FROM users;

-- insert sample course with new teacher_id
INSERT INTO homework
(course_id, teacher_id, title, description, difficulty, deadline, allowed_format)
VALUES
(
  1,       -- course_id (sesuaikan)
  2,       -- teacher_id BARU 
  'Introduction',
  'Introduction yourself to the class',
  'Medium',
  '2025-12-30 23:59:00',
  'pdf,zip'
);

-- insert sample course
INSERT INTO courses (course_name, teacher_id)
VALUES ('Introduction with Yanto', 2);

DELETE FROM homework

TRUNCATE Table homework;

DELETE FROM submissions;
DELETE FROM homework;

ALTER TABLE homework AUTO_INCREMENT = 1;

SELECT * FROM submissions;


ALTER TABLE submissions
ADD COLUMN score INT,
ADD COLUMN feedback TEXT;

-- deadline update
UPDATE homework
SET deadline = '2026-01-01 00:00:00'
WHERE homework_id = 1;

SELECT submission_id, status FROM submissions;

SELECT submission_id, status
FROM submissions
ORDER BY submission_id DESC;

-- add unique constraint to prevent multiple submissions
ALTER TABLE submissions
ADD UNIQUE KEY uniq_homework_student (homework_id, student_id);


SELECT submission_id, homework_id, student_id, file_path, status, submit_time
FROM submissions
WHERE homework_id = 1 AND student_id = 3;

SELECT * FROM login_logs;
ALTER TABLE submissions
MODIFY status ENUM('submitted','late','graded') NOT NULL;
