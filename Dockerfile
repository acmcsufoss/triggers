FROM amazoncorretto:19

ADD triggers-1.0-SNAPSHOT-all.jar triggers-1.0-SNAPSHOT-all.jar

ENTRYPOINT ["java", "-jar", "triggers-1.0-SNAPSHOT-all.jar"]