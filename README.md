# pet-helper-auth-service

Данный микросервис предназначен для создания и проверки JWT токена, а также проверки доступа пользователя к функционалу.

# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## AuthController (8082/api/v1/auth)

Контроллер поддерживает следующие операции:

- авторизация пользователя
- аутентификация пользователя
- проверка токена на срок жизни

#### Аутентификация и авторизация:

Реализована аутентификация и авторизация с помощью Spring-Boot и JWT.

Для создания нового пользователя необходимо отправить POST-запрос на конечную точку с телом, содержащим логин, пароль и
одну из доступных ролей.

Доступные роли:

```text
ADMIN, USER, SUPPORT
```

## Реализация

### AuthController

#### POST запрос на создание нового пользователя:

Request:

```http request
http://localhost:8082/api/v1/auth/signUp
```

```json
{
  "login": "support10@mail.com",
  "password": "support10@mail.com",
  "passwordConfirm": "support10@mail.com",
  "role": "SUPPORT"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdXBwb3J0MTBAbWFpbC5jb20iLCJ1c2VybmFtZSI6InN1cHBvcnQxMEBtYWlsLmNvbSIsInJvbGUiOiJTVVBQT1JUIiwiZXhwIjoxNzEyMzY4NzQzfQ.lF39N_ZHzt1eC2dMPUMh0E3RuPj30_zjw9FEERdla4U"
}
```

Если пользователь существует:

```json
{
  "errorMessage": "Username is exists",
  "errorCode": 302
}
```

#### POST запрос на получение токена аутентификации:

Request:

```http request
http://localhost:8082/api/v1/auth/signIn
```

```json
{
  "login": "Admin",
  "password": "Admin"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdXBwb3J0MTBAbWFpbC5jb20iLCJ1c2VybmFtZSI6InN1cHBvcnQxMEBtYWlsLmNvbSIsInJvbGUiOiJTVVBQT1JUIiwiZXhwIjoxNzEyMzY4Nzg3fQ.KpBXr2HHD2NElUCRb17YYCz2tCVXiA9f4uAIL4ZFRZY"
}
```

Если пользователь не найден:

```json
{
  "errorMessage": "Username not exists",
  "errorCode": 302
}
```

#### POST запрос на проверку пользователя и его токена:

Необходимо передавать токен в заголовке Authorization.

Request:

```http request
http://localhost:8081/api/v1/auth/check?login=Admin

```

Response если токен действителен и пользователь имеет доступ к данному функционалу:

```text
true
```

Response если токен не действителен или пользователь не имеет доступа к данному функционалу:

```text
false
```


