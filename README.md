
### Запуск проекта:
+ Создать базу данных командой ```create database students```;
+ Запустить Zookeeper (docker-compose up zookeeper)
+ Запустить Kafka (docker-compose up kafka)
+ Запустить minIO (docker-compose up minio)
+ Запустить сервис Server(Сервис S) ```cd spring-rest-kafka-postgre-minio\client``````mvn spring-boot:run```;
+ Запустить сервис Client(Сервис R)```cd spring-rest-kafka-postgre-minio\server``````mvn spring-boot:run```;

### Примеры команд для браузера:
+ http://localhost:8080/getAllUnits
+ http://localhost:8080/getOneUnit/1
+ http://localhost:8080/getOneUnit/2
+ http://localhost:8080/getOneUnit/3
