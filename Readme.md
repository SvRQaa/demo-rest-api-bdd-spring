# Description

#### This project is Rest API Application build using Java Spring

### There are two concepts.
#### First: Spring repositories to CRUD entities.
#### Integrated into Spring with BDD test approach.
#### Second: REST API to CRUD entities.
#### It's dependent on a running DemoApplication class, otherwise all your requests will hit nothing.
#### Also with BDD.

Maven wrapper is included and JAVA version 21 is expected. (>sdk use java 21.0.2-zulu)
Local mvn -v 3.8.8

## how to run

drop all and start application
> ./mvnw clean spring-boot:run
> ./mvnw verify

html report location

Cucumber Report:
> /target/cucumber-report/cucumber.html

Jacoco Report:
> /target/generated-report/index.html
 
Terminal manual checks:
> curl -X GET http://localhost:8080/users/1

