package com.example.mylibrary.service;

import com.example.mylibrary.dao.ApplyDao;
import com.example.mylibrary.entity.Apply;
import com.example.mylibrary.entity.query.ApplyQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyServiceImp implements ApplyService{

    @Autowired

    private ApplyDao applyDao;

    @Override
    public void insertOne(Apply apply) {
        applyDao.insertOne(apply);
    }

    @Override
    public List<Apply> getByCondition(ApplyQuery applyQuery) {
        return applyDao.getByCondition(applyQuery);
    }

    @Override
    public void deleteById(Integer id) {
        applyDao.deleteById(id);
    }

    @Override
    public Apply getById(Integer id) {
        return applyDao.getById(id);
    }
}
