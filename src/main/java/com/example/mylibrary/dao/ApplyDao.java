package com.example.mylibrary.dao;

import com.example.mylibrary.entity.Apply;
import com.example.mylibrary.entity.query.ApplyQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ApplyDao {
    List<Apply> getByCondition(ApplyQuery applyQuery);
    void insertOne(Apply apply);
    void deleteById(Integer id);
    Apply getById(Integer id);
}
