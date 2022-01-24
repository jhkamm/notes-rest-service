# Notes REST Service for DISQO Interview
Provides a simple RESTful API for creating, reading, updating and deleting notes.
Uses SpringBoot framework for RESTful API functionality, MySQL for storage and Gradle for building.

Dependencies for running:
* gradle
* docker
* docker-compose

# To run
Clone the repository and run the application with docker-compose:
```
git clone https://github.com/jhkamm/notes-rest-service.git
cd notes-rest-service
./gradlew build
sudo docker-compose up
```

# Example CURL requests to test the API
## Create a note
Create a note

`curl -v -u user1@example.com:password1 -X POST "localhost:8080/notes/add" -H 'Content-type: application/json' -d '{"title": "{title}", "note": "{note-text}"}'`

`curl -v -u user2@example.com:password2 -X POST "localhost:8080/notes/add" -H 'Content-type: application/json' -d '{"title": "{title}", "note": "{note-text}"}'`
## Read note(s)
Get all notes in the system

`curl -v -u user1@example.com:password1 "localhost:8080/notes/all"`

`curl -v -u user2@example.com:password2 "localhost:8080/notes/all"`

Get a note by note ID

`curl -v -u user1@example.com:password1 "localhost:8080/notes/{id}"`

`curl -v -u user2@example.com:password2 "localhost:8080/notes/{id}"`

## Update note by id
Update a note

`curl -v -u user1@example.com:password1 -X PUT "localhost:8080/notes/{id}" -H 'Content-type: application/json' -d '{"title": "{title}", "note": "{note-text}"}'`

`curl -v -u user2@example.com:password2 -X PUT "localhost:8080/notes/{id}" -H 'Content-type: application/json' -d '{"title": "{title}", "note": "{note-text}"}'`

## Delete note by id
Delete a note

`curl -v -u user1@example.com:password1 -X DELETE "localhost:8080/notes/{id}"`

`curl -v -u user2@example.com:password2 -X DELETE "localhost:8080/notes/{id}"`
