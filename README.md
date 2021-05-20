League STORAGE - the system for managing media files within FreeTon League Projects and communication with cloud storage 
---
Tags: #java, #springboot, #hibernate, #jpa, #springSecurity, #AWS, #postgreSQL

Project uses: JDK11, Digital Ocean as AWS-client, Remote/Local PostgreSQL server.


### Requirements
**league-storage**
- Available ports: 7702
- Installed: gradle and jdk11 for development
- Installed: docker and docker-compose Windows / Mac / Linux for deploy


### Configuration for start of project:

1. Use src/main/resources/application.yml for configure connection to DB PostgreSQL and AWS Client (digitalOcean).
2. Use external (dev) PostgreSQL server or local server.
All docker infra-images (DB, Message Broker, e.t.c) contain in "freetonleague-core" project. See `docker` folder 
```
# Open project directory
$ cd ./league-starage
# Build image
$ docker-compose -p freeton-league -f docker/docker-compose.yml build
# Run docker image
$ docker-compose -p freeton-league -f docker/docker-compose.yml up
# Test project on: localhost:7702 
```
