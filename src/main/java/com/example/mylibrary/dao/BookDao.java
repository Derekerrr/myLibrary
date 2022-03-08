package com.example.mylibrary.dao;

import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.query.BookQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookDao {
    //条件查询
    List<Book> getBookList(BookQuery bookQuery);
    void insertBook(Book book);
    Book getByIsbn(String isbn);
    //修改
    void modifyByIsbn(Book book);
    //删除
    void deleteByIsbn(String isbn);
}
