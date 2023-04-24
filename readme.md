### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### Your experience in Java

Please let us know more about your Java experience in a few sentences:

- I've been using java for a long time (more than 10 years) but most of my experience is in java 7 & 8, although I've been using Grails for almost 5 years
- Started learning and using Spring Boot last November

#### Additional Comments

- Added validations to the API calls 
- Added error handling
- Since the database implementation is on memory, added a library to simulate the database for the tests
- Things left to do because I ran out of time:
  - Authentication Implementation: Ran into an obscure error with Springfox swagger and Spring Basic Auth, that took too long to resolve, so it was removed.
  - Return a success message for save, delete, and update calls
  - Modify the validators to not have repeated classes
  

