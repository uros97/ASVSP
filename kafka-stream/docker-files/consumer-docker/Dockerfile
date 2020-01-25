FROM maven AS maven_build

COPY ./primeri /usr/consumers
RUN mvn -f /usr/consumers/pom.xml clean package

FROM openjdk:8
COPY --from=maven_build /usr/consumers/target /usr/consumers
ENTRYPOINT ["java","-cp","/usr/consumers/kafka-streams-examples-0.0.1-SNAPSHOT.jar", "com.example.kafka_streams_projekat.DelayTypeAverage"]
#ENTRYPOINT ["/bin/bash"]
