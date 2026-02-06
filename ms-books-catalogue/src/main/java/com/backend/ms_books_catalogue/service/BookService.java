package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.model.Book;
import com.backend.ms_books_catalogue.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {
    private final BookRepository repository;

    @Override
    public List<Book> findAll() {
        return List.of();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Book save(Book book) {
        return repository.save(book);
    }

    @Override
    @Transactional
    public Optional<Book> update(Long id, Book bookDetails) {
        return repository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setEditorial(bookDetails.getEditorial());
            book.setPages(bookDetails.getPages());
            book.setGenres(bookDetails.getGenres());
            book.setPublishedDate(bookDetails.getPublishedDate());
            book.setRating(bookDetails.getRating());
            book.setPrice(bookDetails.getPrice());
            book.setCoverImage(bookDetails.getCoverImage());
            book.setDimensions(bookDetails.getDimensions());
            book.setStock(bookDetails.getStock());
            book.setVisible(bookDetails.getVisible());
            return repository.save(book);
        });
    }

    @Override
    @Transactional
    public Optional<Book> partialUpdate(Long id, Book partialBook) {
        return repository.findById(id).map(existingBook -> {
            if (partialBook.getRating() != null) existingBook.setRating(partialBook.getRating());
            if (partialBook.getVisible() != null) existingBook.setVisible(partialBook.getVisible());
            if (partialBook.getPrice() != null) existingBook.setPrice(partialBook.getPrice());
            if (partialBook.getStock() != null) existingBook.setStock(partialBook.getStock());
            return repository.save(existingBook);
        });
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return repository.findById(id).map(book -> {
            repository.delete(book);
            return true;
        }).orElse(false);
    }
}
