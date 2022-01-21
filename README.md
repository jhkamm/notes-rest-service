# Notes REST Service for DISQO Interview
Provides a simple RESTful API for creating, reading, updating and deleting notes.
Uses SpringBoot framework for RESTful API functionality and MySQL for storage.

Supports both Gradle and Maven as build systems.

# To run
Clone the repository:
`git clone git@github.com:jhkamm/notes-rest-service.git`

Run the application with the Gradle wrapper:
`./gradlew bootRun`

Run the application with the Maven wrapper:
`./mvnw spring-boot:run`

# Example CURL requests to test the API
## Create a note
Create a note

`curl -v -X POST "localhost:8080/notes/add" -H 'Content-type: application/json' -d '{"title": "{title}", "note": "{note-text}"}'`
## Read note(s)
Get all notes in the system
`curl -v "localhost:8080/notes/all"`
Get a note by note ID
`curl -v "localhost:8080/notes/{id}"`

## Update note by id
Update a note
`curl -v -X PUT "localhost:8080/notes/{id}" -H 'Content-type: application/json' -d '{"title": "{title}", "note": "{note-text}"}'`

## Delete note by id
Delete a note
`curl -v -X DELETE "localhost:8080/notes/{id}"`
