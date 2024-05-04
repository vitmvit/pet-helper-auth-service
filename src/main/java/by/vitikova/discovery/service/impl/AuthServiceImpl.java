package by.vitikova.discovery.service.impl;

import by.vitikova.discovery.auth.JwtDto;
import by.vitikova.discovery.auth.SignInDto;
import by.vitikova.discovery.auth.SignUpCreateDto;
import by.vitikova.discovery.config.TokenProvider;
import by.vitikova.discovery.converter.UserConverter;
import by.vitikova.discovery.exception.EntityNotFoundException;
import by.vitikova.discovery.exception.InvalidJwtException;
import by.vitikova.discovery.feign.UserClient;
import by.vitikova.discovery.model.entity.User;
import by.vitikova.discovery.service.AuthService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static by.vitikova.discovery.constant.Constant.*;

/**
 * Реализация сервиса аутентификации
 */
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserClient userClient;
    private final UserConverter userConverter;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    /**
     * Метод для регистрации нового пользователя
     *
     * @param dto объект SignUpDto, содержащий данные для регистрации
     * @return объект JwtDto, содержащий JWT-токен
     */
    @Transactional
    public JwtDto signUp(SignUpCreateDto dto) {
        if(!dto.password().equals(dto.passwordConfirm())){
            throw new InvalidJwtException(PASSWORD_ERROR);
        }

        if (Boolean.TRUE.equals(userClient.existsByLogin(dto.login()).getBody())) {
            logger.error("AuthService: Invalid Jwt with message: " + USERNAME_IS_EXIST);
            throw new InvalidJwtException(USERNAME_IS_EXIST);
        }
        logger.info("AuthService: check sign up");

        String encryptedPassword = passwordEncoder.encode(dto.password());
        User newUser = new User(dto.login(), encryptedPassword, dto.role(), LocalDateTime.now(), LocalDateTime.now());
        userClient.create(userConverter.convertToUser(newUser));
        return buildJwt(dto.login(), dto.password());
    }

    /**
     * Метод для аутентификации пользователя
     *
     * @param dto объект SignInDto, содержащий данные для аутентификации
     * @return объект JwtDto, содержащий JWT-токен
     */
    @Override
    public JwtDto signIn(SignInDto dto) {
        try {
            if (!userClient.existsByLogin(dto.login()).getBody()) {
                logger.error("AuthService: Invalid Jwt with message: " + USERNAME_NOT_EXIST);
                throw new InvalidJwtException(USERNAME_NOT_EXIST);
            }
            logger.info("AuthService: check sign in");

            userClient.updateLastVisit(dto.login());
            return buildJwt(dto.login(), dto.password());
        } catch (Exception e) {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Проверка прав доступа пользователя по JWT токену.
     *
     * @param token JWT токен
     * @return true, если у пользователя есть доступ, иначе false
     */
    @Override
    public boolean check(String token) {
        logger.info("AuthService: check user");
        return checkToken(token);
    }

    /**
     * Проверяет валидность JWT токена.
     *
     * @param token JWT токен
     * @return true, если токен валиден, иначе false
     */
    private boolean checkToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Метод для создания и возвращения JWT-токена
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return объект JwtDto, содержащий JWT-токен
     */
    private JwtDto buildJwt(String login, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        var authUser = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenProvider.generateAccessToken((User) authUser.getPrincipal());
        return new JwtDto(accessToken);
    }
}