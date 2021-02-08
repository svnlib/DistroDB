# DistroDB

A distributed and easily scalable key-value database.

Run with

```
./gradlew shadowJar
docker-compose up -d
```

Default ports: `5000-5001`

Message Format:

```
[Operation] (UUID) [payload]...
```

Operations: `INSERT`, `UPDATE`, `DELETE`, `GET`

Example:

```
INSERT Value to be added
UPDATE 2f3...1fe Anothrer value
```