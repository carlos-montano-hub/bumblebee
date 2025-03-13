# beehive-nest

Build image:
```sh
docker build -t beehive-nest .
```

Create container:
```sh
docker run --name beehive-nest-container -p 8080:8080 \
	-e SERVER_PORT=8080 \
	-e DB_URL= \
	-e DB_USER= \
	-e DB_PASSWORD= \
	beehive-nest
```

or load env variables from `.env` file:
```sh
docker run --name beehive-nest-container --env-file .env -p 8080:8080 beehive-nest
```


Env file example:

```sh
SERVER_PORT=8080
DB_URL=jdbc:postgresql://host.docker.internal:5432/
DB_USER=postgres
DB_PASSWORD=password

GUARD_SERVICE_URL=http://host.docker.internal:8852
MIND_SERVICE_URL=http://localhost:5553

S3_ENDPOINT=http://host.docker.internal:9000
S3_ACCESS_KEY=<ACCESS_KEY>
S3_SECRET_KEY=<SECRET_KEY>
API_KEY=<GUARD_SERVICE_API_KEY>
```
