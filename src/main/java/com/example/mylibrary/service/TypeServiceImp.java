package com.example.mylibrary.service;

import com.example.mylibrary.dao.TypeDao;
import com.example.mylibrary.entity.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImp implements TypeService{

    @Autowired
    private TypeDao typeDao;

    @Override
    public List<Type> findAll() {
        return typeDao.findAll();
    }

    @Override
    public Type getById(Integer id) {
        return typeDao.getById(id);
    }

    @Override
    public void insertOne(Type type) {
        typeDao.insertOne(type);
    }

    @Override
    public void updateById(Type type) {
        typeDao.updateById(type);
    }

    @Override
    public void deleteById(Integer id) {
        typeDao.deleteById(id);
    }
}
