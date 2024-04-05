package by.vitikova.discovery.service;

import by.vitikova.discovery.auth.JwtDto;
import by.vitikova.discovery.auth.SignInDto;
import by.vitikova.discovery.auth.SignUpCreateDto;
import by.vitikova.discovery.auth.SignUpDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthService {

    JwtDto signUp(SignUpCreateDto dto);

    JwtDto signIn(SignInDto dto);

    boolean check(String token) throws JsonProcessingException;
}