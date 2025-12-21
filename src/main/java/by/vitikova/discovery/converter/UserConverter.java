package by.vitikova.discovery.converter;

import by.vitikova.discovery.auth.SignUpCreateDto;
import by.vitikova.discovery.create.UserCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

    UserCreateDto convert(SignUpCreateDto source);
}