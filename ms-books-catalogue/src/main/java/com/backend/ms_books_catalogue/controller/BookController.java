package com.backend.ms_books_catalogue.controller;

import com.backend.ms_books_catalogue.model.Book;
import com.backend.ms_books_catalogue.service.IBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService service;

//    @GetMapping
//    public ResponseEntity<List<Book>> getAllBooks(
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) String author,
//            @RequestParam(required = false) String editorial,
//            @RequestParam(required = false) String genre,
//            @RequestParam(required = false) Double ratingMin,
//            @RequestParam(required = false) Boolean visible) {
//
//        List<Book> books = service.findAll(title, author, editorial, genre, ratingMin, visible);
//        return ResponseEntity.ok(books);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        Book createdBook = service.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @Valid @RequestBody Book book) {
        return service.update(id, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> partialUpdate(@PathVariable Long id, @RequestBody Book partialBook) {
        return service.partialUpdate(id, partialBook)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
