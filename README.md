#Restaurant voting api for deciding where to have lunch

###[TopJava Internship](https://javaops.ru/view/topjava) graduation project
- <a href="https://github.com/glanbor/RestVoting.git">GitHub</a> project reference

##Task:

####Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.
Build a voting system for deciding where to have lunch.

- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
- If user votes again the same day:
  - If it is before 11:00 we assume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed
- Each restaurant provides a new menu each day.
- Make sure everything works with latest version that is on github
- Assume that your API will be used by a frontend developer to build frontend on top of that.

##Stack:
Maven, Spring Boot, Spring MVC, Spring Security, Spring DataJPA, REST(Jackson), Swagger/OpenAPI, HSQLDB, Lombok, JUnit 5
_____

[REST API documentation](http://localhost:8080/rest/swagger-ui.html) by Swagger