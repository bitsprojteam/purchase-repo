FROM openjdk:8
VOLUME /tmp
EXPOSE 9002
ADD target/purchase-service-1.0.jar purchase-service-1.0.jar 
ENTRYPOINT ["java","-jar","/purchase-service-1.0.jar"]