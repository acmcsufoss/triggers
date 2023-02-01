FROM amazoncorretto:19

COPY triggers.jar triggers.jar

ENTRYPOINT ["java", "-jar", "triggers.jar"]

