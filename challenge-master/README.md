# NDVR Java Coding Challenge

## Description
This application fetches market data of stocks identified by their ticker symbol, e.g.: `LOGM` from Yahoo Finance, 
and projects future prices.

### Usage
The microservice starts up a vanilla Spring Boot app that will listen on http://localhost:8080

For simpler debugging, swagger-ui is enabled on http://localhost:8080/swagger-ui.html 

## Technical prerequisites
The application is built by maven.

    mvn spring-boot:run

The application uses [Lombok](https://projectlombok.org/). For the best experience, you'll need to
install the Lombok Plugin of your favourite IDE, and enable annotation processing.

