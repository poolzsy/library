package com.lilac.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {
    private Integer id;
    //书籍类别名称
    private String categoryName;
    //状态（0正常，1异常）
    private String status;
    //描述
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}