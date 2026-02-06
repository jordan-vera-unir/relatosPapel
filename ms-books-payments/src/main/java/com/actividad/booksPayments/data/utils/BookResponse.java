package com.actividad.booksPayments.data.utils;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class BookResponse {

    private String title;
    private String author;
    private String editorial;
    private Integer pages;
    private List<String> genres;
    private LocalDate publishedDate;
    private Integer rating;
    private Double price;
    private String coverImage;
    private String dimensions;
    private Integer stock;
    private Boolean visible = false;

}
