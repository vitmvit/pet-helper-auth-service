package by.vitikova.discovery.controller;

import by.vitikova.discovery.auth.JwtDto;
import by.vitikova.discovery.auth.SignInDto;
import by.vitikova.discovery.auth.SignUpCreateDto;
import by.vitikova.discovery.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для авторизации и аутентификации
 */
@Log
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Регистрация нового пользователя
     *
     * @param dto объект SignUpDto с данными для регистрации
     * @return объект ResponseEntity с созданным пользователем типа UserDto и статусом OK
     */
    @PostMapping("/signUp")
    public ResponseEntity<JwtDto> signUp(@RequestBody @Valid SignUpCreateDto dto) {
        return ResponseEntity.ok(authService.signUp(dto));
    }

    /**
     * Авторизация пользователя
     *
     * @param dto объект SignInDto с данными для авторизации
     * @return объект ResponseEntity с JWT-токеном типа JwtDto и статусом OK, если авторизация успешна
     */
    @PostMapping(value = "/signIn")
    public ResponseEntity<JwtDto> signIn(@RequestBody @Valid SignInDto dto) {
        return ResponseEntity.ok(authService.signIn(dto));
    }

    /**
     * Проверка валидности JWT-токена
     *
     * @param auth Bearer-токен
     * @return true, если JWT-токен действителен; false в противном случае
     * @throws JsonProcessingException если возникает ошибка при обработке токена
     */
    @PostMapping("/check")
    public ResponseEntity<Boolean> check(@RequestHeader("Authorization") String auth) throws JsonProcessingException {
        var token = auth.replace("Bearer ", "");
        return ResponseEntity.ok(authService.check(token));
    }
}