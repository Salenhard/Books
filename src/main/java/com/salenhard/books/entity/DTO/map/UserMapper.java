package com.salenhard.books.entity.DTO.map;

import com.salenhard.books.entity.DTO.UserDto;
import com.salenhard.books.entity.DTO.UserNamesDto;
import com.salenhard.books.entity.User;
import com.salenhard.books.entity.UserNames;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User author);

    User toEntity(UserDto authorDto);

    UserNamesDto toDto(UserNames userNames);

    UserNames toEntity(UserNamesDto userNamesDto);
}
