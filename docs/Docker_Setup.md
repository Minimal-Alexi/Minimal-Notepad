# Docker
The docker is a containerization technology which allows the API to run on any machine and in any environment using cloud technology.

## Initialization Steps
1. Set up the .env file for cloud usage. Change the db_host to `DB_HOST_DOCKER` in the .env.template file.
2. Clean and install the application.
3. Build the dockers, first by having the maria_db service build, and then by having the app service build.
4. Ensure the maria-db service has the tables initialized. If not, run the following command in the maria-db client: `source ./docker-entrypoint-initdb.d/init-db.sql`
5. Ensure the app service is running properly. If the app is trying to connect to localhost delete the app service and run `docker-compose build --no-cache`.

Congrats, you have dockerized the app.