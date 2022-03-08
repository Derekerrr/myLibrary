package com.example.mylibrary.dao;

import com.example.mylibrary.entity.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TypeDao {
    List<Type> findAll();
    Type getById(Integer id);
    void insertOne(Type type);
    void updateById(Type type);
    void deleteById(Integer id);
}
