package com.example.mylibrary.service;

import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.query.BookQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    //条件查询
    PageInfo<Book> getBookList(BookQuery bookQuery);
    void insertBook(Book book);
    Book getByIsbn(String isbn);
    void modifyByIsbn(Book book);
    void deleteByIsbn(String isbn);
}
