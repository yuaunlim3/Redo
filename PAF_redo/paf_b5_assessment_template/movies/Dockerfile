FROM openjdk:23-jdk-oracle AS builder

WORKDIR /src

COPY src src
COPY data data

COPY ./mvnw .
COPY pom.xml .
COPY .mvn .mvn


RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

FROM openjdk:23-jdk-oracle

WORKDIR /app

COPY --from=builder /src/target/movies-0.0.1-SNAPSHOT.jar.original app.jar
COPY data/movies_post_2010.zip data/
COPY data/director_movies_report.jasper data/

ENV PORT=8080
ENV SPRING_DATA_NAME=name
ENV SPRING_DATA_BATCH=batch
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=password
ENV SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/movies\
ENV SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/movies

EXPOSE {$PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar