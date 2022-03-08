package com.example.mylibrary.entity.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowQuery {
    private Integer pageNum = 1;//当前的页码
    private Integer pageSize = 10; //每一页所显示的数量

    private Integer member_id = null;
    private String member_name;
    private String book_name;

    /*
     * 1:全部
     * 2：未还
     * 3：已还
     * 4：已预期
     */
    private Integer status = 1;

    //2 正序  1 倒序
    private Integer order = 1;

    private String status2 = null;
    private String status3 = null;
    private String status4 = null;
    private String order1 = null;
    private String order2 = null;

}
