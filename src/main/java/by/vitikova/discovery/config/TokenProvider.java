package by.vitikova.discovery.config;

import by.vitikova.discovery.constant.RoleName;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static by.vitikova.discovery.constant.Constant.GENERATION_TOKEN_ERROR;

@Component
public class TokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    public String generateAccessToken(String login, RoleName role) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withSubject(login)
                    .withClaim("username", login)
                    .withClaim("role", role.getRole())
                    .withExpiresAt(genAccessExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException(GENERATION_TOKEN_ERROR, exception);
        }
    }

    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}