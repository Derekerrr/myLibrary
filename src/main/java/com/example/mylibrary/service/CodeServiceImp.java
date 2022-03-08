package com.example.mylibrary.service;

import com.example.mylibrary.dao.CodeDao;
import com.example.mylibrary.entity.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImp implements CodeService{

    @Autowired
    private CodeDao codeDao;

    @Override
    public Code getOneByEmail(String email) {
        return codeDao.getOneByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        codeDao.deleteByEmail(email);
    }

    @Override
    public void insertOne(Code code) {
        codeDao.insertOne(code);
    }
}
