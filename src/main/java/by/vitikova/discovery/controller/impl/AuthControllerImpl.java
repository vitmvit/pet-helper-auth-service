package by.vitikova.discovery.controller.impl;

import by.vitikova.discovery.auth.JwtDto;
import by.vitikova.discovery.auth.SignInDto;
import by.vitikova.discovery.auth.SignUpCreateDto;
import by.vitikova.discovery.controller.AuthController;
import by.vitikova.discovery.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public Mono<ResponseEntity<JwtDto>> signUp(@RequestBody @Valid SignUpCreateDto dto) {
        return authService.signUp(dto).map(ResponseEntity::ok);
    }

    @PostMapping("/signIn")
    public Mono<ResponseEntity<JwtDto>> signIn(@RequestBody @Valid SignInDto dto) {
        return authService.signIn(dto).map(ResponseEntity::ok);
    }

    @PostMapping("/check")
    public Mono<ResponseEntity<Boolean>> check(@RequestHeader("Authorization") String auth) throws JsonProcessingException {
        String token = auth.startsWith("Bearer ") ? auth.substring(7) : auth;
        return authService.check(token)
                .map(ResponseEntity::ok);
    }
}