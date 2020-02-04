DROP DATABASE IF EXISTS zapdb;

GRANT USAGE ON *.* TO 'zapuser'@'localhost';

DROP USER 'zapuser'@'localhost';

CREATE USER 'zapuser'@'localhost' IDENTIFIED BY 'zappazz';

CREATE DATABASE zapdb;

GRANT ALL PRIVILEGES ON zapdb.* to 'zapuser'@'localhost' IDENTIFIED BY 'zappazz';
