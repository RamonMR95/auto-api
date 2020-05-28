FROM openjdk:11.0.7-jre-slim
ADD libs/apache-activemq-5.14.5 activemq/apache-activemq-5.14.5
ADD startup.sh startup.sh
ADD target/auto-api.jar auto-api.jar
EXPOSE 61616
EXPOSE 8080
CMD ["/bin/bash", "startup.sh"]
