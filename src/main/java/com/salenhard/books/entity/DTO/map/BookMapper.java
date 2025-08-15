package com.salenhard.books.entity.DTO.map;

import com.salenhard.books.entity.Book;
import com.salenhard.books.entity.DTO.BookDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);
}
