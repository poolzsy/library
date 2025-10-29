package com.lilac.controller;

import com.lilac.domain.dto.AddCategoryDTO;
import com.lilac.domain.dto.CategoryDTO;
import com.lilac.domain.entity.Category;
import com.lilac.domain.result.Result;
import com.lilac.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService CategoryService;

    /**
     * 查询所有分类
     */
    @GetMapping("/list")
    public Result list(CategoryDTO categoryDTO){
        log.info("查询所有分类");
        return Result.success(CategoryService.page(categoryDTO));
    }

    /**
     * 增加分类
     */
    @PostMapping ("/save")
    public Result save(@RequestBody AddCategoryDTO addCategoryDTO ) {
        log.info("增加分类");
        CategoryService.save(addCategoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除分类");
        CategoryService.delete(id);
        return Result.success();
    }

    /**
     * 修改分类
     */
    @PutMapping ("/update")
    public Result update(@RequestBody Category category) {
        log.info("修改分类");
        CategoryService.update(category);
        return Result.success();
    }

    /**
     * 查询分类
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("查询分类");
        return Result.success(CategoryService.SelectById(id));
    }
}