## KumaBudget API

### Environment variables needed

```dotenv
SPRING_PROFILES_ACTIVE=postgres,security,debug
PORT=8000

POSTGRES_HOST=your-db-host
POSTGRES_DB=your-db-name
POSTGRES_USER=your-db-credentials
POSTGRES_PASSWORD=your-db-password
DDL_AUTO=update

JWT_SECRET=your-jwt-secret
TOKEN_EXPIRATION=your-token-expiration-in-ms
REFRESH_TOKEN_EXPIRATION=your-refresh-token-expiration-in-ms
PEPPER=your-pepper-password-key
```

### Run the project in Local mode
1. add `.env` file at root with the requested environment variables in different `application.properties` files
2. determine which profiles to run and add them in environment variable `SPRING_PROFILES_ACTIVE` separated by comma: 
- `h2` or `postgres` as database
- `security` is mandatory
- if you want `debug` logs and SQL trace
3. enter command line:
```bash
mvn spring-boot:run
```
4. add `-DskipTests` to skip tests
```bash
mvn spring-boot:run -DskipTests
```

### Deploy in Prod
1. always make sure the database tables are updated with the last changes because a validation is made at deployment
2. java version can't be higher than 20
3. push on `main` branch on Github, rest is automated 🙂
- the `system.properties` file is mandatory for Koyeb
- the environment variables and secrets are set in Koyeb