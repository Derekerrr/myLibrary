package com.example.mylibrary.service;

import com.example.mylibrary.entity.Apply;
import com.example.mylibrary.entity.query.ApplyQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApplyService {
    void insertOne(Apply apply);
    List<Apply> getByCondition(ApplyQuery applyQuery);
    void deleteById(Integer id);
    Apply getById(Integer id);
}
