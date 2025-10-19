package com.lilac.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * (Book)表实体类
 *
 * @author lilac
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private Integer id;
    //图书名称
    private String bookName;
    //作家名字
    private String author;
    //图书类别id
    private Integer categoryId;
    //状态（0正常，1异常）
    private String status;
    //图书标签
    private String tag;
    //出版社
    private String publisher;
    //国际标准书号
    private String isbn;
    //图书馆数量
    private Integer num;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    //逻辑删除(0正常，1删除)
    private String delFlag;

}

