# task-manager

###### Build the image. In the terminal, make sure you're in the working directory.

`docker build -t task-manager-app .`

###### Run your container using the docker run command and specify the name of the image you just created

`docker run -p 8080:8080 task-manager-app`


Postman collection with all requests is in the resource folder.


**Sample curl commands to test endpoints:**

Create user <br />
`curl --location 'localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Petko",
"email": "petko@gmail.bg"
}'`

Get all users <br />
`curl --location 'localhost:8080/users'`

Update user <br />
`curl --location --request PUT 'localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
"id": 1,
"name": "Petko Petkov",
"email": "petko_update@gmail.bg"
}'`


Create Task - <br />
`curl --location 'localhost:8080/tasks' \
--header 'Content-Type: application/json' \
--data '{
"title": "Fisrt Task",
"description": "Initializing task",
"dueDate": "2025-06-21",
"status": "IN_PROGRESS",
"userId": null,
"dependsOn" : null
}'`

Get all tasks<br />
`curl --location 'localhost:8080/tasks'`

Get task by id<br />
`curl --location 'localhost:8080/tasks/1'`

Get all task by user<br />
`curl --location 'localhost:8080/tasks/user/1'`

Update task status<br />
`curl --location --request PUT 'localhost:8080/tasks/1' \
--header 'Content-Type: application/json' \
--data '{
"status": "IN_PROGRESS"
}'`

Update task assignee user<br />
`curl --location --request PUT 'localhost:8080/tasks/1' \
--header 'Content-Type: application/json' \
--data '{
"userId": 1
}'`

Get all task statuses<br />
`curl --location 'localhost:8080/tasks/statuses'`
