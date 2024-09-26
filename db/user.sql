CREATE USER 'petauser'@'%' IDENTIFIED BY 'petauserPassword@123';

GRANT ALL PRIVILEGES ON *.* TO 'petauser'@'%' WITH GRANT OPTION;