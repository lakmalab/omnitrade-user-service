# omnitrade-user-service


docker run -d -p 8081:8081 --name omnitrade-user-service -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/omnitrade_user_service?useSSL=false&serverTimezone=UTC" -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=1234 -e SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver -e SPRING_FLYWAY_ENABLED=false -e SERVER_PORT=8081 lakmalab/omnitrade-user-service