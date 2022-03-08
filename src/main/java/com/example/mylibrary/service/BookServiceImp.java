package com.example.mylibrary.service;

import com.example.mylibrary.dao.BookDao;
import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.query.BookQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImp implements BookService{

    @Autowired
    private BookDao bookDao;

    @Override
    public PageInfo<Book> getBookList(BookQuery bookQuery) {
        PageHelper.startPage(bookQuery.getPageNum(),bookQuery.getPageSize());
        return new PageInfo<>(bookDao.getBookList(bookQuery));
    }

    @Override
    public void insertBook(Book book) {
        bookDao.insertBook(book);
    }

    @Override
    public Book getByIsbn(String isbn) {
        return bookDao.getByIsbn(isbn);
    }

    @Override
    public void modifyByIsbn(Book book) {
        bookDao.modifyByIsbn(book);
    }

    @Override
    public void deleteByIsbn(String isbn) {
        bookDao.deleteByIsbn(isbn);
    }
}
