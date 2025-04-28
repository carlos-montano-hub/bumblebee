import os, uvicorn


def main():
    host = os.getenv("HOST", "127.0.0.1")
    port = int(os.getenv("MIND_SERVICE_PORT", 8080))
    uvicorn.run("app.main:app", host=host, port=port, reload=True)


if __name__ == "__main__":
    main()
