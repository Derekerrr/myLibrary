package com.example.mylibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyMember {
    private String email;
    private String password1;
    private String password2;
    private String code;
}
