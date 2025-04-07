FROM eclipse-temurin:17-jre-alpine
COPY /target/JobOffers.jar /JobOffers.jar
ENTRYPOINT ["java","-jar","/JobOffers.jar"]