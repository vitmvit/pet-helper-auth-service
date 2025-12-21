package by.vitikova.discovery.service.impl;

import by.vitikova.discovery.auth.JwtDto;
import by.vitikova.discovery.auth.SignInDto;
import by.vitikova.discovery.auth.SignUpCreateDto;
import by.vitikova.discovery.config.TokenProvider;
import by.vitikova.discovery.constant.RoleName;
import by.vitikova.discovery.converter.UserConverter;
import by.vitikova.discovery.exception.EntityNotFoundException;
import by.vitikova.discovery.exception.InvalidJwtException;
import by.vitikova.discovery.client.UserClient;
import by.vitikova.discovery.service.AuthService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserClient userClient;
    private final UserConverter userConverter;
    private final TokenProvider tokenProvider;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Override
    public Mono<JwtDto> signUp(SignUpCreateDto dto) {
        log.info("AuthService: check sign up");
        return userClient.create(userConverter.convert(dto))
                .map(userDto -> buildJwt(dto.login(), userDto.getRole()))
                .onErrorMap(e -> new InvalidJwtException(e.getMessage()));
    }

    @Override
    public Mono<JwtDto> signIn(SignInDto dto) {
        log.info("AuthService: check sign in");
        return userClient.updateLastVisit(dto.login())
                .map(userDto -> buildJwt(dto.login(), userDto.getRole()))
                .switchIfEmpty(Mono.error(new EntityNotFoundException()))
                .onErrorMap(e -> !(e instanceof EntityNotFoundException),
                        e -> new InvalidJwtException(e.getMessage()));
    }

    @Override
    public Mono<Boolean> check(String token) {
        log.info("AuthService: check user");
        return Mono.fromCallable(() -> {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWT.require(algorithm)
                    .build()
                    .verify(token);
            return true;
        }).onErrorReturn(false);
    }

    private JwtDto buildJwt(String login, RoleName role) {
        return new JwtDto(tokenProvider.generateAccessToken(login, role));
    }
}