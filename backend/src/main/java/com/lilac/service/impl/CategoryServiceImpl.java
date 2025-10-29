package com.lilac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lilac.domain.dto.AddCategoryDTO;
import com.lilac.domain.dto.CategoryDTO;
import com.lilac.domain.entity.Category;
import com.lilac.domain.vo.CategoryVO;
import com.lilac.domain.vo.PageVO;
import com.lilac.mapper.CategoryMapper;
import com.lilac.service.CategoryService;
import com.lilac.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Category)表服务实现类
 *
 * @author lilac
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询所有分类
     */
    @Override
    public PageVO page(CategoryDTO categoryDTO) {
        PageHelper.startPage(categoryDTO.getPageNum(), categoryDTO.getPageSize());
        List<Category> categoryList = categoryMapper.page(categoryDTO);
        List<CategoryVO> categoryVOList = BeanCopyUtils.copyBeanList(categoryList, CategoryVO.class);
        PageInfo<CategoryVO> pageInfo = new PageInfo<>(categoryVOList);
        PageVO page = new PageVO(pageInfo.getTotal(), pageInfo.getList());
        return page;
    }

    /**
     * 新增分类
     */
    @Override
    public void save(AddCategoryDTO addCategoryDTO) {
        Category category = BeanCopyUtils.copyBean(addCategoryDTO, Category.class);
        categoryMapper.save(category);
    }

    /**
     * 删除分类
     */
    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }

    /**
     * 修改分类
     */
    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }

    /**
     * 通过id查询分类
     */
    @Override
    public Category SelectById(Integer id) {
        return categoryMapper.SelectById(id);
    }
}
