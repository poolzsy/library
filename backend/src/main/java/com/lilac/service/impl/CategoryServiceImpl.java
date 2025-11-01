package com.lilac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lilac.constant.ExceptionConstant;
import com.lilac.domain.dto.AddCategoryDTO;
import com.lilac.domain.dto.CategoryDTO;
import com.lilac.domain.entity.Category;
import com.lilac.domain.vo.CategoryVO;
import com.lilac.domain.vo.PageVO;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.exception.SystemException;
import com.lilac.mapper.CategoryMapper;
import com.lilac.service.CategoryService;
import com.lilac.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

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
    @Transactional
    public void save(AddCategoryDTO addCategoryDTO) {
        //输入参数校验
        if (!StringUtils.hasText(addCategoryDTO.getCategoryName())) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EMPTY, ExceptionConstant.CATEGORY_NAME_NOT_NULL);
        }
        // 检查分类名是否已存在
        Category existingCategory = categoryMapper.findByCategoryName(addCategoryDTO.getCategoryName());
        if (Objects.nonNull(existingCategory)) {
            throw new SystemException(HttpsCodeEnum.DATA_EXIST, ExceptionConstant.CATEGORY_NAME_EXIST);
        }
        Category category = BeanCopyUtils.copyBean(addCategoryDTO, Category.class);
        categoryMapper.save(category);
    }

    /**
     * 删除分类
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        // 检查分类是否存在
        checkCategoryExists(id);
        // TODO: 增加逻辑判断：如果该分类下有关联文章，是否允许删除？
        // if (articleMapper.countByCategoryId(id) > 0) {
        //     throw new SystemException(HttpsCodeEnum.BUSINESS_ERROR, "该分类下有关联文章，无法删除");
        // }
        categoryMapper.delete(id);
    }

    /**
     * 修改分类
     */
    @Override
    @Transactional
    public void update(Category category) {
        // 输入参数校验
        if (Objects.isNull(category.getId())) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EMPTY, ExceptionConstant.CATEGORY_ID_NOT_NULL);
        }
        if (!StringUtils.hasText(category.getCategoryName())) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EMPTY, ExceptionConstant.CATEGORY_NAME_NOT_NULL);
        }
        // 检查分类是否存在
        checkCategoryExists(category.getId());
        // 检查分类名是否已存在
        Category existingCategory = categoryMapper.findByCategoryName(category.getCategoryName());
        if (Objects.nonNull(existingCategory) && !existingCategory.getId().equals(category.getId())) {
            throw new SystemException(HttpsCodeEnum.DATA_EXIST, ExceptionConstant.CATEGORY_NAME_EXIST);
        }
        categoryMapper.update(category);
    }

    /**
     * 通过id查询分类
     */
    @Override
    public Category SelectById(Integer id) {
        Category category = categoryMapper.selectById(id);
        if (Objects.isNull(category)) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EXIST, ExceptionConstant.CATEGORY_NAME_NOT_EXIST);
        }
        return category;
    }

    /**
     * 检查分类是否存在
     */
    private void checkCategoryExists(Integer id) {
        // 检查分类id
        if (Objects.isNull(id) || id <= 0) {
            throw new SystemException(HttpsCodeEnum.DATA_ERROR, ExceptionConstant.CATEGORY_ID_NOT_NULL);
        }
        // 检查分类是否存在
        Category category = categoryMapper.selectById(id);
        if (Objects.isNull(category)) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EXIST, ExceptionConstant.CATEGORY_NAME_NOT_EXIST);
        }
    }
}
