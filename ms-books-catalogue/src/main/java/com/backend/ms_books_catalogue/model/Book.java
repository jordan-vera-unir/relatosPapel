package com.backend.ms_books_catalogue.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title is required")
    @Size(max = 60)
    private String title;

    @NotBlank(message = "author is required")
    private String author;

    private String editorial;

    @Min(value = 1, message = "Must have 1 page at least")
    private Integer pages;

    @NotEmpty(message = "Must have 1 genre at least")
    private List<String> genres;

    @NotNull
    @PastOrPresent(message = "Date must be previous today")
    private LocalDate publishedDate;

    @Min(1) @Max(5)
    private Integer rating;

    @DecimalMin(value = "0.0", message = "Price must be positive number")
    private Double price;

    @org.hibernate.validator.constraints.URL(message = "Image must be a valid url")
    private String coverImage;

    @NotNull
    private String dimensions;

    @Min(value = 0, message = "stock must be positive number")
    private Integer stock;

    @NotNull
    private Boolean visible = false;
}
