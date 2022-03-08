package com.example.mylibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {
    private Integer id;
    private Integer member_id;
    private String member_name;
    private String isbn;
    private String book_name;
    private Date borrow_time;
    private Boolean is_returned;
    private Boolean is_overdue;
    private Date return_time;
    private Integer days = 30; //默认借书天数
    private Boolean is_punished = false;
}
