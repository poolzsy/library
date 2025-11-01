package com.lilac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lilac.constant.ExceptionConstant;
import com.lilac.domain.dto.AddRoleDTO;
import com.lilac.domain.dto.RoleDTO;
import com.lilac.domain.entity.Role;
import com.lilac.domain.vo.PageVO;
import com.lilac.domain.vo.RoleVO;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.exception.SystemException;
import com.lilac.mapper.RoleMapper;
import com.lilac.service.RoleService;
import com.lilac.service.UserRoleService;
import com.lilac.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * (Role)表服务实现类
 *
 * @author lilac
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 通过id查询角色
     *
     * @param id 主键
     */
    @Override
    public Role SelectById(Integer id) {
        // 增加对非法ID的校验
        if (id == null || id <= 0) {
            throw new SystemException(HttpsCodeEnum.DATA_ERROR, ExceptionConstant.ROLE_ID_NOT_NULL);
        }
        return roleMapper.SelectById(id);
    }

    /**
     * 分页查询角色
     */
    @Override
    public PageVO page(RoleDTO roleDTO) {
        PageHelper.startPage(roleDTO.getPageNum(), roleDTO.getPageSize());
        List<Role> roleList = roleMapper.page(roleDTO);
        List<RoleVO> roleVOList = BeanCopyUtils.copyBeanList(roleList, RoleVO.class);
        PageInfo<RoleVO> pageInfo = new PageInfo<>(roleVOList);
        return new PageVO(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 新增角色
     */
    @Override
    public void save(AddRoleDTO addRoleDTO) {
        // 输入校验
        if (!StringUtils.hasText(addRoleDTO.getRoleName())) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EMPTY, ExceptionConstant.ROLE_NAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addRoleDTO.getRoleKey())) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EMPTY, ExceptionConstant.ROLE_KEY_NOT_NULL);
        }
        // 唯一性校验
        Role existingRoleByName = roleMapper.SelectByRoleName(addRoleDTO.getRoleName());
        if (existingRoleByName != null) {
            throw new SystemException(HttpsCodeEnum.DATA_EXIST, ExceptionConstant.ROLE_EXIST);
        }
        Role existingRoleByKey = roleMapper.SelectByRoleKey(addRoleDTO.getRoleKey());
        if (existingRoleByKey != null) {
            throw new SystemException(HttpsCodeEnum.DATA_EXIST, ExceptionConstant.ROLE_KEY_EXIST);
        }

        Role role = new Role();
        BeanUtils.copyProperties(addRoleDTO, role);
        roleMapper.save(role);
    }

    /**
     * 删除角色
     */
    @Override
    public void deleteRoleById(Integer id) {
        // 判断角色是否存在
        Role role = roleMapper.SelectById(id);
        if (role == null) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EXIST, ExceptionConstant.ROLE_NOT_EXIST);
        }
        // 删除用户角色关联
        userRoleService.removeByRoleId(id);
        roleMapper.deleteById(id);
    }

    /**
     * 修改角色
     */
    @Override
    public void update(Role role) {
        // 输入参数校验
        if (role.getId() == null) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EMPTY, ExceptionConstant.ROLE_ID_NOT_NULL);
        }
        if (!StringUtils.hasText(role.getRoleName())) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EMPTY, ExceptionConstant.ROLE_NAME_NOT_NULL);
        }
        // 角色是否存在
        Role existingRole = roleMapper.SelectById(role.getId());
        if (existingRole == null) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EXIST, ExceptionConstant.ROLE_NOT_EXIST);
        }
        // 角色名是否与其它角色冲突
        Role roleWithSameName = roleMapper.SelectByRoleName(role.getRoleName());
        if (roleWithSameName != null && !Objects.equals(roleWithSameName.getId(), role.getId())) {
            throw new SystemException(HttpsCodeEnum.DATA_EXIST, ExceptionConstant.ROLE_EXIST);
        }
        // 角色权限标识是否与其它角色冲突
        if (StringUtils.hasText(role.getRoleKey())) {
            Role roleWithSameKey = roleMapper.SelectByRoleKey(role.getRoleKey());
            if (roleWithSameKey != null && !Objects.equals(roleWithSameKey.getId(), role.getId())) {
                throw new SystemException(HttpsCodeEnum.DATA_EXIST, ExceptionConstant.ROLE_KEY_EXIST);
            }
        }
        // 执行更新
        roleMapper.update(role);
    }

    /**
     * 查询所有角色
     */
    @Override
    public List<Role> listAll() {
        return roleMapper.listAll();
    }
}
