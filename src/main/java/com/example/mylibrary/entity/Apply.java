package com.example.mylibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apply {
    private Integer id;
    private Integer member_id;
    private Integer type;//1：借书申请 2：还书申请 3：续借申请
    private Integer borrow_id;
    private String isbn;
    private Date time; //申请时间
    private Integer days;//续借天数
    private String book_name;
    private String member_name;
}
