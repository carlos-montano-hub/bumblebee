# beehive-guard

Build image:
```sh
docker build -t beehive-guard .
```

Create container:
```sh
docker run --name beehive-guard-container -p 8852:8080 \
	-e SERVER_PORT=8080 \
	-e DB_URL= \
	-e DB_USER= \
	-e DB_PASSWORD= \
	beehive-guard
```

or load env variables from `.env` file:
```sh
docker run --name beehive-guard-container --env-file .env -p 8852:8080 beehive-guard
```


Env file example:

```sh
SERVER_PORT=8080
DB_URL=jdbc:postgresql://host.docker.internal:5432/
DB_USER=postgres
DB_PASSWORD=password

JWT_SECRET=<VALID_256_SECRET_LENGTH>
API_KEY=<ANY_STRING>
```

