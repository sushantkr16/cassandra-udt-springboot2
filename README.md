# Getting Started
This is a sample project for Dockerizing a Spring Boot Application. It has a basic docker file 
and a rest api. It can be validated by doing below steps 
 
 * docker build 
 * docker run (This will bring tomcat up)
 * hitting the rest API 

### API reference

Rest service : 

* Http Method : GET
* URL : http://localhost:9095/sk/spring-boot2/docker

### Docker commands

* To build (don't forget the dot at the last of the command)

   docker build -t spring-boot2-docker .

* To Run and validate the service:

  docker run -p 9095:9095 spring-boot2-docker
  
  
  TAGS : ksushant, sushant, spring boot 2, docker