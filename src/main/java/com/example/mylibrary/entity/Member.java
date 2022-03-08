package com.example.mylibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private int id;
    private String name;
    private String email;
    private String password;
    private Date birthday;
    private float money;
}
