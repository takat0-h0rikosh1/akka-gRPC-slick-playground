# akka gRPC + Slick Playground

## prepare

```
docker run -d --name myapp-db -e POSTGRES_USER=myapp -e POSTGRES_PASSWORD=myapp -e POSTGRES_DB=myapp -p 5432:5432 postgres
docker start myapp-db
sbt "flyway/flywayMigrate"
```

## start gRPC server

```
sbt run

evans -r -p 8080 --host 127.0.0.1
> call getAll
```



