CREATE DATABASE notes_db;
CREATE USER 'springuser'@'%' identified BY 'password';
GRANT all ON notes_db.* TO 'springuser'@'%';