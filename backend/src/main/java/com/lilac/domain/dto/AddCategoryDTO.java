package com.lilac.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryDTO {
    //书籍类别名称
    private String categoryName;
    //状态（0正常，1异常）
    private String status;
    //描述
    private String description;
}
