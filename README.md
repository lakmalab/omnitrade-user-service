# omnitrade-user-service


docker run -d \
-p 8081:8081 \
-e DB_URL=jdbc:mysql://host.docker.internal:3306/omnitrade \
-e DB_USERNAME=root \
-e DB_PASSWORD=your_password \
omnitrade/omnitrade-user-service:latest