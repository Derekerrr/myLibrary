package com.example.mylibrary.entity.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberQuery {
    private Integer pageNum = 1;//当前的页码
    private Integer pageSize = 10; //每一页所显示的数量

    private String name;
    private String email;
}
