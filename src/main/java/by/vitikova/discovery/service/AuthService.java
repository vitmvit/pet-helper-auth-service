package by.vitikova.discovery.service;

import by.vitikova.discovery.auth.JwtDto;
import by.vitikova.discovery.auth.SignInDto;
import by.vitikova.discovery.auth.SignUpCreateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<JwtDto> signUp(SignUpCreateDto dto);

    Mono<JwtDto> signIn(SignInDto dto);

    Mono<Boolean> check(String token) throws JsonProcessingException;
}