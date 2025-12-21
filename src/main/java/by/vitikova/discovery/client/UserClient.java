package by.vitikova.discovery.client;

import by.vitikova.discovery.UserDto;
import by.vitikova.discovery.create.UserCreateDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import reactor.core.publisher.Mono;

public interface UserClient {

    @PostExchange()
    Mono<UserDto> create(@RequestBody UserCreateDto dto);

    @PutExchange("/{login}")
    Mono<UserDto> updateLastVisit(@PathVariable String login);
}