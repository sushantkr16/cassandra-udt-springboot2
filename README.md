# Getting Started
This is a sample project which shows how to make prometheus working with spring boot 2 application
 
 * Make sure the application yml file has below config details.
 * Start the application and hit the below URL.

### Application configs

management:
  health.cassandra.enabled: false
  metrics:
    export:
      prometheus:
        enabled: true
  endpoints:
    prometheus:
      enabled: true
    web:
      exposure:
        include: "*"
    metrics.enabled: true

### API reference

Rest service : 

* Http Method : GET
* URL : http://localhost:9095/sk/spring-boot2/prometheus

### Adding Micrometer Prometheus Registry to your Spring Boot application

* Spring boot 2 uses Micrometer, an application metrics facade to integrate actuator metrics with 
  external monitoring systems. It supports several monitoring systems like Netflix Atlas, AWS Cloudwatch, Prometheus etc.

### API to get the metrics (which prometheus uses)

* Http Method : GET
* URL : http://localhost:9096/sk/actuator/prometheus

### Integrate with Prometheus and Grafana


  
  TAGS : ksushant, sushant, spring boot 2, docker