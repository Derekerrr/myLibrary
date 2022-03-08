package com.example.mylibrary.entity.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookQuery {
    private Integer pageNum = 1;//当前的页码
    private Integer pageSize = 10; //每一页所显示的数量
    private String searchName = null;
    private String searchAuthor = null;
    private Integer searchType = null;
}
