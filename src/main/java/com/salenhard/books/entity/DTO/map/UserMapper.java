package com.salenhard.books.entity.DTO.map;

import com.salenhard.books.entity.DTO.UserDto;
import com.salenhard.books.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userNames.name", target = "name")
    @Mapping(source = "userNames.middleName", target = "middleName")
    @Mapping(source = "userNames.secondName", target = "secondName")
    UserDto toDto(User user);
    @Mapping(target = "userNames.name", source = "name")
    @Mapping(target = "userNames.middleName", source = "middleName")
    @Mapping(target = "userNames.secondName", source = "secondName")
    User toEntity(UserDto userDto);

}
