FROM openjdk:8-alpine
ADD target/api-exchange-rate-1.0.jar .
ENTRYPOINT ["/usr/bin/java", "-jar", "/api-exchange-rate-1.0.jar"]