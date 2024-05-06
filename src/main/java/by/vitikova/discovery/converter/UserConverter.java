package by.vitikova.discovery.converter;

import by.vitikova.discovery.UserDto;
import by.vitikova.discovery.create.UserCreateDto;
import by.vitikova.discovery.model.entity.User;
import org.mapstruct.Mapper;
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
}
