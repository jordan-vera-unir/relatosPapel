package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.model.Book;

import java.util.List;
import java.util.Optional;

public interface IBookService {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    Optional<Book> update(Long id, Book book);

    Optional<Book> partialUpdate(Long id, Book partialBook);

    boolean delete(Long id);
}
