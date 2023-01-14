FROM amazoncorretto:19

ADD crying-counter-1.0-SNAPSHOT-all.jar crying-counter-1.0-SNAPSHOT-all.jar

ENTRYPOINT ["java", "-jar", "crying-counter-1.0-SNAPSHOT-all.jar"]