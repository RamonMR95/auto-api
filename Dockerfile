FROM openjdk:11.0.7-jre-slim
ADD libs/payara-micro-5.201.jar payara-micro-5.201.jar
ADD libs/apache-activemq-5.14.5 activemq/apache-activemq-5.14.5
ADD target/auto-api.war auto-api.war
ADD startup.sh startup.sh
EXPOSE 61616
EXPOSE 8080
CMD ["/bin/bash", "startup.sh"]
