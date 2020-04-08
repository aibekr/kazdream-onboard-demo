##### **Rest API на Spring Boot**

Для того чтобы запустить ПО необходимо в консоле набрать команду

```
./startup.sh
```

Если у Вас не установлены Docker demon и doker-compose вот [cсылка](https://docs.docker.com/get-docker/)

В качестве Open API используется Swagger и доступен по ссылке:
```
    http://localhost:8080/api/v1/swagger-ui.html
```

Для локального запуска ПО необходимо запустить RabbitMq:
```
docker-compose -f docker-compose-local.yml up --build -d
```

Для запуска тестирования:
```
./gradlew cleanTest test jacocoTestReport
```
(При запуске тестов необходимо учесть что ПО интегрируется с сервером Rabbit, перед тестированием обязательно запустите docker-compose-local)


Jacoco report html доступен по этому пути:
```
${buildDir}/jacocoHtml
```