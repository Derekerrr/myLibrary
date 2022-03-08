package com.example.mylibrary.dao;

import com.example.mylibrary.entity.Borrow;
import com.example.mylibrary.entity.query.BorrowQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface BorrowDao {
    void insertOne(Borrow borrow);
    List<Borrow> findByCondition(BorrowQuery borrowQuery);
    List<Borrow> findByIsbn(String isbn);
    Borrow getById(Integer id);
    void updateOne(Borrow borrow);
    Date lastUpdateTime();
    void updateLastDate(Date last_update_time);
    void deleteById(Integer id);
}
