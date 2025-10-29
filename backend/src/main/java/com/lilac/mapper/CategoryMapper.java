package com.lilac.mapper;

import com.lilac.annotation.AutoFill;
import com.lilac.domain.dto.CategoryDTO;
import com.lilac.domain.entity.Category;
import com.lilac.domain.vo.CategoryVO;
import com.lilac.enums.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (Category)表数据库访问层
 * @author lilac
 */
@Mapper
public interface CategoryMapper {
    /**
     * 查询所有分类
     */
    List<Category> page(CategoryDTO categoryDTO);

    /**
     * 新增分类
     */
    @AutoFill(OperationType.INSERT)
    void save(Category category);

    /**
     * 删除分类
     */
    @Update("update category set del_flag = 1 where id = #{id}")
    void delete(Integer id);

    /**
     * 修改分类
     */
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /**
     * 通过id查询分类
     */
    @Select ("select * from category where id = #{id} and del_flag = 0")
    Category SelectById(Integer id);
}