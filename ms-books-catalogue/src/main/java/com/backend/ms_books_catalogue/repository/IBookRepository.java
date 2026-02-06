package com.backend.ms_books_catalogue.repository;

import com.backend.ms_books_catalogue.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {
    
}
