package com.backend.ms_books_catalogue.repository;

import com.backend.ms_books_catalogue.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final IBookRepository jpaRepository;

    public List<Book> findAll() {
        return jpaRepository.findAll();
    }

    public Book findById(Long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    public Book save(Book Book) {
        return jpaRepository.save(Book);
    }

    public void delete(Book Book) {
        jpaRepository.delete(Book);
    }


}
