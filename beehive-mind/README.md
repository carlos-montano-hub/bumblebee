# beehive-mind

Build image:
```sh
docker build -t beehive-mind .
```

Create container:
```sh
docker run --name beehive-mind-container -p 8084:8000 \
	-e JWT_SECRET= \
	-e API_KEY= \
	-e S3_ENDPOINT= \
	-e S3_ACCESS_KEY= \
	-e S3_SECRET_KEY= \
	beehive-mind
```

or load env variables from `.env` file:
```sh
docker run --name beehive-mind-container --env-file .env -p 8084:8000 beehive-mind
```


Env file example:

```sh
JWT_SECRET=<SECRET>
API_KEY=<API_KEY>


S3_ENDPOINT=<ENDPOINT>
S3_ACCESS_KEY=<ACCESS_KEY>
S3_SECRET_KEY=<SECRET_KEY>
```


