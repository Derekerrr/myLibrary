package com.example.mylibrary.service;

import com.example.mylibrary.entity.Borrow;
import com.example.mylibrary.entity.query.BorrowQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface BorrowService {
    void insertOne(Borrow borrow);
    PageInfo<Borrow> findByCondition(BorrowQuery borrowQuery);
    List<Borrow> findByIsbn(String isbn);
    Borrow getById(Integer id);
    void updateOne(Borrow borrow);
    void deleteById(Integer id);

}
