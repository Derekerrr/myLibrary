package com.example.mylibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String isbn;
    private String name;
    private Integer type;
    private Integer number;
    private String author;
    private String price;
    private String description;
}
