# REFERENCE
[Doc API](https://auth.api.restaurant.dattpmars.com/swagger-ui/index.html)
# change password
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '[newpassword]';
# config mysql docker
sudo docker pull mysql
sudo docker network create springboot-mysql-net
sudo docker network ls
sudo docker run --name mysql --network springboot-mysql-net -e MYSQL_ROOT_PASSWORD=dattp -e MYSQL_DATABASE=PRODUCT_DB -d mysql
sudo docker exec -it mysql bash
mysql>create database PRODUCT_DB;
# run app
sudo docker pull dattp6501/restaurent-product-service:first
sudo docker run --network springboot-mysql-net --name restaurent-product-service -p 9001:9001 dattp6501/restaurent-product-service:first
# cicd
mkdir actions-runner && cd actions-runner
curl -o actions-runner-linux-x64-2.314.1.tar.gz -L https://github.com/actions/runner/releases/download/v2.314.1/actions-runner-linux-x64-2.314.1.tar.gz
echo "6c726a118bbe02cd32e222f890e1e476567bf299353a96886ba75b423c1137b5  actions-runner-linux-x64-2.314.1.tar.gz" | shasum -a 256 -c
tar xzf ./actions-runner-linux-x64-2.314.1.tar.gz
./config.sh --url https://github.com/dattp6501/notification --token AYYGNOPAH2E5JLJWPWSEFYTF5V3CW
./run.sh