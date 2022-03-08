package com.example.mylibrary.dao;

import com.example.mylibrary.entity.Admin;
import com.example.mylibrary.entity.Code;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CodeDao {
    Code getOneByEmail(String email);
    void deleteByEmail(String email);
    void insertOne(Code code);
    Admin getAdmin(String name);
}
