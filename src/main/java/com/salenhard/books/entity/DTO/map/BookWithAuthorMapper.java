package com.salenhard.books.entity.DTO.map;

import com.salenhard.books.entity.Book;
import com.salenhard.books.entity.DTO.BookWithAuthorDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookWithAuthorMapper {

    BookWithAuthorDto toDto(Book book);

    Book toEntity(BookWithAuthorDto bookDto);
}
