sudo docker pull mysql/mysql-server:latest
sudo docker run --name=mysql -d mysql/mysql-server:latest -p 3306:3306
sudo docker logs mysql
  username: root
  password: [Entrypoint] GENERATED ROOT PASSWORD
sudo docker exec -it mysql /bin/bash
mysql -u root -p
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '[newpassword]';
mysql>create database PRODUCT_DB;

# config mysql
sudo docker pull mysql
sudo docker network create springboot-mysql-net
sudo docker network ls
sudo docker run --name mysql --network springboot-mysql-net -e MYSQL_ROOT_PASSWORD=dattp -e MYSQL_DATABASE=PRODUCT_DB -d mysql
sudo docker exec -it mysql bash
mysql>create database PRODUCT_DB;
# run app
sudo docker pull dattp6501/restaurent-product-service:first
sudo docker run --network springboot-mysql-net --name restaurent-product-service -p 9001:9001 dattp6501/restaurent-product-service:first