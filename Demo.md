## This is a Demo project for the technical task.
### Requirements:

• usage of Java programming language

• test web service with REST API and other aspects in Java and Cucumber

    assumption: rest api must be implemented in the same project.
    Spring-boot was selected to serve that role.
    Why?
    Unknown tool - a lot of learning = a lot of takeaways.
    Also let us use latest versions of all libraries to make it even more challenging.

• publish as open source in GitHub/GitLab/Bitbucket

• provide README file in order to execute tests

• provide test report


### Test infrastructure highlights:
### Runners

- simple classes to mass configure each Cucumber test.
- Each Runner is the entry point for the test with its unique configuration.
- RunCucumberTest - is an entry point for mvn verify.
- RunnerIT - also a Runner class, but they are configured differently.
- cucumber options vs junit suite config.
- Every runner is pointed to all features.
- You must control the scope using TAGS.

### Steps
scenario step definition per context
- Steps - general steps for direct usage of the Entity repositories
- RestSteps - same but the Entities are manager using the rest endpoints
- StepHooks - precondition and cleanup procedures
- CoreRegressionStepDefs - to demonstrate dependency injection mechanism
### Features
>src/test/resources/tse/api/demo/feature/injection/dependency.feature

cucumber-spring dependency injection feature
### Reporting:

Having current configuration every Cucumber execution will produce cucumber.json and cucumber.html
Additionally on mvn verify we have a plugin configured to produce one more HTML report.
>target/generated-report/index.html  

in post-integration test phase.

### Conclusion
The above framework has very high potential.
Because it can run
    -locally from IDE
    -locally using mvn verify
    -on agent using ./mvnw verify (+git +java)
Using the Runner classes gives full control over the scope of execution.
Has proper reporting configured.
Has very simple dependency injection mechanics enabling data sharing between the steps.

### Potential Enablers:

concurrent execution configuration

docker container + docker-compose.yml

report aggregation to build test trends

extend reporting with more metadata to improve traceability

## Additional features
### Swagger
Run the DemoApplication.
Access Swagger UI @ http://localhost:8080/swagger-ui/index.html

