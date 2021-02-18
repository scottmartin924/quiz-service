# Quiz Serivce

Simple CRUD web service for managing user-defined quizzes. Almost no features, the user portion is not built-out 
can only work with quizzes at the moment. Meant to be a very simple companion to the 
[Quizling](https://github.com/scottmartin924/quizling-coordinator) 
app which would allow you to create quizzes which Quizling could then run matches with. 

### Technologies
Spring Boot, MongoDb

### Running the app
Run the app with `mvn spring-boot:run`

### Configuration options
In order to run the app you'll need to point it to a Mongo database. The connection details
for this can be found in `src/main/resources/application.yaml` in the `spring.data.mongodb` property
--by default `host: localhost`, `port: 27017`.

Similarly you can edit the port the web server runs on using the `server.port` property