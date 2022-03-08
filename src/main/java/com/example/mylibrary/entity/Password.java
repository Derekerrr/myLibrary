package com.example.mylibrary.entity;

import lombok.Data;

@Data
public class Password {
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;
}
