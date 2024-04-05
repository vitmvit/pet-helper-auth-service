package by.vitikova.discovery.converter;

import by.vitikova.discovery.UserDto;
import by.vitikova.discovery.create.UserCreateDto;
import by.vitikova.discovery.model.entity.User;
import by.vitikova.discovery.update.UserUpdateDateDto;
import by.vitikova.discovery.update.UserUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

    /**
     * Преобразование объекта UserDto в объект User
     *
     * @param source исходный комментарий типа User
     * @return преобразованный комментарий типа UserDto
     */
    User convert(UserDto source);

    /**
     * Преобразование объекта UserDto в объект UserCreateDto
     *
     * @param source исходный DTO для создания комментария типа UserCreateDto
     * @return преобразованный комментарий типа User
     */
    UserCreateDto convertToUser(User source);

    /**
     * Преобразование объекта UserUpdateDto в объект User.
     *
     * @param source исходный объект UserUpdateDto
     * @return преобразованный объект User
     */
    User convert(UserUpdateDto source);

    /**
     * Преобразование объекта UserUpdateDateDto в объект User.
     *
     * @param source исходный объект UserUpdateDateDto
     * @return преобразованный объект User
     */
    User convert(UserUpdateDateDto source);

    /**
     * Обновление полей объекта User на основе данных из UserUpdateDto.
     *
     * @param user объект User, который нужно обновить
     * @param dto  объект UserUpdateDto с обновленными данными
     * @return обновленный объект User
     */
    User merge(@MappingTarget User user, UserUpdateDto dto);

    /**
     * Обновление полей объекта User на основе данных из UserUpdateDateDto.
     *
     * @param user объект User, который нужно обновить
     * @param dto  объект UserUpdateDateDto с обновленными данными даты
     * @return обновленный объект User
     */
    User merge(@MappingTarget User user, UserUpdateDateDto dto);
}
