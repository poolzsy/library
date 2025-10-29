package com.lilac.service;

import com.lilac.domain.dto.AddCategoryDTO;
import com.lilac.domain.dto.CategoryDTO;
import com.lilac.domain.entity.Category;
import com.lilac.domain.vo.PageVO;

/**
 * (Category)表服务接口
 *
 * @author lilac
 */
public interface CategoryService {
    /**
     * 查询所有分类
     */
    PageVO page(CategoryDTO categoryDTO);

    /**
     * 新增分类
     */
    void save(AddCategoryDTO addCategoryDTO);

    /**
     * 删除分类
     */
    void delete(Integer id);

    /**
     * 修改分类信息
     */
    void update(Category category);

    /**
     * 通过id查询分类
     */
    Category SelectById(Integer id);
}