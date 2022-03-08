package com.example.mylibrary.service;

import com.example.mylibrary.entity.Type;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TypeService {
    List<Type> findAll();
    Type getById(Integer id);
    void insertOne(Type type);
    void updateById(Type type);
    void deleteById(Integer id);
}
