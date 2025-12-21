# pet-helper-auth-service

[Точка входа в приложение](https://github.com/vitmvit/pet-helper-api-gateway-service)

Данный микросервис предназначен для аутентификации и авторизации пользователей с использованием JWT токенов. Сервис
предоставляет REST API для регистрации новых пользователей, входа в систему и проверки валидности JWT токенов.

## Технический стек

- Java 17+
- Spring Boot 3.2.1
- Spring WebFlux (реактивный REST API)
- Spring Security
- Spring Cloud Netflix Eureka Client
- Project Reactor (Mono/Flux)
- Lombok
- SpringDoc OpenAPI
- Auth0 Java JWT
- MapStruct

## Доступ по ролям

- доступен всем

## Swagger

http://localhost:8082/api/doc/swagger-ui/index.html#/

## Порт

```text
8082
```

#### Аутентификация и авторизация:

Реализована аутентификация и авторизация с помощью JWT.

Для создания нового пользователя необходимо отправить POST-запрос на конечную точку с телом, содержащим логин, пароль и
одну из доступных ролей.

Доступные роли:

```text
ADMIN, EDITOR, USER, SUPPORT
```

Вот README.md для вашего pet-helper-auth-service в соответствии с форматом:

```markdown
# pet-helper-auth-service

[Точка входа в приложение](https://github.com/vitmvit/pet-helper-api-gateway-service)

Данный микросервис предназначен для аутентификации и авторизации пользователей с использованием JWT токенов. Сервис
предоставляет REST API для регистрации новых пользователей, входа в систему и проверки валидности JWT токенов.

## Технический стек

- Java 17+
- Spring Boot 3.2.1
- Spring WebFlux (реактивный REST API)
- Spring Security
- Spring Cloud Netflix Eureka Client
- Project Reactor (Mono/Flux)
- Lombok
- SpringDoc OpenAPI
- Auth0 Java JWT
- MapStruct

## Доступ по ролям

- доступен всем

## Swagger

http://localhost:8082/api/doc/swagger-ui/index.html#/

## Порт

```text
8082
```

## Валидация DTO моделей на AuthController

### Таблица валидации SignUpCreateDto

| Поле            | Тип      | Обязательно | Правила валидации           | Сообщение об ошибке                                                     |
|-----------------|----------|-------------|-----------------------------|-------------------------------------------------------------------------|
| login           | String   | Да          | Валидный email формат       | "Email is required"<br>"Email should be valid"                          |
| password        | String   | Да          | Минимум 6 символов          | "Password is required"<br>"Password must be at least 6 characters long" |
| passwordConfirm | String   | Да          | Должно совпадать с password | "Password confirmation is required"                                     |
| role            | RoleName | Да          | Не может быть null          | "Role is required"                                                      |

### Таблица валидации SignInDto

| Поле     | Тип    | Обязательно | Правила валидации     | Сообщение об ошибке                            |
|----------|--------|-------------|-----------------------|------------------------------------------------|
| login    | String | Да          | Валидный email формат | "Email is required"<br>"Email should be valid" |
| password | String | Да          | Любая непустая строка | "Password is required"                         |

## Доступные роли

```text
USER    - обычный пользователь
ADMIN   - администратор системы
SUPPORT - техническая поддержка
EDITOR  - редактор контента
```

## Интеграция с другими сервисами

### Зависимости

- **User Service** (порт 8081) - для создания пользователей и обновления даты последнего визита

### Конфигурация WebClient

Сервис использует реактивный WebClient для взаимодействия с User Service:

```yaml
user-service:
  url: http://localhost:8081
```

## Конфигурация безопасности

### JWT Secret Key

Секретный ключ для подписи JWT токенов настраивается через свойство:

```yaml
security:
  jwt:
    token:
      secret-key: your-secret-key-here
```

## AuthController (8082/api/v1/auth)

Контроллер поддерживает следующие операции:

- регистрация нового пользователя (signUp)
- аутентификация пользователя (signIn)
- проверка валидности JWT токена (check)

#### POST запрос на создание нового пользователя:

##### Успешное создание

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
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdXBwb3J0MTBAbWFpbC5jb20iLCJ1c2VybmFtZSI6InN1cHBvcnQxMEBtYWlsLmNvbSIsInJvbGUiOiJTVVBQT1JUIiwiZXhwIjoxNzY2MzUxNDYxfQ.AW7P4-3MUjw6Tcg88uMfizR11FKuhcyuFD2KPViAkE4"
}
```

##### Пользователь уже существует

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
  "errorMessage": "409 Conflict from POST http://localhost:8081/api/v1/users",
  "errorCode": 400
}
```

#### POST запрос на получение токена аутентификации:

##### Успешная авторизация

Request:

```http request
http://localhost:8082/api/v1/auth/signIn
```

```json
{
  "login": "support10@mail.com",
  "password": "support10@mail.com"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdXBwb3J0MTBAbWFpbC5jb20iLCJ1c2VybmFtZSI6InN1cHBvcnQxMEBtYWlsLmNvbSIsInJvbGUiOiJTVVBQT1JUIiwiZXhwIjoxNzY2MzUyNDEyfQ.QYmJBJP05Ofqi4bcMNKEFh5Tm7lnby5sZ-pWNsU_zdo"
}
```

##### Пользователь не найден

Request:

```http request
http://localhost:8082/api/v1/auth/signIn
```

```json
{
  "login": "support10@mail.com",
  "password": "support10@mail.com"
}
```

Response:

```json
{
  "errorMessage": "404 Not Found from PUT http://localhost:8081/api/v1/users/support11%40mail.com",
  "errorCode": 400
}
```

#### POST запрос на проверку пользователя и его токена:

Необходимо передавать bearer токен в заголовке Authorization.

Request:

```http request
http://localhost:8082/api/v1/auth/check?login=support10@mail.com

```

Response если токен действителен и пользователь имеет доступ к данному функционалу:

```text
true
```

Response если токен не действителен или пользователь не имеет доступа к данному функционалу:

```text
false
```


