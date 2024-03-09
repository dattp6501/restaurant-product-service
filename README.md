sudo docker pull mysql/mysql-server:latest
sudo docker run --name=mysql -d mysql/mysql-server:latest
sudo docker logs mysql
  username: root
  password: [Entrypoint] GENERATED ROOT PASSWORD
sudo docker exec -it mysql /bin/bash
mysql -u root -p
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '[newpassword]';
mysql>create database PRODUCT_DB;
