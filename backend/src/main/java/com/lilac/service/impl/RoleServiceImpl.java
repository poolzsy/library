package com.lilac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lilac.domain.dto.AddRoleDTO;
import com.lilac.domain.dto.RoleDTO;
import com.lilac.domain.entity.Role;
import com.lilac.domain.vo.PageVO;
import com.lilac.mapper.RoleMapper;
import com.lilac.service.RoleService;
import com.lilac.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Role)表服务实现类
 *
 * @author lilac
 */
@Service("roleService")
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 通过id查询角色
     *
     * @param id 主键
     */
    @Override
    public Role SelectById(Integer id) {
        return roleMapper.SelectById(id);
    }

    /**
     * 分页查询角色
     */
    @Override
    public PageVO page(RoleDTO roleDTO) {
        PageHelper.startPage(roleDTO.getPageNum(), roleDTO.getPageSize());
        List<Role> roleList = roleMapper.page(roleDTO);
        List<Role> roleVOList = BeanCopyUtils.copyBeanList(roleList, Role.class);
        PageInfo<Role> pageInfo = new PageInfo<>(roleVOList);
        return new PageVO(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 新增角色
     */
    @Override
    public void save(AddRoleDTO addRoleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(addRoleDTO, role);
        roleMapper.save(role);
    }

    /**
     * 删除角色
     */
    @Override
    public void deleteRoleById(Integer id) {
        roleMapper.deleteById(id);
    }

    /**
     * 修改角色
     */
    @Override
    public void update(Role role) {
        roleMapper.update(role);
    }
}
