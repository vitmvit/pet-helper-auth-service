package by.vitikova.discovery.feign;

import by.vitikova.discovery.UserDto;
import by.vitikova.discovery.create.UserCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Feign-клиент для взаимодействия с микросервисом управления пользователями.
 */
@FeignClient(contextId = "userClient", value = "${feign.user-service.value}", url = "${feign.user-service.url}")
public interface UserClient {

    /**
     * Находит пользователя по его логину.
     *
     * @param login логин пользователя
     * @return объект ResponseEntity со статусом ответа и найденным пользователем в теле ответа
     */
    @GetMapping("/{login}")
    ResponseEntity<UserDto> findByLogin(@PathVariable("login") String login);

    /**
     * Проверяет существование пользователя с заданным логином.
     *
     * @param login логин пользователя
     * @return объект ResponseEntity со статусом ответа и результатом проверки (true, если пользователь существует, иначе - false)
     */
    @GetMapping("/exists/{login}")
    ResponseEntity<Boolean> existsByLogin(@PathVariable("login") String login);

    /**
     * Создает нового пользователя.
     *
     * @param userCreateDto объект UserCreateDto, содержащий данные для создания пользователя
     * @return объект ResponseEntity со статусом ответа и созданным пользователем в теле ответа
     */
    @PostMapping
    ResponseEntity<UserDto> create(@RequestBody UserCreateDto userCreateDto);

    /**
     * Обновляет дату последнего посещения пользователя.
     *
     * @param login логин пользователя, для которого нужно обновить дату последнего посещения
     * @return объект ResponseEntity со статусом ответа и обновленным пользователем в теле ответа
     */
    @PutMapping("/{login}")
    ResponseEntity<UserDto> updateLastVisit(@PathVariable("login") String login);
}
