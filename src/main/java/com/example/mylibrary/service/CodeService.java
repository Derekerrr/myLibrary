package com.example.mylibrary.service;

import com.example.mylibrary.entity.Code;

public interface CodeService {
    Code getOneByEmail(String email);
    void deleteByEmail(String email);
    void insertOne(Code code);
}
