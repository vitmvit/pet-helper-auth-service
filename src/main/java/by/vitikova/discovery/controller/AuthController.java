package by.vitikova.discovery.controller;

import by.vitikova.discovery.ErrorDto;
import by.vitikova.discovery.auth.JwtDto;
import by.vitikova.discovery.auth.SignInDto;
import by.vitikova.discovery.auth.SignUpCreateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Authentication", description = "API для регистрации, аутентификации и проверки токенов")
public interface AuthController {

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя и возвращает JWT токен"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно зарегистрирован",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JwtDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            Невалидные данные:
                            - Логин уже существует
                            - Пароль не соответствует требованиям
                            - Поля не прошли валидацию
                            """,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/signUp")
    Mono<ResponseEntity<JwtDto>> signUp(SignUpCreateDto dto);

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Аутентифицирует существующего пользователя и обновляет дату последнего визита"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно аутентифицирован",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JwtDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидные данные для аутентификации",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/signIn")
    Mono<ResponseEntity<JwtDto>> signIn(SignInDto dto);

    @Operation(
            summary = "Проверка валидности JWT токена",
            description = "Проверяет валидность JWT токена"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Токен валиден",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "boolean", example = "true")
                    )
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Токен невалиден",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "boolean", example = "false")
                    )
            )
    })
    @PostMapping("/check")
    Mono<ResponseEntity<Boolean>> check(String auth) throws JsonProcessingException;
}