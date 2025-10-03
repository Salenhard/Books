package com.salenhard.books.entity.DTO.map;

import com.salenhard.books.entity.Book;
import com.salenhard.books.entity.DTO.BookWithAuthorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookWithAuthorMapper {
    @Mapping(source = "user.userNames.name", target = "user.name")
    @Mapping(source = "user.userNames.middleName", target = "user.middleName")
    @Mapping(source = "user.userNames.secondName", target = "user.secondName")
    BookWithAuthorDto toDto(Book book);
    @Mapping(target = "user.userNames.name", source = "user.name")
    @Mapping(target = "user.userNames.middleName", source = "user.middleName")
    @Mapping(target = "user.userNames.secondName", source = "user.secondName")
    Book toEntity(BookWithAuthorDto bookDto);
}
