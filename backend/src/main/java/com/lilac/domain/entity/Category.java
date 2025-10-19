package com.lilac.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * (Category)表实体类
 *
 * @author lilac
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Integer id;
    //书籍类别名称
    private String categoryName;
    //状态（0正常，1异常）
    private String status;
    //描述
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    //逻辑删除（0正常，1删除）
    private String delFlag;

}

