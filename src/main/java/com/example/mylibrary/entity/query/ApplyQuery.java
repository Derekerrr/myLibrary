package com.example.mylibrary.entity.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyQuery {
    private Integer member_id;
    private String member_name;
}
