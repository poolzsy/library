package com.lilac.controller;

import com.lilac.domain.dto.AddRoleDTO;
import com.lilac.domain.dto.RoleDTO;
import com.lilac.domain.entity.Role;
import com.lilac.domain.result.Result;
import com.lilac.domain.vo.PageVO;
import com.lilac.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 根据id查询角色
     */
    @GetMapping("/{id}")
    public Result list(@PathVariable Integer id){
        log.info("根据id查询角色");
        return Result.success(roleService.SelectById(id));
    }

    /**
     * 查询角色列表
     */
    @GetMapping("/list")
    public Result list(RoleDTO roleDTO){
        log.info("查询角色");
        PageVO pagevo = roleService.page(roleDTO);
        return Result.success(pagevo);
    }

    /**
     * 新增角色
     */
    @PostMapping("/save")
    public Result save(@RequestBody AddRoleDTO addRoleDTO){
        log.info("新增角色");
        roleService.save(addRoleDTO);
        return Result.success();
    }

    /**
     * 删除用户角色
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteRole(@PathVariable Integer id) {
        log.info("删除用户角色");
        roleService.deleteRoleById(id);
        return Result.success();
    }

    /**
     * 修改角色信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody Role role) {
        log.info("修改角色信息");
        roleService.update(role);
        return Result.success();
    }
}
