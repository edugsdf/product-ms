FROM maven:3.3-jdk-8-alpine as MAVEN_BUILD

RUN mkdir -p /app_tmp
WORKDIR /app_tmp

COPY . .

RUN mvn clean install

#--- Segunda etapa
FROM maven:3.3-jdk-8-alpine
LABEL Eduardo Gonçalves <edugsdf@gmail.com>

RUN mkdir -p /home/product-ms

WORKDIR /home/product-ms

COPY --from=MAVEN_BUILD /app_tmp/target/product-ms-0.0.1-SNAPSHOT.jar .

EXPOSE 9999

CMD ["java", "-jar", "./product-ms-0.0.1-SNAPSHOT.jar"]
